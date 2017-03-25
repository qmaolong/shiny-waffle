package com.covilla.service.authKey;

import com.covilla.repository.mongodb.AuthKeyMongoDao;
import com.covilla.model.mongo.shop.AuthKey;
import com.covilla.service.ServiceException;
import com.covilla.util.MySecurity;
import com.covilla.util.RandomUtil;
import com.covilla.util.ValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by qmaolong on 2016/10/17.
 */
@Service
@Transactional
public class AuthKeyService {
    @Autowired
    private AuthKeyMongoDao authKeyMongoDao;

    public AuthKey findOneAvailableKey(){
        List<AuthKey> keys = authKeyMongoDao.findAvailableKeys();
        if(ValidatorUtil.isNotNull(keys)){
            return keys.get(0);
        }
        return null;
    }

    public void takenKey(String key){
        authKeyMongoDao.updateState(key, 1);
    }

    public AuthKey genTestKey(){
        AuthKey  authKey = new AuthKey();
        authKey.set_id("TEST-" + UUID.randomUUID().toString().toUpperCase().replaceAll("-", ""));
        List<String> keys = new ArrayList<String>();
        keys.add(RandomUtil.genRandomString(20));
        authKey.setKeys(keys);
        authKey.setState(0);
        authKeyMongoDao.insert(authKey);
        return authKey;
    }

    public String encodeAuthKey(String key) throws Exception{
        AuthKey authKey = authKeyMongoDao.findById(key);
        if(ValidatorUtil.isNull(authKey)){
            throw new ServiceException("获取失败~");
        }
        StringBuffer buffer = new StringBuffer("KeySN=" + key).append("\n").append("Algorithm=HMACSHA1");
        for (String temp: authKey.getKeys()){
            buffer.append("\n").append("Key" + (authKey.getKeys().indexOf(temp)+1) + "=" +temp);
        }
        byte[] keySpc = "l5NH8sc4$5f\\B+[2^6.Lw{NFA^E~y(B}".getBytes("UTF-8");
        byte[] iv = "^uR[A(|(D8Rhg\\'H".getBytes("UTF-8");
        byte[] sign = MySecurity.AES256Encrypt(buffer.toString(), "Rijndael", keySpc, iv);
        String signStr = new BASE64Encoder().encode(sign);
        return signStr;
    }
}
