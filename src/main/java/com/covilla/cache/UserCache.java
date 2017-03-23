package com.covilla.cache;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 用户缓存
 *
 * @author daiwen
 * @date 2015/1/28
 */
@Component
public class UserCache extends AbstractCache {
    public static final String CACHEID = "userCache";

    /**
     * 初始化的时候加载所有缓存数据,注意加上@PostConstruct注解
     */
    @PostConstruct
    public void init() {
//        super.init();
    }

    @Override
    public List<Map<String, Object>> getAll() {
        return null;
    }


    @Override
    public String getCacheName() {
        return CACHEID;
    }

}
