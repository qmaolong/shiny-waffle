package com.covilla.servlet;

import com.covilla.common.Config;
import com.covilla.common.RoleEnum;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.model.mongo.user.User;
import com.covilla.service.shop.ShopService;
import com.covilla.service.user.UserService;
import com.covilla.util.SpringContextUtil;
import com.covilla.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Date;

/**
 * Servlet implementation class InitServlet
 */
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(getClass());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InitServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
	public void init() throws ServletException {
        //创建超级管理员
        UserService userService = (UserService)SpringContextUtil.getBean("userService");
        User superAdmin = userService.findBy_id(Config.SUPER_ADMIN_ID);
        if (ValidatorUtil.isNull(superAdmin)){
            superAdmin = new User();
            superAdmin.set_id(Config.SUPER_ADMIN_ID);
            superAdmin.setRole(RoleEnum.superAdmin.getCode());
            superAdmin.setName("admin");
            try {
                superAdmin.setPassword(userService.generatePassword("123654"));
            }catch (Exception e){
                superAdmin.setPassword("");
            }
            superAdmin.setCreateTime(new Date());
            userService.insert(superAdmin);
            logger.info("初始化超级管理员");
        }
        ShopService shopService = (ShopService)SpringContextUtil.getBean("shopService");
        //创建数据门店
        Shop dataShop = shopService.findBy_id(Config.DATA_SHOP_ID);
        if (ValidatorUtil.isNull(dataShop)){
            dataShop = new Shop();
            dataShop.set_id(Config.DATA_SHOP_ID);
            dataShop.setId(0);
            dataShop.setName("克维拉科技");
            dataShop.setOwner(superAdmin.get_id());
            shopService.insert(dataShop);
            logger.info("初始化数据门店");
        }
        //创建模板门店
        Shop moduleShop = shopService.findBy_id(Config.MODOULE_SHOP_ID);
        if(ValidatorUtil.isNull(moduleShop)){
            moduleShop = new Shop();
            moduleShop.set_id(Config.MODOULE_SHOP_ID);
            moduleShop.setId(0);
            moduleShop.setName("模板门店");
            moduleShop.setOwner(superAdmin.get_id());
            shopService.insert(moduleShop);
            logger.info("初始化模板门店");
        }

	}
}
