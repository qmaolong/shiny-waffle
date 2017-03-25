package com.covilla.controller.bk.setting;

import com.covilla.common.Constant;
import com.covilla.model.mongo.log.OpLog;
import com.covilla.service.log.LogOptionService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by qmaolong on 2017/2/13.
 */
@Controller
@RequestMapping("merchant/admin/setting")
public class OpLogController {
    @Autowired
    private LogOptionService logOptionService;

    @RequestMapping("opLogs")
    public String opLogs(){
        return "bk/setting/op-logs";
    }

    @RequestMapping("getOpLogs")
    @ResponseBody
    public List<OpLog> getOpLogs(HttpSession session){
        ObjectId shopId = (ObjectId)session.getAttribute(Constant.SHOPID_SESSION);
        return logOptionService.getOpLogs(shopId);
    }
}
