package com.covilla.oplog.maker;

import com.covilla.common.Constant;
import com.covilla.common.OperationEnum;
import com.covilla.oplog.IOpLogMaker;
import com.covilla.model.mongo.log.OpLog;
import com.covilla.model.mongo.user.User;
import com.covilla.util.CusAccessObjectUtil;
import com.covilla.util.ValidatorUtil;
import org.aspectj.lang.JoinPoint;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 业务log生成器
 * Created by qmaolong on 2017/2/13.
 */
public class BusinessLogMaker implements IOpLogMaker {

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
        String oprationItem = "";

        if (ValidatorUtil.isNotNull(displayItemFromParameter)){//从请求参数中获取字段
            String[] names = displayItemFromParameter.split(",");
            String[] titles = displayTitle.split(",");
            for (int i=0; i<names.length; i++){
                if (titles.length == names.length){
                    oprationItem += titles[i] + "：";
                }
                oprationItem += request.getParameter(names[i]);
                if (i<names.length-1){
                    oprationItem += "，";
                }
            }
        }
        OpLog opLog = new OpLog();
        opLog.setDescription(description);
        opLog.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
        opLog.setIp(ip);
        opLog.setOperator(user);
        opLog.setTime(new Date());
        opLog.setShopId(shopId);
        opLog.setRemark(remark);
        opLog.setOperationItem(oprationItem);
        return opLog;
    }
}
