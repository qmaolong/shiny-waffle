package com.covilla.service.shop;

import com.covilla.common.ModifyBlockEnum;
import com.covilla.common.OperationEnum;
import com.covilla.common.TicketParam;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.repository.mongodb.ShopMongoDao;
import com.covilla.model.mongo.printer.PrintScheme;
import com.covilla.model.mongo.printer.TicketForm;
import com.covilla.model.mongo.shop.Printer;
import com.covilla.model.mongo.shop.Shop;
import com.covilla.service.BaseMongoService;
import com.covilla.service.ServiceException;
import com.covilla.util.SerializationUtil;
import com.covilla.util.ValidatorUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LongLongAgo on 2016/10/9.
 */
@Service
@Transactional
public class PrinterService extends BaseMongoService<Shop> {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopMongoDao shopMongoDao;
    protected BaseMongoDao<Shop> getBaseMongoDao(){
        return shopMongoDao;
    }

    /**
     * 操作打印机信息
     * @param shopId
     * @param oper
     * @throws Exception
     */
    public void updatePrinter(String dataStr, ObjectId shopId, String oper) throws Exception{
        Shop shop = shopService.findBy_id(shopId);
        if(OperationEnum.add.getCode().equals(oper)){
            Printer printer = SerializationUtil.deSerializeObject(dataStr, Printer.class);
            printer.setCreateTime(new Date());

            if(ValidatorUtil.isNotNull(shop.getPrinters())){
                for(Printer printer1 : shop.getPrinters()){
                    if(printer.getName().equals(printer1.getName())){
                        throw new ServiceException("打印机名已存在~");
                    }
                }
            }

            if(printer.getIsDefault()){//设为默认是，其他都取消默认
                Query query = new Query(Criteria.where("_id").is(shopId).and("printer.isDefault").is(true));
                Update update = new Update().set("printer.$.isDefault", false);
                getMongoTemplate().updateFirst(query, update, Shop.class);
            }

            Criteria criteria =  Criteria.where("_id").is(shopId);
            Update update = new Update();
            update.push("printer", printer);

            getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        }else if(OperationEnum.edit.getCode().equals(oper)){
            Printer printer = SerializationUtil.deSerializeObject(dataStr, Printer.class);
            if(printer.getIsDefault()){//设为默认是，其他都取消默认
                Query query = new Query(Criteria.where("_id").is(shopId).and("printer.isDefault").is(true));
                Update update = new Update().set("printer.$.isDefault", false);
                getMongoTemplate().updateFirst(query, update, Shop.class);
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where("printer.name").is(printer.getName()));
            Update update = new Update().set("printer.$", printer);
            getMongoTemplate().updateFirst(query, update, Shop.class);
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<Printer> printers = SerializationUtil.deSerializeList(dataStr, Printer.class);
            for (int i=0; i<shop.getPrinters().size(); i++){
                for (Printer form : printers){
                    if(shop.getPrinters().get(i).getName().equals(form.getName())){
                        shop.getPrinters().remove(i);
                        break;
                    }
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));

            getMongoTemplate().updateFirst(query, new Update().set("printer", shop.getPrinters()), Shop.class);
        }else {
            throw new Exception("操作失败~");
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    /**
     * 操作打印格式
     * @param dataStr
     * @param shopId
     * @param oper
     * @throws Exception
     */
    public void updateTicketForm(String dataStr, ObjectId shopId, Integer category, String oper) throws Exception{
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNull(shop)){
            throw new ServiceException("未找到相应门店");
        }

        //获取类别
        String categoryName = "";
        List<TicketForm> forms = new ArrayList<TicketForm>();
        if(TicketParam.TicketCategory.Ticket.code.equals(category)){
            categoryName = "ticketForms";
            forms = shop.getTicketForms();
        }else if(TicketParam.TicketCategory.Kitchen.code.equals(category)){
            categoryName = "kitchenForms";
            forms = shop.getKitchenForms();
        }else if(TicketParam.TicketCategory.Control.code.equals(category)){
            categoryName = "controlForms";
            forms = shop.getControlForms();
        }

        if(OperationEnum.add.getCode().equals(oper)){
            TicketForm form = SerializationUtil.deSerializeObject(dataStr, TicketForm.class);
            form.setCreateTime(new Date());
            form.setId(generate2thLevelId(shopId, categoryName));
            if(ValidatorUtil.isNotNull(forms)){
                for(TicketForm form1 : forms){
                    if(form.getName().equals(form1.getName())){
                        throw new ServiceException("该名称已存在~");
                    }
                }
            }

            Criteria criteria =  Criteria.where("_id").is(shopId);
            Update update = new Update();
            update.push(categoryName, form);

            getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        }else if(OperationEnum.edit.getCode().equals(oper)){
            TicketForm form = SerializationUtil.deSerializeObject(dataStr, TicketForm.class);
            if(ValidatorUtil.isNotNull(forms)){
                for(TicketForm form1 : forms){
                    if(form.getName().equals(form1.getName())&&!form.getId().equals(form1.getId())){
                        throw new ServiceException("该名称已存在~");
                    }
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where(categoryName + ".id").is(form.getId()));
            Update update = new Update().set(categoryName + ".$", form);
            getMongoTemplate().updateFirst(query, update, Shop.class);
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<TicketForm> deleteItems = SerializationUtil.deSerializeList(dataStr, TicketForm.class);
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));
            Update update = new Update();
            for (TicketForm form : deleteItems){
                update.pull(categoryName, form);
            }

            getMongoTemplate().updateFirst(query, update, Shop.class);
        }else {
            throw new Exception("操作失败~");
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

    public List<TicketForm> getTicketForms(ObjectId shopId, Integer category){
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNull(shop)){
            return null;
        }else if(TicketParam.TicketCategory.Ticket.code.equals(category)){
            return shop.getTicketForms();
        }else if(TicketParam.TicketCategory.Kitchen.code.equals(category)){
            return shop.getKitchenForms();
        }else if(TicketParam.TicketCategory.Control.code.equals(category)){
            return shop.getControlForms();
        }
        return null;
    }

    public void updatePrinterPolicy(String dataStr, String itemsStr, ObjectId shopId, String oper){
        Shop shop = shopService.findBy_id(shopId);
        if(ValidatorUtil.isNull(shop)){
            throw new ServiceException("未找到相应门店");
        }
        List<PrintScheme.RuleItem> items = SerializationUtil.deSerializeList(itemsStr, PrintScheme.RuleItem.class);

        if(OperationEnum.add.getCode().equals(oper)){
            PrintScheme form = SerializationUtil.deSerializeObject(dataStr, PrintScheme.class);
            form.setItems(items);
            if(ValidatorUtil.isNotNull(shop)){
                for(PrintScheme form1 : shop.getPrintSchemes()){
                    if(form.getName().equals(form1.getName())){
                        throw new ServiceException("该名称已存在~");
                    }
                }
            }

            Criteria criteria =  Criteria.where("_id").is(shopId);
            Update update = new Update();
            update.push("printerPolicy", form);

            getMongoTemplate().updateFirst(new Query(criteria), update, Shop.class);
        }else if(OperationEnum.edit.getCode().equals(oper)){
            PrintScheme form = SerializationUtil.deSerializeObject(dataStr, PrintScheme.class);
            form.setItems(items);
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId)).addCriteria(Criteria.where("printerPolicy.name").is(form.getName()));
            Update update = new Update().set("printerPolicy.$", form);
            getMongoTemplate().updateFirst(query, update, Shop.class);
        }else if(OperationEnum.delete.getCode().equals(oper)){
            List<PrintScheme> deleteItems = SerializationUtil.deSerializeList(dataStr, PrintScheme.class);
            for (int i=0; i<shop.getPrintSchemes().size(); i++){
                for (PrintScheme form : deleteItems){
                    if(shop.getPrintSchemes().get(i).getName().equals(form.getName())){
                        shop.getPrintSchemes().remove(i);
                        break;
                    }
                }
            }
            Query query = new Query().addCriteria(Criteria.where("_id").is(shopId));
            getMongoTemplate().updateFirst(query, new Update().set("printerPolicy", shop.getPrintSchemes()), Shop.class);
        }else {
            throw new ServiceException("操作失败~");
        }
        updateModifyTime("shop", shopId, ModifyBlockEnum.shop.getKey());
    }

}
