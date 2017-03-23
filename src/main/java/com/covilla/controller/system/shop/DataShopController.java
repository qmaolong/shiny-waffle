package com.covilla.controller.system.shop;

import com.covilla.common.Config;
import com.covilla.service.shop.ShopService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 数据门店操作
 * Created by qmaolong on 2017/3/13.
 */
@Controller
@RequestMapping("/sys/dataShop")
public class DataShopController {
    @Autowired
    private ShopService shopService;
    private static ObjectId dataShopId = Config.DATA_SHOP_ID;


}
