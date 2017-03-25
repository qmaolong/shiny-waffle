package com.covilla.service.purchase;

import com.covilla.common.OperationEnum;
import com.covilla.model.mongo.purchase.Area;
import com.covilla.model.mongo.purchase.Supplier;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.ServiceException;
import com.covilla.service.shop.ShopService;
import com.covilla.util.SerializationUtil;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by qmaolong on 2017/3/15.
 */
@Service
public class SupplierService {
    @Autowired
    private ShopService shopService;

    public List<Area> getAreasByShop(ObjectId shopId){
        Shop shop = shopService.findBy_id(shopId);
        return shop.getAreas();
    }

    public void editArea(String dataStr, String oper, ObjectId shopId){
        Shop shop = shopService.findBy_id(shopId);
        if (OperationEnum.add.getCode().equals(oper)){
            Area area = SerializationUtil.deSerializeObject(dataStr, Area.class);
            Integer id = shopService.generate2thLevelId(shopId, "area");
            area.setId(id.toString());
            if (ValidatorUtil.isNull(shop.getAreas())){
                shop.setAreas(new ArrayList<Area>());
            }
            shop.getAreas().add(area);
        }else if (OperationEnum.edit.getCode().equals(oper) && ValidatorUtil.isNotNull(shop.getAreas())){
            Area area = SerializationUtil.deSerializeObject(dataStr, Area.class);
            for (Area tmp : shop.getAreas()){
                if (tmp.getId().equals(area.getId())){
                    shop.getAreas().set(shop.getAreas().indexOf(tmp), area);
                    break;
                }
            }
        }else if (OperationEnum.delete.getCode().equals(oper) && ValidatorUtil.isNotNull(shop.getAreas())){
            List<Area> areas = SerializationUtil.deSerializeList(dataStr, Area.class);
            Iterator<Area> iterator = shop.getAreas().iterator();
            while (iterator.hasNext()){
                for (Area area : areas){
                    if (iterator.next().getId().equals(area.getId())){
                        iterator.remove();
                    }
                }
            }
        }else {
            throw new ServiceException("没有相关操作");
        }
        shopService.updateDocument(shop);
    }

    public Area findAreaByShop(String areaId, ObjectId shopId){
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNull(shop) || ValidatorUtil.isNull(shop.getAreas())){
            return null;
        }
        for (Area area : shop.getAreas()){
            if (areaId.equals(area.getId())){
                return area;
            }
        }
        return null;
    }

    public void editSupplier(String dataStr, String oper, ObjectId shopId){
        Shop shop = shopService.findBy_id(shopId);
        if (ValidatorUtil.isNull(shop)){
            throw new ServiceException("门店查询失败");
        }
        Area area = null;
        if (OperationEnum.add.getCode().equals(oper)){
            Supplier supplier = SerializationUtil.deSerializeObject(dataStr, Supplier.class);
            for (Area temp : shop.getAreas()){
                if(temp.getId().equals(supplier.getAreaId())){
                    area = temp;
                    break;
                }
            }
            int id = generateSupplierNo(shop.getAreas());
            supplier.setNo(id);
            if (ValidatorUtil.isNull(area.getSuppliers())){
                area.setSuppliers(new ArrayList<Supplier>());
            }
            area.getSuppliers().add(supplier);
        }else if (OperationEnum.edit.getCode().equals(oper)){
            Supplier supplier = SerializationUtil.deSerializeObject(dataStr, Supplier.class);
            for (Area temp : shop.getAreas()){
                if(temp.getId().equals(supplier.getAreaId())){
                    area = temp;
                    break;
                }
            }
            for (Supplier tmp : area.getSuppliers()){
                if (tmp.getNo().equals(supplier.getNo())){
                    area.getSuppliers().set(area.getSuppliers().indexOf(tmp), supplier);
                    break;
                }
            }
        }else if (OperationEnum.delete.getCode().equals(oper)){
            List<Supplier> suppliers = SerializationUtil.deSerializeList(dataStr, Supplier.class);
            for (Area temp : shop.getAreas()){
                Iterator<Supplier> iterator = temp.getSuppliers().iterator();
                while (iterator.hasNext()){
                    for (Supplier supplier : suppliers){
                        if (iterator.next().getNo().equals(supplier.getNo())){
                            iterator.remove();
                        }
                    }
                }
            }
        }else {
            throw new ServiceException("没有相关操作");
        }
        shopService.updateDocument(shop);
    }

    private int generateSupplierNo(List<Area> areas){
        if (ValidatorUtil.isNull(areas)){
            return 1;
        }
        int max = 0;
        for (Area area : areas){
            if (ValidatorUtil.isNull(area.getSuppliers())){
                continue;
            }
            for (Supplier supplier : area.getSuppliers()){
                if (Integer.valueOf(supplier.getNo()) > max){
                    max = Integer.valueOf(supplier.getNo());
                }
            }
        }
        return ++max;
    }
}
