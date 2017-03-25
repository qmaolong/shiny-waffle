package com.covilla.controller.system.wx;

import com.alibaba.fastjson.JSONObject;
import com.covilla.common.Config;
import com.covilla.service.config.ConfigService;
import com.covilla.util.wechat.entity.customer.Article;
import com.covilla.util.wechat.service.MatiarialService;
import com.covilla.util.wechat.service.MenuService;
import com.covilla.vo.ResultMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 公众号相关设置
 * Created by qmaolong on 2017/1/3.
 */
@Controller
@RequestMapping("sys/system/wx")
public class WXSettingController {
    @Autowired
    private ConfigService configService;

    /**
     * 自定义菜单
     * @return
     */
    @RequestMapping("menu")
    public String menu(Model model){
        /*Object menu = configService.findConfigByName(ConfigModule.WX_MENU_CONFIG);
        if(ValidatorUtil.isNull(menu)){
            menu = MenuService.findMenu(Config.WEIXIN_APPID, Config.WEIXIN_APPSECRET);
            configService.getMongoTemplate().upsert(new Query(Criteria.where("name").is(ConfigModule.WX_MENU_CONFIG)), new Update().set("value", menu), ConfigModule.class);
        }*/
        JSONObject menu = MenuService.findMenu(Config.WEIXIN_APPID, Config.WEIXIN_APPSECRET);
        model.addAttribute("menu", menu.getJSONObject("menu"));
        return "system/wx/menu";
    }

    @RequestMapping("saveMenu")
    @ResponseBody
    public ResultMsg saveMEnu(String dataStr){
        return new ResultMsg();
    }

    @RequestMapping("publishMenu")
    @ResponseBody
    public ResultMsg publishMenu(String dataStr){
        try {
            JSONObject object = JSONObject.parseObject(dataStr);
            int result = MenuService.createMenu(object.toJSONString(), Config.WEIXIN_APPID, Config.WEIXIN_APPSECRET);
            if (result == 0){
                return ResultMsg.buildSuccessMsg();
            }else {
                return ResultMsg.buildFailMsg("-1", "发布失败");
            }
        }catch (Exception e){
            return ResultMsg.buildFailMsg("-1", "发布失败");
        }
    }

    @RequestMapping("materials")
    public String materials(){
        return "system/wx/materials";
    }

    @RequestMapping("getMaterials")
    @ResponseBody
    public List<Article> getMaterials(){

        JSONObject matiarialCounts = MatiarialService.getMatiarialCount(Config.WEIXIN_APPID, Config.WEIXIN_APPSECRET);
        Integer count = matiarialCounts.getInteger("news_count");

        List<Article> articles = MatiarialService.getMatiarials(Config.WEIXIN_APPID, Config.WEIXIN_APPSECRET, 0, count);
        return articles;
    }
}
