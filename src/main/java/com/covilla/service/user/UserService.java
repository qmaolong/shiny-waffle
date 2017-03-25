package com.covilla.service.user;

import com.covilla.common.Config;
import com.covilla.common.Constant;
import com.covilla.common.OperationEnum;
import com.covilla.common.RoleEnum;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.UserMongoDao;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.util.*;
import com.covilla.util.wechat.entity.user.UserWeiXin;
import com.covilla.util.wechat.util.MD5;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/8/24.
 */
@Service
@Transactional
public class UserService extends BaseMongoService<User> {
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserMongoDao userMongoDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    protected BaseMongoDao<User> getBaseMongoDao(){
        return userMongoDao;
    }

    /**
     * 以openId获取用户
     * @param openId
     * @param isWxUser 是否为微信用户
     * @return
     */
    public User findByOpenId(String openId, boolean isWxUser){
        Criteria criteria = Criteria.where("openId").is(openId).and("dataStatus").ne(Constant.DATA_STATUS_INVALID);
        if(isWxUser){
            criteria.and("role").is(RoleEnum.customer.getCode());
        }else {
            criteria.and("role").ne(RoleEnum.customer.getCode());
        }
        List<User> userList = getMongoTemplate().find(new Query(criteria), User.class);
        if(ValidatorUtil.isNotNull(userList)){
            return  userList.get(0);
        }
        return null;
    }

    public User findByName(String name){
        Query query = new Query(Criteria.where("name").is(name).and("dataStatus").ne(Constant.DATA_STATUS_INVALID));
        List<User> userList = getMongoTemplate().find(query, User.class);
        if(ValidatorUtil.isNotNull(userList)){
            return  userList.get(0);
        }
        return null;
    }

    public User findByActiveCode(String code){
        List<User> userList = findByProperty("activeCode", code);
        if(ValidatorUtil.isNotNull(userList)){
            return  userList.get(0);
        }
        return null;
    }

    public User bindUser(UserWeiXin userWeiXin, User user){
        if(ValidatorUtil.isNull(user)||ValidatorUtil.isNull(userWeiXin)){
            throw new ServiceException("绑定失败~");
        }
        user.setOpenId(userWeiXin.getOpenid());
        user.setNickName(userWeiXin.getNickname());
        user.setHeadImgUrl(userWeiXin.getHeadimgurl());
        user.setProvince(userWeiXin.getProvince());
        user.setCity(userWeiXin.getCity());
        user.setSex(userWeiXin.getSex());
        updateDocument(user);
        return user;
    }

    public void editUser(String dataStr, String oper, RoleEnum role) throws Exception{
        User operator = SpringSecurityUtil.getCurrentUser();
        if(OperationEnum.delete.getCode().equals(oper)){
            List<User> users = SerializationUtil.deSerializeList(dataStr, User.class);
            List<ObjectId> ids = new ArrayList<ObjectId>();
            for(User user1 : users){
                ids.add(new ObjectId(user1.getObjectIdStr()));
            }
            deleteByValues(ids, "_id");
        }else {
            User user = SerializationUtil.deSerializeObject(dataStr, User.class);
            if(RoleEnum.manager.getCode().equals(role.getCode()) && ValidatorUtil.isNotNull(user.getManageShopStr())){
                List<ObjectId> ids = new ArrayList<ObjectId>();
                String[] idStrs = user.getManageShopStr().split(",");
                for(String idStr : idStrs){
                    ids.add(new ObjectId(idStr));
                }
                user.setManageShops(ids);
            }
            if(OperationEnum.add.getCode().equals(oper)){
                User existUser = findByName(user.getName());
                if(ValidatorUtil.isNotNull(existUser)){
                    throw new ServiceException("用户名已存在");
                }
                user.setPassword(generatePassword(user.getPassword()));
                user.setCreator(operator.get_id());
                user.setRole(role.getCode());
                user.setCreateTime(new Date());
                user.setId(generateDocumentId("user"));
                insert(user);
            }else if(OperationEnum.edit.getCode().equals(oper)){
                //编辑后的密码与原密码不同，重新设置
                User existUser = findByName(user.getName());
                if(ValidatorUtil.isNotNull(existUser)&&!user.getPassword().equals(existUser.getPassword())){
                    user.setPassword(generatePassword(user.getPassword()));
                }
                updateDocument(user);
            }
        }
    }

    /**
     * 获取门店管理员列表
     * @param creator
     * @return
     */
    public List<User> getShopManagersByCreator(ObjectId creator){
        return findByMap(MiscUtils.toMap("role", RoleEnum.manager.getCode(), "creator", creator, "dataStatus", Constant.DATA_STATUS_VALID));
    }

    /**
     * 获取系统管理员列表
     * @return
     */
    public List<User> getSysManagersByCreator(){
        return findByMap(MiscUtils.toMap("role", RoleEnum.admin.getCode(), "dataStatus", Constant.DATA_STATUS_VALID));
    }

    /*
    获取商家列表
     */
    public List<User> getMerchants(){
        return findByMap(MiscUtils.toMap("role", RoleEnum.owner.getCode(), "dataStatus", Constant.DATA_STATUS_VALID));
    }

    public Shop defaultShop(User user){
        List<Shop> shopList = shopService.findShopsByUser(user);
        if (ValidatorUtil.isNull(shopList)){
            return null;
        }
        for (Shop shop : shopList){
            if (ValidatorUtil.isNotNull(shop.getSuperFlag()) && shop.getSuperFlag()){
                return shop;
            }
        }
        return shopList.get(0);
    }

    /**
     * 用户注册
     * @param user
     */
    public User regist(User user) throws Exception{
        if(ValidatorUtil.isNull(user.getTel()) || ValidatorUtil.isNull(user.getName()) || ValidatorUtil.isNull(user.getPassword())){
            throw new ServiceException("信息缺失，注册失败~");
        }
        List<User> existName = findByProperty("name", user.getName());
        if(ValidatorUtil.isNotNull(existName)){
            throw new ServiceException("该用户名已存在");
        }else if(!user.getName().matches("^[a-zA-z]\\w{5,15}$")){
            throw new ServiceException("用户名格式不正确");
        }
        List<User> existTel = findByProperty("tel", user.getTel());
        if (ValidatorUtil.isNotNull(existTel)){
            throw new ServiceException("该手机号已存在");
        }
        if(ValidatorUtil.isNotNull(user.getBindKey())){//用激活码绑定
            User existUser = findByActiveCode(user.getBindKey());
            if(ValidatorUtil.isNull(user) || ValidatorUtil.isNull(user.getCodeExpireTime()) || user.getCodeExpireTime().before(new Date())){
                throw new ServiceException("无效激活码~");
            }
            existUser.setOpenId(user.getOpenId());
            existUser.setNickName(user.getNickName());
            existUser.setHeadImgUrl(user.getHeadImgUrl());
            existUser.setProvince(user.getProvince());
            existUser.setCity(user.getCity());
            existUser.setSex(user.getSex());
            existUser.setName(user.getName());
            existUser.setPassword(generatePassword(user.getPassword()));
            updateDocument(existUser);
            return existUser;
        }else {//新建用户
            user.setId(generateDocumentId("user"));
            user.setPassword(generatePassword(user.getPassword()));
            user.setCreateTime(new Date());
            user.setRole(RoleEnum.owner.getCode());
            insert(user);
            return user;
        }
    }

    public User checkPassword(String password, String name) throws Exception{
        User user = findByName(name);
        if(ValidatorUtil.isNotNull(user)){

            //旧有用户密码规则（少部分用户）
            String key = user.getPassword().substring(user.getPassword().indexOf("$") + 1);
            if (user.getPassword().indexOf("$") >= 0 && user.getPassword().equals(MD5.byteArrayToHexString(MySecurity.HmacSHA1Encrypt(password, key)) + "$" + key)){
                return user;
            }
            //旧有用户密码规则（少部分用户）
            if(user.getPassword().indexOf("$") < 0 && MD5Utils.getSHA1(password + "nowyouseeme").equals(user.getPassword())){
                return user;
            }
            //新密码规则，采用Spring Security加密
            if (bCryptPasswordEncoder.matches(password, user.getPassword())){
                return user;
            }
        }
        return null;
    }

    /**
     * 获取唯一超级管理员的userId
     * @return
     */
    public ObjectId findSuperAdminUserId(){
        return Config.SUPER_ADMIN_ID;
    }

    public User changePassword(String newPassword, User user) throws Exception{
        String password = generatePassword(newPassword);
        updateByMap(MiscUtils.toMap("_id", user.get_id()), MiscUtils.toMap("password", password));
        user.setPassword(password);
        return user;
    }

    public void unBindWeixin(ObjectId _id){
        User user = findBy_id(_id);
        if(ValidatorUtil.isNull(user) || ValidatorUtil.isNull(user.getOpenId())){
            throw new ServiceException("解绑失败~");
        }
        Query query = new Query(Criteria.where("_id").is(_id));
        Update update = new Update().set("openId", null).set("nickName", null).set("headImgUrl", null).set("province", null).set("city", null);
        getMongoTemplate().updateFirst(query, update, User.class);
    }

    /**
     * 密码加密
     * @param oriContent
     * @return
     * @throws Exception
     */
    public String generatePassword(String oriContent) throws Exception{
        return bCryptPasswordEncoder.encode(oriContent);
    }

    public static void main(String[] args){
        String randomStr = RandomUtil.genRandomString(6);
        try {
            String test = MD5.byteArrayToHexString(MySecurity.HmacSHA1Encrypt("123456", randomStr)) + "$" + randomStr;
            System.out.println(test);
            System.out.println(test.substring(test.indexOf("$") + 1));
        }catch (Exception e){
            //
        }
    }
}
