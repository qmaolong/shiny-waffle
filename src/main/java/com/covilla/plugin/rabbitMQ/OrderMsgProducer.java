package com.covilla.plugin.rabbitMQ;

import com.alibaba.fastjson.JSONObject;
import com.covilla.weixin.OrderMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by qmaolong on 2016/9/3.
 */
@Component
public class OrderMsgProducer {
    private Logger logger = LoggerFactory.getLogger(OrderMsgProducer.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static String ORDER_COMPLETE_KEY = "result";

    public void sendOrderMessage(OrderMessage orderMessage) {
        logger.info("to send message:{}", JSONObject.toJSON(orderMessage));
        rabbitTemplate.convertAndSend(ORDER_COMPLETE_KEY, orderMessage);
    }

    public void sendMessage(Object message){
        logger.info("to send message:{}",message);
        rabbitTemplate.convertAndSend(message);
    }

    public void sendMessage(String bindKey, Object message){
        logger.info("to send message:{}",message);
        rabbitTemplate.convertAndSend(bindKey, message);
    }
}