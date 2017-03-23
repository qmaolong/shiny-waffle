package com.covilla.oplog.maker;

import com.alibaba.fastjson.JSONObject;
import com.covilla.common.Constant;
import com.covilla.common.OperationEnum;
import com.covilla.oplog.IOpLogMaker;
import com.covilla.model.mongo.card.CardBatch;
import com.covilla.model.mongo.log.OpLog;
import com.covilla.model.mongo.user.User;
import com.covilla.service.card.CardService;
import com.covilla.util.CusAccessObjectUtil;
import com.covilla.util.ValidatorUtil;
import org.aspectj.lang.JoinPoint;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 卡券生成log生成器
 * Created by qmaolong on 2017/2/13.
 */
public class GenerateCardOpLogMaker implements IOpLogMaker {
    public OpLog makeOpLog(HttpServletRequest request, JoinPoint joinPoint, String description, String remark, String displayTitle, String displayItemFromDataStr, String displayItemFromParameter){
        HttpSession session = request.getSession();
        //读取session中的用户
        User user = (User) session.getAttribute(Constant.USER_SESSION);
        //读取session中的门店id
        ObjectId shopId = (ObjectId) session.getAttribute(Constant.SHOPID_SESSION);
        //读取请求参数
        Object[] args = joinPoint.getArgs();
        //读取请求的IP
        String ip = CusAccessObjectUtil.getIpAddress(request);
        //设置操作名
        String oper = request.getParameter("oper");
        if (ValidatorUtil.isNotNull(oper)){
            description += "-" + OperationEnum.getNameByCode(oper);
        }
        String dataStr = request.getParameter("dataStr");
        String operationItem = "";
        if (ValidatorUtil.isNotNull(displayItemFromDataStr) && ValidatorUtil.isNotNull(dataStr)){//从dataStr中获取字段
            CardBatch cardBatch = JSONObject.parseObject(dataStr, CardBatch.class);
            operationItem = new CardService().countWillBeMade(cardBatch) + "张";
        }
        OpLog opLog = new OpLog();
        opLog.setDescription(description);
        opLog.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
        opLog.setIp(ip);
        opLog.setOperator(user);
        opLog.setTime(new Date());
        opLog.setShopId(shopId);
        opLog.setRemark(remark);
        opLog.setOperationItem(operationItem);
        return opLog;
    }
}
