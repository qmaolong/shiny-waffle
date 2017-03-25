package com.covilla.controller.api.shop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.covilla.common.Constant;
import com.covilla.model.mongo.card.Card;
import com.covilla.service.ServiceException;
import com.covilla.service.card.CardService;
import com.covilla.util.MiscUtils;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.BaseApiResultMsg;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by qmaolong on 2017/1/13.
 */
@Controller
@RequestMapping("/api/shop")
public class ShopApiController {
    Logger logger = LoggerFactory.getLogger(ShopApiController.class);
    @Autowired
    private CardService cardService;

    @RequestMapping("updateCard")
    @ResponseBody
    public BaseApiResultMsg batchUpdateCard(HttpServletRequest request){
        ObjectId shopId = (ObjectId)request.getSession().getAttribute(Constant.SHOPID_SESSION);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine())!=null){
                sb.append(line);
            }
            JSONArray cards = JSONArray.parseArray(new String(sb.toString().getBytes(), "UTF-8"));
            //验证权限
            for (int i=0; i<cards.size(); i++){
                JSONObject object = cards.getJSONObject(i);
                String id = object.getString("id");
                if (ValidatorUtil.isNull(id) || !cardService.authCheck(id, shopId.toString())){
                    return BaseApiResultMsg.buildErrorMsg("-1", "没有权限操作卡‘" + id + "’");
                }
                String mediumKey = object.getString("mediumKey");
                if (ValidatorUtil.isNotNull(mediumKey)){
                    List<Card> existCards = cardService.findByMap(MiscUtils.toMap("shopId", shopId, "mediumKey", mediumKey));
                    if (ValidatorUtil.isNotNull(existCards) && !existCards.get(0).get_id().toString().equals(id)){
                        return BaseApiResultMsg.buildErrorMsg("-1", mediumKey + "已存在");
                    }else if (mediumKey.startsWith("13")||mediumKey.startsWith("28")){
                        return BaseApiResultMsg.buildErrorMsg("-1", "mediumKey不能以‘13’或‘28’开头");
                    }
                }
            }
            //修改卡信息
            for (int i=0; i<cards.size(); i++){
                JSONObject object = cards.getJSONObject(i);
                String id = object.getString("id");
                String mediumKey = object.getString("mediumKey");
                Card card = cardService.findByCardId(id);
                if (ValidatorUtil.isNull(card)){
                    continue;
                }
                Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
                Update update = new Update().set("mediumKey", mediumKey);
                cardService.getMongoTemplate().updateFirst(query, update, Card.class);
            }
        }catch (ServiceException se){
            return BaseApiResultMsg.buildErrorMsg("-1", se.getMessage());
        }catch (Exception e){
            logger.error(e.getMessage());
            return BaseApiResultMsg.buildErrorMsg("-1", "系统错误");
        }
        return new BaseApiResultMsg();
    }
}
