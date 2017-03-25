package com.covilla.plugin.rabbitMQ;

import com.alibaba.fastjson.JSONObject;
import com.covilla.sms.VerificationCodeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Random;

/**
 * 短信推送类
 * Created by qmaolong on 2017/3/10.
 */
@Component
public class SmsProducer {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private static String queueName = "covilla-sms";
    private Random random = new Random();

    @Bean
    Queue smsQueue() {
        return new Queue(queueName);
    }

    public VerificationCodeMessage sendVerifyCode(String tel, String action, String shopId) {
        String code = String.format("%1$06d", random.nextInt(1000000));
        logger.info(String.format("短信验证码为：%s", code));
        VerificationCodeMessage message = new VerificationCodeMessage();
        message.setTel(tel);
        message.setAction(action);
        message.setShopId(shopId);
        message.setCode(code);
        message.setTime(new Date());
        logger.info("推送信息：" + JSONObject.toJSONString(message));
        rabbitTemplate.convertAndSend("", queueName, message);
        return message;
    }
}
