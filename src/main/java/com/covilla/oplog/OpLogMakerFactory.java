package com.covilla.oplog;

import com.covilla.common.LogOptionEnum;
import com.covilla.oplog.maker.BusinessLogMaker;
import com.covilla.oplog.maker.CrudOpLogMaker;
import com.covilla.oplog.maker.GenerateCardOpLogMaker;

/**
 * log生成器工厂
 * Created by qmaolong on 2017/2/12.
 */
public class OpLogMakerFactory {
    public IOpLogMaker getOpLogMakerInstant(LogOptionEnum logOptionEnum){
        if (LogOptionEnum.CRUD.getName().equals(logOptionEnum.getName())){//增删改
            return new CrudOpLogMaker();
        }else if (LogOptionEnum.GENERATE_CARD.getName().equals(logOptionEnum.getName())){//生成卡
            return new GenerateCardOpLogMaker();
        }else {//其他业务操作
            return new BusinessLogMaker();
        }
    }
}
