package com.covilla.aspect;

import com.covilla.annotation.AOPLogAnnotation;
import com.covilla.common.LogOptionEnum;
import com.covilla.oplog.OpLogMakerFactory;
import com.covilla.model.mongo.log.OpLog;
import com.covilla.service.log.LogOptionService;
import com.covilla.vo.ResultMsg;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志操作切面
 * Created by qmaolong on 2017/2/10.
 */
@Aspect
@Component
public class SystemLogAspect {
    //注入Service用于把日志保存数据库
    @Resource
    private LogOptionService logService;
    //本地异常日志记录对象
    private static  final Logger logger = LoggerFactory.getLogger(SystemLogAspect. class);
    //日志对象
    private OpLog opLog = new OpLog();

    //Controller层切点
    @Pointcut("@annotation(com.covilla.annotation.AOPLogAnnotation)")
    public  void controllerAspect() {
    }

    private String description;
    private String remark;
    private LogOptionEnum option;
    private String displayTitle;
    private String displayItemFromDataStr;
    private String displayItemFromParameter;

    /**
     * 获取注入的基本参数
     * @param joinPoint
     */
    private void init(JoinPoint joinPoint){
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        description = method.getAnnotation(AOPLogAnnotation.class).description();
                        remark = method.getAnnotation(AOPLogAnnotation.class).remark();
                        option = method.getAnnotation(AOPLogAnnotation.class).option();
                        displayTitle = method.getAnnotation(AOPLogAnnotation.class).displayTitle();
                        displayItemFromDataStr = method.getAnnotation(AOPLogAnnotation.class).displayItemFromDataStr();
                        displayItemFromParameter = method.getAnnotation(AOPLogAnnotation.class).displayItemFromParameter();
                        break;
                    }
                }
            }
        }catch (Exception e){
            logger.error("操作log记录失败！！！");
        }
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("controllerAspect()")
    public  void doBefore(JoinPoint joinPoint) {
        try {
            init(joinPoint);
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            /*========控制台输出=========*/
            System.out.println("=====前置通知开始=====");
            opLog = new OpLogMakerFactory().getOpLogMakerInstant(option).makeOpLog(request, joinPoint, description, remark, displayTitle, displayItemFromDataStr, displayItemFromParameter);
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("操作:" + opLog.getDescription());
            System.out.println("操作项:" + opLog.getOperationItem());
            System.out.println("请求人:" + opLog.getOperator().getName());
            System.out.println("请求IP:" + opLog.getIp());
            System.out.println("=====前置通知结束=====");
        }  catch (Exception e) {
            //记录本地异常日志
            logger.error("==前置通知异常==");
            logger.error("异常信息:{}", e.getMessage());
        }
    }

    /**
     *后置通知
     * @param joinPoint
     * @param rtv
     * @throws Throwable
     */
    @AfterReturning(value="controllerAspect()", returning="rtv")
    public void afterReturning(JoinPoint joinPoint, Object rtv) throws Throwable{
        try {
            if (rtv instanceof ResultMsg){//返回类型为ResultMsg的log
                ResultMsg resultMsg = (ResultMsg)rtv;
                if (ResultMsg.SUCCESS.equals(resultMsg.getResultCode())){
                    opLog.setSuccess(true);
                }else {
                    opLog.setFailReason(resultMsg.getErrCodeDesc());
                    opLog.setSuccess(false);
                }
                logService.addLog(opLog);
            }else {
                opLog.setSuccess(true);
                logService.addLog(opLog);
            }
        }catch (Exception e){
            logger.error("操作log记录失败！！！");
        }
    }

    /**
     * 异常通知
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "controllerAspect()", throwing = "e")
    public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.info("service error");
    }
}
