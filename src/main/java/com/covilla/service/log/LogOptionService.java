package com.covilla.service.log;

import com.covilla.repository.mongodb.OpLogMongoDao;
import com.covilla.model.mongo.log.OpLog;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qmaolong on 2017/2/12.
 */
@Service
public class LogOptionService {
    @Autowired
    private OpLogMongoDao opLogMongoDao;

    public void addLog(OpLog opLog){
        opLogMongoDao.insertOpLog(opLog);
    }

    public List<OpLog> getOpLogs(ObjectId shopId){
        return opLogMongoDao.getOpLogs(shopId);
    }
}
