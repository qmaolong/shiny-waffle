package com.covilla.oplog;

import com.covilla.model.mongo.log.OpLog;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by qmaolong on 2017/2/12.
 */
public interface IOpLogMaker {
    OpLog makeOpLog(HttpServletRequest request, JoinPoint joinPoint, String description, String remark, String displayTitle, String displayItemFromDataStr, String displayItemFormParameter);
}
