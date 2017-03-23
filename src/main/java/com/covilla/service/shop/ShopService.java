package com.covilla.service.shop;

import com.covilla.common.*;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.shop.AuthKey;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.Cashier;
import com.covilla.model.mongo.user.User;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.service.authKey.AuthKeyService;
import com.covilla.service.user.UserService;
import com.covilla.util.*;
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
 * Created by LongLongAgo on 2016/8/24.
 */
@Service
@Transactional
public class ShopService extends BaseMongoService<Shop> {
    private Logger logger = LoggerFactory.getLogger(ShopService.class);
    @Autowired
    private ShopMongoDao shopMongoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthKeyService authKeyService;

    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    public List<Shop> findByOwner(ObjectId userId){
        Criteria criteria =  Criteria.where("owner").is(userId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID);
        Query query = new Query().addCriteria(criteria);
        return shopMongoDao.getMongoTemplate().find(query, Shop.class);
    }

    /**
     * 查找shop
     * @param shopId
     * @return
     */
    public Shop findAvailableShop(ObjectId shopId){
        Criteria criteria =  Criteria.where("_id").is(shopId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID);
        Query query = new Query().addCriteria(criteria);
        List<Shop> shopList = getMongoTemplate().find(query, Shop.class);
        if(ValidatorUtil.isNotNull(shopList)){
            return shopList.get(0);
        }
        return null;
    }

    public List<Shop> findByIds(List<ObjectId> ids){
        Query query = new Query(Criteria.where("_id").in(ids).and("dataStatus").ne(Constant.DATA_STATUS_INVALID));
        return shopMongoDao.getMongoTemplate().find(query, Shop.class);
    }

    public List<Shop> findShopsByUser(User user){
        if(RoleEnum.owner.getCode().equals(user.getRole()) || RoleEnum.superAdmin.getCode().equals(user.getRole())){
            return findByOwner(user.get_id());
        }else if(RoleEnum.manager.getCode().equals(user.getRole())){
            return findByIds(user.getManageShops());
        }
        return null;
    }

    /**
     * 系统管理员获取所有非模板门店
     * @return
     */
    public List<Shop> findAllShops(){
        Query query = new Query(Criteria.where("dataStatus").ne(Constant.DATA_STATUS_INVALID).and("owner").ne(Config.SUPER_ADMIN_ID));
        return getMongoTemplate().find(query, Shop.class);
    }

    /**
     * 用户自建门店
     * @param shopData
     * @param user
     * @param shopId
     */
    public void addShopByUser(Shop shopData, User user, ObjectId shopId){
        Shop shop = findModuleShop();
        shop.set_id(null);
        shop.setOwner(user.get_id());
        shop.setId(generateDocumentId("shop"));
        shop.setSuperShop(shopId);
        shop.setName(shopData.getName());
        shop.setOwnerName(shopData.getOwnerName());
        shop.setPhone(shopData.getPhone());
        shop.setSuperFlag(false);
        shop.setSuperShop(shopId);
        shop.setExpired(shopData.getExpired());
        shop.setAddress(shopData.getAddress());
        //新建试用key
        AuthKey authKey = authKeyService.genTestKey();
        List<String> keys = new ArrayList<String>();
        keys.add(authKey.get_id());
        shop.setAuthKeys(keys);
        //生成试用期一个月的收银台
        Cashier cashier = new Cashier();
        cashier.setName("*");
        cashier.setMid("*");
        cashier.setExpired(shop.getExpired());
        cashier.setId(1);
        shop.setCashiers(new ArrayList<Cashier>());
        shop.getCashiers().add(cashier);

        //生成icKey
        byte randomBytes[]=new byte[20];
        Random rand=new Random();
        rand.nextBytes(randomBytes);//生成随机字节
        shop.setIcKey(randomBytes);

        insert(shop);
        authKeyService.takenKey(authKey.get_id());
    }

    public Shop updateShopInfo(Shop shop){
        Update update = Update.update("name", shop.getName()).set("address", shop.getAddress()).set("ownerName", shop.getOwnerName()).set("phone", shop.getPhone()).set("address", shop.getAddress()).set("expired", shop.getExpired());
        Criteria criteria = Criteria.where("_id").is(new ObjectId(shop.getObjectIdStr()));
        shopMongoDao.getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        updateModifyTime("shop", shop.get_id(), ModifyBlockEnum.shop.getKey());
        return shop;
    }

    public void deleteShopInfo(List<Shop> shops){
        List<Integer> ids = new ArrayList<Integer>();
        for(Shop shop : shops){
            ids.add(shop.getId());
        }
        deleteByValues(ids, "id");
    }

    public Shop updateOwner(Shop shop){
        Update update = Update.update("owner", shop.getOwner());
        Criteria criteria = Criteria.where("id").is(shop.getId());
        shopMongoDao.getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        return shop;
    }

    /*public Shop findUnBindShop(String authKey){
        Criteria criteria = Criteria.where("authKey").is(authKey).andOperator(Criteria.where("owner").exists(false));
        List<Shop> shopList = shopMongoDao.getMongoTemplate().find(new Query(criteria), Shop.class);
        if(ValidatorUtil.isNull(shopList)){
            return null;
        }
        return shopList.get(0);
    }*/

    public Shop findMainShop(ObjectId owner){
        Criteria criteria = Criteria.where("owner").is(owner).and("superFlag").is(true).and("dataStatus").ne(Constant.DATA_STATUS_INVALID);
        List<Shop> shopList = shopMongoDao.getMongoTemplate().find(new Query(criteria), Shop.class);
        if(ValidatorUtil.isNull(shopList)){
            return null;
        }
        return shopList.get(0);
    }

    /**
     * 系统管理员编辑门店
     * @param dataStr
     * @param oper
     */
    public void editShopBySys(String dataStr, String oper, String creator){
        if(OperationEnum.add.getCode().equals(oper)){
            Shop shopData = SerializationUtil.deSerializeObject(dataStr, Shop.class);
            Integer ownerId = ContentUtil.decodeId(shopData.getOwnerId());

            User merchant = userService.findById(ownerId);
            if(ValidatorUtil.isNull(merchant) || merchant.getDataStatus().equals(1) || !RoleEnum.owner.getCode().equals(merchant.getRole())){
                throw new ServiceException("商户ID无效~");
            }
            Shop shop = findModuleShop();
            shop.setOwner(merchant.get_id());
            shop.setId(generateDocumentId("shop"));
            shop.setCreateTime(new Date());
            shop.setCreator(creator);
            shop.set_id(null);
            shop.setName(shopData.getName());
            shop.setOwnerName(shopData.getOwnerName());
            shop.setPhone(shopData.getPhone());
            shop.setSuperFlag(shopData.getSuperFlag());
            shop.setExpired(shopData.getExpired());
            shop.setAddress(shopData.getAddress());
            if(!shop.getSuperFlag()){//新建分店
                Shop mainShop = findMainShop(merchant.get_id());
                if(ValidatorUtil.isNull(mainShop)){
                    throw new ServiceException("该商户尚未新建总店~");
                }
                shop.setSuperShop(mainShop.get_id());
            }else if(ValidatorUtil.isNotNull(shop.getSuperFlag())&&shop.getSuperFlag()){
                Long count = shopMongoDao.countByOwnerAndType(merchant.get_id(), true);
                if(count > 0){
                    throw new ServiceException("该商户已存在总店类型门店~");
                }
            }

            //生成试用期一个月的收银台
            Cashier cashier = new Cashier();
            cashier.setName("*");
            cashier.setMid("*");
            cashier.setExpired(shop.getExpired());
            cashier.setId(1);
            shop.setCashiers(new ArrayList<Cashier>());
            shop.getCashiers().add(cashier);

            //新建试用key
            AuthKey authKey = authKeyService.genTestKey();
            List<String> keys = new ArrayList<String>();
            keys.add(authKey.get_id());
            shop.setAuthKeys(keys);

            //生成icKey
            byte randomBytes[]=new byte[20];
            Random rand=new Random();
            rand.nextBytes(randomBytes);//生成随机字节
            shop.setIcKey(randomBytes);

            insert(shop);
            if(shop.getSuperFlag()){
                bindMainShop(merchant);//绑定商家总店
            }
        }else if(OperationEnum.edit.getCode().equals(oper)){
            Shop shop = SerializationUtil.deSerializeObject(dataStr, Shop.class);
            updateShopInfo(shop);
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<Shop> shopList = SerializationUtil.deSerializeList(dataStr, Shop.class);
            deleteShopInfo(shopList);
        }else {
            throw new ServiceException("无效操作类型~");
        }
    }

    /**
     * 创建试用门店
     * @param shopData
     * @param user
     */
    public void createTryOutShop(Shop shopData, User user){
        //新增总店
        Shop shop = findModuleShop();

        shop.setName(shopData.getName());
        shop.setOwnerName(shopData.getOwnerName());
        shop.setPhone(shopData.getPhone());
        shop.setAddress(shopData.getAddress());
        shop.set_id(null);

        shop.setOwner(user.get_id());
        shop.setId(generateDocumentId("shop"));
        shop.setCreateTime(new Date());
        shop.setCreator(user.get_id().toString());
        shop.setSuperFlag(true);
        shop.setExpired(DateUtil.addDay(new Date(), 30));

        //新建试用key
        AuthKey authKey = authKeyService.genTestKey();
        List<String> keys = new ArrayList<String>();
        keys.add(authKey.get_id());
        shop.setAuthKeys(keys);

        //生成试用期一个月的收银台
        Cashier cashier = new Cashier();
        cashier.setName("*");
        cashier.setMid("*");
        cashier.setExpired(shop.getExpired());
        cashier.setId(1);
        shop.setCashiers(new ArrayList<Cashier>());
        shop.getCashiers().add(cashier);

        //生成icKey
        byte randomBytes[]=new byte[20];
        Random rand=new Random();
        rand.nextBytes(randomBytes);//生成随机字节
        shop.setIcKey(randomBytes);

        authKeyService.takenKey(authKey.get_id());
        insert(shop);
    }

    /**
     * 绑定商家总店
     * @param user
     */
    public void bindMainShop(User user){
        Shop mainShop = findMainShop(user.get_id());
        if(ValidatorUtil.isNull(mainShop)){
            return;
        }
        Map<String, Object> queryMap = MiscUtils.toMap("id", user.get_id());
        Map<String, Object> updateMap = MiscUtils.toMap("mainShop", mainShop.get_id());
        userService.updateByMap(queryMap, updateMap);
    }

    /**
     * 获取系统模板门店
     * @return
     */
    public Shop findModuleShop(){
        return findBy_id(Config.MODOULE_SHOP_ID);
    }

    public Shop findDataShop(){
        return findBy_id(Config.DATA_SHOP_ID);
    }

    public List<ObjectId> findIdsByUser(User user){
        if(RoleEnum.owner.getCode().equals(user.getRole())){
            return shopMongoDao.findIdsByOwner(user.get_id());
        }else {
            return user.getManageShops();
        }
    }

    public Shop findByEncodeId(String encodeId){
        Integer id = null;
        try {
            id = ContentUtil.decodeId(encodeId);
        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }
        Shop shop = findById(id);
        if (ValidatorUtil.isNotNull(shop) && (ValidatorUtil.isNull(shop.getDataStatus()) || !shop.getDataStatus().equals(1))){
            return shop;
        }
        return null;
    }

    public Shop findMainShopByShopId(ObjectId shopId){
        Shop shop = findBy_id(shopId);
        Shop superShop = findMainShop(shop.getOwner());
        return superShop;
    }
}
