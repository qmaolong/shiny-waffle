package com.covilla.service.food;

import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.food.Category;
import com.covilla.model.mongo.food.Food;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.Children;
import com.covilla.service.BaseMongoService;
import com.covilla.util.MiscUtils;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qmaolong on 2016/8/30.
 */
@Service
@Transactional
public class CategoryService extends BaseMongoService<Shop> {
    @Autowired
    private ShopMongoDao shopMongoDao;
    @Autowired
    private FoodService foodService;

    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    /**
     * 新增大类
     * @param shopId
     */
    public void addCategoryToShop(ObjectId shopId, Category category){
        Integer categoryId = generate2thLevelId(shopId, "categories");
        category.setId(Category.transIdToString(categoryId));

        Criteria criteria =  Criteria.where("_id").is(shopId);
        Update update = new Update();
        update.push("categories", category);

        shopMongoDao.getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    /**
     * 删除大类
     * @param shopId
     */
    public void deleteCategoryFromShop(ObjectId shopId, List<Category> categories){
        Shop shop = shopMongoDao.findBy_id(shopId);
        if (ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCategories())){
            for (int i=0; i<shop.getCategories().size(); i++){
                for (Category category : categories){
                    if(shop.getCategories().get(i).getId().equals(category.getId())){
                        shop.getCategories().remove(i);
                        break;
                    }
                }
            }
        }
        Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));
        shopMongoDao.getMongoTemplate().updateFirst(query, new Update().set("categories", shop.getCategories()), Shop.class);

        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    /**
     * 编辑大类
     * @param shopId
     * @param category
     */
    public void updateCategory(ObjectId shopId, Category category){
        Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where("categories.id").is(category.getId()));
        Update update = new Update().set("categories.$", category);

        shopMongoDao.getMongoTemplate().updateFirst(query, update, Shop.class);
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    public void editChildren(ObjectId shopId, Children children, String oper){
        Shop shop = findBy_id(shopId);

        Category category = null;
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCategories())){
            for (Category temp : shop.getCategories()){
                if(temp.getId().equals(children.getParentId())){
                    category = temp;
                    break;
                }
            }
            if(OperationEnum.add.getCode().equals(oper)){
                children.setId(category.getId() + getChildrenIdFromCategory(shopId, category.getId()));
                if(ValidatorUtil.isNotNull(category.getChildren())){
                    category.getChildren().add(children);
                }else {
                    List<Children> childrenList = new ArrayList<Children>();
                    childrenList.add(children);
                    category.setChildren(childrenList);
                }
            }else if(OperationEnum.edit.getCode().equals(oper) && ValidatorUtil.isNotNull(category.getChildren())){
                for(int i=0; i<category.getChildren().size(); i++){
                    Children child = category.getChildren().get(i);
                    if(child.getId().equals(children.getId())){
                        category.getChildren().set(i, children);
                        break;
                    }
                }
            }
        }

        /*Update update = new Update().set("categories.$.children", category.getChildren());
        shopMongoDao.getMongoTemplate().updateFirst(new BasicQuery(dbObject), update, Shop.class);*/

        updateByMap(MiscUtils.toMap("_id", shopId), MiscUtils.toMap("categories", shop.getCategories()));
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    public void deleteChildrens(ObjectId shopId, List<Children> children){
        Shop shop = findBy_id(shopId);

        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCategories())){
            OK:
            for (Category category : shop.getCategories()){
                if(ValidatorUtil.isNotNull(category.getChildren())){
                    for(int i=0; i<category.getChildren().size(); i++){
                        for(Children child : children){
                            if(child.getId().equals(category.getChildren().get(i).getId())){
                                category.getChildren().remove(i);
                                break OK;
                            }
                        }
                    }
                }
            }

        }

        /*Update update = new Update().set("categories.$.children", category.getChildren());
        shopMongoDao.getMongoTemplate().updateFirst(new BasicQuery(dbObject), update, Shop.class);*/
        updateByMap(MiscUtils.toMap("_id", shopId), MiscUtils.toMap("categories", shop.getCategories()));
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    /**
     * 数据分析获取（非原子性）
     * @return
     */
    public String getChildrenIdFromCategory(ObjectId shopId, String cateId){
        Integer childrenId = generate3thLevelId(shopId, "categories", cateId, "children", true);;

        if(childrenId < 9){
            return "0" + childrenId;
        }

        return childrenId.toString();
    }

    public List<Children> getAllChildrenCate(ObjectId shopId){
        List<Children> result = new ArrayList<Children>();
        Shop shop = shopMongoDao.findOne(shopId);
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCategories())){
            for (Category category : shop.getCategories()){
                for(Children child : category.getChildren()){
                    result.add(child);
                }
            }
        }
        return result;
    }

    public List<String> getAllChildrenCateNo(ObjectId shopId){
        List<String > result = new ArrayList<String>();
        Shop shop = shopMongoDao.findOne(shopId);
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCategories())){
            for (Category category : shop.getCategories()){
                for(Children child : category.getChildren()){
                    result.add(child.getId());
                }
            }
        }
        return result;
    }

    public String getCategoryNameById(String id, ObjectId shopId){
        Shop shop = shopMongoDao.findBy_id(shopId);
        if(ValidatorUtil.isNotNull(shop)&&ValidatorUtil.isNotNull(shop.getCategories())){
            for (Category category : shop.getCategories()){
                if(category.getId().equals(id)){
                    return category.getName();
                }
            }
        }
        return "";
    }

    public Long countFoodByCategory(String categoryId, ObjectId shopId){
        Query query = new Query(Criteria.where("owner").is(shopId).and("cat").is(categoryId));
        return foodService.getMongoTemplate().count(query, Food.class);
    }

}
