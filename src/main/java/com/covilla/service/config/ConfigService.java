package com.covilla.service.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.covilla.common.TicketParam;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ConfigMongoDao;
import com.covilla.model.mongo.config.ConfigModule;
import com.covilla.model.mongo.printer.TicketForm;
import com.covilla.model.mongo.printer.TicketItemForm;
import com.covilla.model.mongo.setting.Payment;
import com.covilla.service.BaseMongoService;
import com.covilla.util.DateUtil;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.TicketFormConfigVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by qmaolong on 2016/10/13.
 */
@Service
@Transactional
public class ConfigService extends BaseMongoService<ConfigModule> {
    @Autowired
    private ConfigMongoDao configDao;
    protected BaseMongoDao<ConfigModule> getBaseMongoDao(){
        return configDao;
    }

    public TicketForm getTicketFormConfig(Integer category){
        TicketFormConfigVo config = configDao.getTicketFormConfig();
        if(ValidatorUtil.isNull(config)){
            return null;
        }
        if(TicketParam.TicketCategory.Kitchen.code.equals(category)){
            return config.getKitchenForm();
        }else if(TicketParam.TicketCategory.Control.code.equals(category)){
            return config.getControlForm();
        }else if(TicketParam.TicketCategory.Ticket.code.equals(category)){
            return config.getTicketForm();
        }
        return null;
    }

    public List<TicketItemForm> getTicketItems(Integer category, Integer zone){
        TicketForm ticketForm = getTicketFormConfig(category);
        if(ValidatorUtil.isNull(ticketForm)){
            return null;
        }else if(TicketParam.TicketZone.Head.code.equals(zone)){
            return ticketForm.getHeader();
        }else if(TicketParam.TicketZone.Content.code.equals(zone)){
            return ticketForm.getContent();
        }else if(TicketParam.TicketZone.Tail.code.equals(zone)){
            return ticketForm.getTail();
        }
        return null;
    }

    public List<Payment> getAllPayment(){
        Query query = new Query(Criteria.where("name").is("payment"));
        List<ConfigModule> result = getMongoTemplate().find(query, ConfigModule.class, "config");
        if(ValidatorUtil.isNotNull(result)){
            String jsonString = JSONArray.toJSONString(result.get(0).getValue());

            List<Payment> payments = JSON.parseArray(jsonString, Payment.class);
            return payments;
        }
        return null;
    }

    public Integer getDayBusinessNo(){
        Update update = new Update();
        Criteria criteria =  Criteria.where("name").is("dayBusinessNo");
        String day = DateUtil.formateDateToStr(new Date(), "yyyyMMdd");
        ConfigModule result = getMongoTemplate().findAndModify(new Query(criteria), update.inc("value." + day, 1), new FindAndModifyOptions().returnNew(true), ConfigModule.class);
        if(ValidatorUtil.isNull(result)){
            ConfigModule configModule = new ConfigModule();
            configModule.setName("dayBusinessNo");
            JSONObject object = new JSONObject();
            object.put(day, 1);
            configModule.setValue(object);
            getMongoTemplate().insert(configModule);
            return 1;
        }
        JSONObject object = JSON.parseObject(JSON.toJSONString(result.getValue()));
        return object.getInteger(day);
    }

    public Object findConfigByName(String name){
        List<ConfigModule> configModule = findByProperty("name", name);
        if (ValidatorUtil.isNotNull(configModule)){
            return configModule.get(0).getValue();
        }
        return null;
    }
}
