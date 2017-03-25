package com.covilla.plugin.rabbitMQ;

import com.covilla.common.OrderTypeEnum;
import com.covilla.model.mongo.order.Order;
import com.covilla.service.ServiceException;
import com.covilla.service.order.OrderService;
import com.covilla.util.ValidatorUtil;
import com.covilla.weixin.OrderMessage;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by qmaolong on 2016/9/3.
 */
@Service
public class OrderMsgConsumer implements MessageListener {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMsgProducer orderMsgProducer;

    public void onMessage(Message message) {
        logger.info("收到订单消息" + message.toString());
        try{
            String id = new String(message.getBody());
            Order order = orderService.findBy_id(new ObjectId(id));

            if (ValidatorUtil.isNotNull(order.getDealFlag())){
                logger.info("重复收到消息");
                return;
            }
            OrderMessage orderMessage = null;
            if(OrderTypeEnum.rollbackOrder.getCode().equals(order.getOrderType())){//撤销
                orderMessage = orderService.rollbackOrder(order);
            }else if(OrderTypeEnum.fastOrder.getCode().equals(order.getOrderType())
                    || OrderTypeEnum.takeOut.getCode().equals(order.getOrderType())
                    || OrderTypeEnum.deskOrder.getCode().equals(order.getOrderType())){//堂食、外卖、快餐
                orderMessage = orderService.completeOrder(order);
            }
            //标记订单为已处理
            order.setDealFlag(new Date());
            orderService.updateDocument(order);
            //推送消息
            orderMsgProducer.sendOrderMessage(orderMessage);
        }catch (ServiceException se){
            logger.error("message deal failed!:" + se.getMessage());
        }catch (Exception e){
            logger.error("message deal failed!");
        }

    }
}