package com.covilla.service.food;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.covilla.common.Constant;
import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.FoodMongoDao;
import com.covilla.model.mongo.food.Category;
import com.covilla.model.mongo.food.Food;
import com.covilla.model.mongo.food.FoodOption;
import com.covilla.model.mongo.food.OptionContainer;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.Children;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.util.ContentUtil;
import com.covilla.util.SerializationUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.food.FoodImportVo;
import jodd.typeconverter.Convert;
import org.apache.commons.collections.map.HashedMap;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by qmaolong on 2016/8/30.
 */
@Service
@Transactional
public class FoodService extends BaseMongoService<Food> {
    private Logger logger = LoggerFactory.getLogger(FoodService.class);
    @Autowired
    private FoodMongoDao foodMongoDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShopService shopService;
    protected BaseMongoDao<Food> getBaseMongoDao(){
        return foodMongoDao;
    }

    public List<Food> getAvailableFoods(ObjectId shopId, String cate){
        Query query = new Query(Criteria.where("owner").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("cat").is(cate));
        return getMongoTemplate().find(query, Food.class);
    }

    public void editFood(String dataStr, String oper, ObjectId shopId){
        if(OperationEnum.add.getCode().equals(oper)){
            Food food = SerializationUtil.deSerializeObject(dataStr, Food.class);
            Shop shop = shopService.findBy_id(shopId);
            food.setOwner(shop.get_id());
//            food.setId(generateFoodId(food.getCat(), shop.get_id()));
            List<String> printers = new ArrayList<String>();
            if(ValidatorUtil.isNotNull(food.getPrintersStr())){
                String[] names = food.getPrintersStr().split(",");
                for(String name : names){
                    printers.add(name);
                }
                food.setPrinters(printers);
            }
            Food existFood = findByFoodId(food.getId(), shopId);
            if (ValidatorUtil.isNotNull(existFood)){
                throw new ServiceException("ID已存在");
            }
            if(ValidatorUtil.isNull(food.getAbc())){
                String abc = ContentUtil.getPinYinHeadChar(food.getName());
                food.setAbc(abc.toUpperCase());
            }
            insert(food);
            updateModifyTime("shop", shopId, ModifyBlockEnum.food.getKey());
        }else if(OperationEnum.edit.getCode().equals(oper)){
            Food food = SerializationUtil.deSerializeObject(dataStr, Food.class);
            Food existFood = findByFoodId(food.getId(), shopId);
            if (ValidatorUtil.isNotNull(existFood) && !food.get_id().equals(existFood.get_id())){
                throw new ServiceException("ID已存在");
            }
            if(ValidatorUtil.isNull(food.getAbc())){//自动获取助记码
                String abc = ContentUtil.getPinYinHeadChar(food.getName());
                food.setAbc(abc.toUpperCase());
            }
            List<String> printers = new ArrayList();
            if(ValidatorUtil.isNotNull(food.getPrintersStr())){
                String[] names = food.getPrintersStr().split(",");
                for(String name : names){
                    printers.add(name);
                }
            }
            food.setPrinters(printers);
            updateDocument(food);
            editRefFood(food);//修改关联菜
            updateModifyTime("shop", shopId, ModifyBlockEnum.food.getKey());
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<Food> foodList = SerializationUtil.deSerializeList(dataStr, Food.class);
            deleteFood(foodList, shopId);
        }else {
            throw new ServiceException("操作失败！");
        }
    }

    /**
     * 修改菜品信息时，同时修改被引用后的菜品信息
     * @param food
     */
    public void editRefFood(Food food){
        if (ValidatorUtil.isNull(food.get_id())){
            return;
        }
        Query query = new Query(Criteria.where("refId").is(food.get_id()));
        Update update = new Update().set("name", food.getName())
                .set("price1", food.getPrice1()).set("price2", food.getPrice2())
                .set("price3", food.getPrice3()).set("pricem", food.getPricem());
        getMongoTemplate().updateMulti(query, update, Food.class);
    }

    public void deleteFood(List<Food> foodList, ObjectId shopId){
        List<String> ids = new ArrayList<String>();
        for(Food food : foodList){
            ids.add(food.getId());
        }
        Query query = new Query(Criteria.where("id").in(ids).and("owner").is(shopId));
        getMongoTemplate().remove(query, Food.class);//物理删除
        updateModifyTime("shop", foodList.get(0).getOwner(), ModifyBlockEnum.food.getKey());
    }

    /**
     * 根据门店获取
     * @param owner
     * @return
     */
    public List<Food> findByOwner(ObjectId owner){
        Query query = new Query(Criteria.where("owner").is(owner).and("dataStatus").ne(Constant.DATA_STATUS_INVALID));
        return  foodMongoDao.getMongoTemplate().find(query, Food.class);
    }

    /**
     * 生成foodId
     * @param cateId
     * @param shop_id
     * @return
     */
    public String generateFoodId(String cateId, ObjectId shop_id){
        Query query = new Query().addCriteria(Criteria.where("owner").is(shop_id).and("cat").is(cateId));
        List<Food> foodList = foodMongoDao.getMongoTemplate().find(query, Food.class);
        JSONArray array = JSONArray.parseArray(JSONArray.toJSONString(foodList));
        Integer newId = findUntakenId(array, cateId);

        if(newId < 10){
            return cateId + "0000" + newId;
        }else if(newId < 100){
            return cateId + "000" + newId;
        }else if(newId < 1000){
            return cateId + "00" + newId;
        }else if(newId < 10000){
            return cateId + "0" + newId;
        }

        return cateId + newId;
    }

    public Food findByFoodId(String id, ObjectId shopId){
        Query query = new Query(Criteria.where("owner").is(shopId).and("id").is(id).and("dataStatus").ne(Constant.DATA_STATUS_INVALID));
        List<Food>  foodList = foodMongoDao.getMongoTemplate().find(query, Food.class);
        if(ValidatorUtil.isNotNull(foodList)){
            return foodList.get(0);
        }
        return null;
    }

    /**
     * 批量设置打印机
     * @param foodsStr
     * @param formData
     * @param shopId
     */
    public void batchSetPrinters(String foodsStr, String formData, ObjectId shopId){
        List<Food> foods = SerializationUtil.deSerializeList(foodsStr, Food.class);
        JSONObject object = JSONObject.parseObject(formData);

        List<String> ids = new ArrayList<String>();
        for (Food food: foods){
            ids.add(food.getId());
        }

        foodMongoDao.batchSetPrinters(shopId, ids, object);
        updateModifyTime("shop", shopId, ModifyBlockEnum.food.getKey());
    }

    /**
     * 解析导入菜品文件
     * @param contentStr
     * @param shopId
     * @return
     */
    public List<FoodImportVo> importPreview(String contentStr, ObjectId shopId) {
        String[] rows = contentStr.split("\n");

        List<String> allCateId = categoryService.getAllChildrenCateNo(shopId);
        List<FoodImportVo> result = new ArrayList<FoodImportVo>();

        for (String row : rows){
            String type = checkImportColumnType(row);
            String[] fields = row.split(",");
            if("food".equals(type)){
                FoodImportVo temp = new FoodImportVo();
                try {
                    temp.setType(Food.TYPE_FOOD);
                    temp.setName(fields[0]);
                    temp.setCateId(fields[1]);
                    if(fields.length>6)
                        temp.setPrinter1(fields[6]);
                    if(fields.length>7)
                        temp.setPrinter2(fields[7]);
                    if(fields.length>8)
                        temp.setPrinter3(fields[8]);
                    if(fields.length>9)
                        temp.setPrinterControl(fields[9]);
                    if(fields.length>10)
                        temp.setPrinterPolicy(fields[10]);
                    if(fields.length>2)
                        temp.setPrice1(Convert.toDouble(fields[2]));
                    if(fields.length>3)
                        temp.setPrice2(Convert.toDouble(fields[3]));
                    if(fields.length>4)
                        temp.setPrice3(Convert.toDouble(fields[4]));
                    if(fields.length>5)
                        temp.setPricem(Convert.toDouble(fields[5]));
                }catch (Exception e){
                    logger.error(e.getMessage());
                    temp.setPass(false);
                    temp.setNoPassReason("数据格式错误");
                }
                if(allCateId.indexOf(fields[1])<0){
                    temp.setPass(false);
                    temp.setNoPassReason("小类编号不存在");
                }else if(fields.length<3){
                    temp.setPass(false);
                    temp.setNoPassReason("数据不完整");
                }
                result.add(temp);
            }else if("option".equals(type)){
                OptionContainer container = new OptionContainer();
                container.setName(fields[1]);
                for (int i=2; i<fields.length; i++){
                    FoodOption option = new FoodOption();
                    option.setName(fields[i]);
                    option.setId(i-1 + "");
                    if(ValidatorUtil.isNull(container.getOptions())){
                        container.setOptions(new ArrayList<FoodOption>());
                    }
                    container.getOptions().add(option);
                }
                if(ValidatorUtil.isNull(result.get(result.size()-1).getOptions())){
                    result.get(result.size()-1).setOptions(new ArrayList<OptionContainer>());
                }
                result.get(result.size()-1).getOptions().add(container);
            }else if("combo".equals(type)){
                result.get(result.size()-1).setType(Food.TYPE_SUITE);
                OptionContainer container = new OptionContainer();
                container.setName(fields[2]);
                for (int i=3; i<fields.length; i++){
                    Food food = findByFoodId(fields[i], shopId);
                    if(ValidatorUtil.isNull(food)){
                        result.get(result.size()-1).setPass(false);
                        result.get(result.size()-1).setNoPassReason("套餐包含菜id不存在");
                        continue;
                    }
                    FoodOption option = new FoodOption();
                    option.setId(fields[i]);
                    option.setName(food.getName());
                    if(ValidatorUtil.isNull(container.getOptions())){
                        container.setOptions(new ArrayList<FoodOption>());
                    }
                    container.getOptions().add(option);
                }
                if(ValidatorUtil.isNull(result.get(result.size()-1).getOptions())){
                    result.get(result.size()-1).setCombo(new ArrayList<OptionContainer>());
                }
                result.get(result.size()-1).getCombo().add(container);
            }
        }
        return result;
    }

    private String checkImportColumnType(String rowStr){
        String[] fields = rowStr.split(",");
        if(ValidatorUtil.isNull(fields)){
            return "invalid";
        }else if (ValidatorUtil.isNotNull(fields[0])){
            return "food";
        }else if(ValidatorUtil.isNotNull(fields[1])){
            return "option";
        }else {
            return "combo";
        }
    }

    /**
     * 批量增加菜品
     * @param dataStr
     * @param shopId
     * @return
     */
    public Integer foodImportSubmit(String dataStr, ObjectId shopId){
        List<FoodImportVo> foodList = SerializationUtil.deSerializeList(dataStr, FoodImportVo.class);

        int importCount = 0;
        for (FoodImportVo food : foodList){
            if(!food.getPass()){
                continue;
            }
            try {
                String foodId = generateFoodId(food.getCateId(), shopId);
                String abc = ContentUtil.getPinYinHeadChar(food.getName()).toUpperCase();
                food.setOwner(shopId);
                food.setAbc(abc);
                food.setCreateTime(new Date());
                food.setType(0);
                food.setId(foodId);
                getMongoTemplate().insert(food, "food");
                importCount++;
            }catch (Exception e){
                continue;
            }
        }
        return importCount;
    }

    /**
     * 导出菜品csv格式
     * @param shopId
     * @param cateId
     * @return
     */
    public String foodOutput(ObjectId shopId, String cateId){
        if(ValidatorUtil.isNull(cateId)){
            throw new ServiceException("子类id不可为空");
        }
        StringBuffer buffer = new StringBuffer();
        List<Food> foodList = getAvailableFoods(shopId, cateId);
        for (Food food : foodList){
            buffer.append(food.getName()).append(",").
                    append(cateId).append(",").
                    append(food.getPrice1()).append(",");
                    if(ValidatorUtil.isNotNull(food.getPrice2()))
                        buffer.append(food.getPrice2()).append(",");
                    if(ValidatorUtil.isNotNull(food.getPrice3()))
                        buffer.append(food.getPrice3()).append(",");
                    if(ValidatorUtil.isNotNull(food.getPricem()))
                        buffer.append(food.getPricem()).append(",");
                    if(ValidatorUtil.isNotNull(food.getPrinter1()))
                        buffer.append(food.getPrinter1()).append(",");
                    if(ValidatorUtil.isNotNull(food.getPrinter2()))
                        buffer.append(food.getPrinter2()).append(",");
                    if(ValidatorUtil.isNotNull(food.getPrinter3()))
                        buffer.append(food.getPrinter3()).append(",");
                    if(ValidatorUtil.isNotNull(food.getPrinterControl()))
                        buffer.append(food.getPrinterControl()).append(",");
                    if(ValidatorUtil.isNotNull(food.getPrinterPolicy()))
                        buffer.append(food.getPrinterPolicy());
                    buffer.append("\n");
            //解析选项
            List<OptionContainer> containers = food.getOptions();
            if(ValidatorUtil.isNotNull(containers)){
                for (OptionContainer container : containers){
                    buffer.append(",").append(container.getName()).append(",");
                    List<FoodOption> options = container.getOptions();
                    if(ValidatorUtil.isNull(options)){
                        continue;
                    }
                    for (FoodOption option : options){
                        buffer.append(option.getName()).append(",");
                    }
                    buffer.append("\n");
                }
            }
            //解析子菜
            List<OptionContainer> combos = food.getCombo();
            if(ValidatorUtil.isNotNull(combos)){
                for (OptionContainer combo : combos){
                    buffer.append(",").append(",").append(combo.getName()).append(",");
                    List<FoodOption> options = combo.getOptions();
                    if(ValidatorUtil.isNull(options)){
                        continue;
                    }
                    for (FoodOption option : options){
                        buffer.append(option.getName()).append(",");
                    }
                    buffer.append("\n");
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 引用总店的菜品
     * @param foods
     * @param shopId
     */
    public void quoteFromSuperSubmit(List<Food> foods, JSONObject config, ObjectId shopId){
        String classifyType = config.getString("classifyType");
        if ("0".equals(classifyType)){
            //同步大小类
            Shop currentShop = shopService.findBy_id(shopId);
            Shop superShop = shopService.findMainShop(currentShop.getOwner());
            if (ValidatorUtil.isNotNull(superShop.getCategories())){
                List<Category> categories = superShop.getCategories();
                if (ValidatorUtil.isNotNull(currentShop.getCategories())){//查找总店没有的分类加入
                    for (Category category : currentShop.getCategories()){
                        if (ValidatorUtil.isNull(findCateByIdFromList(categories, category.getId()))){
                            categories.add(category);
                        }
                    }

                    for (Category category : categories){
                        Category oldCate = findCateByIdFromList(currentShop.getCategories(), category.getId());
                        if (ValidatorUtil.isNotNull(oldCate) && ValidatorUtil.isNotNull(oldCate.getChildren())){
                            for (Children children : oldCate.getChildren()){
                                if (ValidatorUtil.isNull(findChildByIdFromList(category.getChildren(), children.getId()))){
                                    if (category.getChildren() == null){
                                        category.setChildren(new ArrayList<Children>());
                                    }
                                    category.getChildren().add(children);
                                }
                            }
                        }
                    }
                }
                currentShop.setCategories(categories);
                shopService.updateDocument(currentShop);
            }
        }
        //保存菜品
        if (ValidatorUtil.isNotNull(foods)){
            for (Food food : foods){
                food.setRefId(food.get_id());
                food.setOwner(shopId);
                food.set_id(null);
                if ("1".equals(classifyType)){
                    String cat = config.getString("cat");
                    food.setCat(cat);
                }
                //查找是否id已存在
                Food existFood = findByFoodId(food.getId(), shopId);
                if (ValidatorUtil.isNotNull(existFood)){
                    removeBy_id(existFood.get_id());
                }
                //保存
                insert(food);
            }
        }
    }

    private Category findCateByIdFromList(List<Category> list, String id){
        for (Category category : list){
            if (category.getId().equals(id)){
                return category;
            }
        }
        return null;
    }

    private Children findChildByIdFromList(List<Children> childrens, String id){
        for (Children children : childrens){
            if (children.getId().equals(id)){
                return children;
            }
        }
        return null;
    }

    public List<Food> findExistFoodsByIds(List<Food> foods, ObjectId shopId){
        List<Food> existFoods = new ArrayList<Food>();
        for (Food food : foods){
            Food existFood = findByFoodId(food.getId(), shopId);
            if (ValidatorUtil.isNotNull(existFood)){
                existFoods.add(existFood);
            }
        }
        return existFoods;
    }

    public List<Food> findRefFoods(ObjectId shopId){
        Query query = new Query(Criteria.where("refId").exists(true));
        return getMongoTemplate().find(query, Food.class);
    }

    public List<OptionContainer> findHisFoodOptions(ObjectId shopId){
        Map<String, OptionContainer> map = new HashedMap();

        Query query = new Query(Criteria.where("owner").is(shopId).and("options").exists(true));
        List<Food> foodList = getMongoTemplate().find(query, Food.class);
        for (Food food : foodList){
            if (ValidatorUtil.isNull(food.getOptions())){
                continue;
            }
            for (OptionContainer optionContainer : food.getOptions()){
                if (ValidatorUtil.isNull(map.get(optionContainer.getName()))){
                    map.put(optionContainer.getName(), optionContainer);
                }else if (optionContainer.getOptions().size() > map.get(optionContainer.getName()).getOptions().size()){
                    map.put(optionContainer.getName(), optionContainer);
                }
            }
        }
        List listValue = new ArrayList();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            listValue.add(map.get(key));
        }
        return listValue;
    }

}
