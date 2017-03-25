package com.covilla.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 缓存基类
 *
 * @author christ
 * @date 2015/1/26
 * @deprecated use DefAbstractCache instead
 */
@Deprecated
public abstract class AbstractCache implements CacheInterface {
    private static Logger logger = LoggerFactory.getLogger(AbstractCache.class);

    //-----------------------api------------------------

    /**
     * 从缓存中获得数据
     *
     * @param key
     * @return
     */
    public synchronized Object get(String key) {

        // 直接从缓存中获得
        Object value = getFromCache(key);

        // 如果缓存中有数据则返回
        if (value != null) {
            return value;
        }

        // 否则重新初始化缓存，因为有可能缓存过期致使其没有找到数据，故而重新初始化一次
        init();

        // 再次尝试从
        return getFromCache(key);
    }


    /**
     * 将数据存入缓存中
     *
     * @param key
     * @param data
     * @return
     */
    public void put(String key, Object data) {

        getCache().put(new Element(key, data));
    }

    /**
     * 删除缓存
     *
     * @param key
     */
    public void remove(String key) {
        if (null != key) {
            getCache().remove(key);
        }
    }

    /**
     * 从缓存中获得数据
     *
     * @param key
     * @return
     */
    protected Object getFromCache(String key) {
        // 先从缓存中获得
        Element e = getCache().get(key);
        if (e != null) {
            return e.getObjectValue();
        } else {
            return null;
        }
    }

    /**
     * 刷新缓存
     */
    public synchronized void refresh() {
        init();
    }


    //-----------------------abstract method------------------------

    /**
     * 加载所有数据
     *
     * @return
     */
    protected abstract List<Map<String, Object>> getAll();

    /**
     * 获得缓存名称，默认使用ggCarCache
     *
     * @return
     */
    public abstract String getCacheName();

    //-----------------------impls------------------------


    /**
     * 初始化加载所有缓存数据
     * <li>子类需要将该方法加上@PostConstruct注解<li/>
     */
    public void init() {

        logger.info("----------清除缓存：" + this.getCacheName());
        getCache().removeAll();

        logger.info("----------初始化缓存：" + this.getCacheName());

        // 将单条数据缓存起来:缓存key：每条数据的id字段
        List<Map<String, Object>> data = getAll();

        // 目前看来缓存单条数据用处不是很大，故而先不缓存之....
//        if (data != null) {
//            for (Map<String, Object> each : data) {
//                getCache().put(new Element(each.get("id").toString(), each));
//            }
//        }

        // 将数据库返回的所有记录缓存起来缓存key：缓存的名称
        if (!CollectionUtils.isEmpty(data)) {
            if (getCache() == null) {
                logger.error("缓存{}创建失败", getCacheName());
            }
            getCache().put(new Element(getCacheName(), data));
        }
    }

    /**
     * 获得当前底层缓存
     *
     * @return
     */
    protected Cache getCache() {
        Cache cache = CacheManager.create(AbstractCache.class.getResource("/config/ehcache.xml")).getCache(getCacheName());
        if (cache == null) {
            logger.error("缓存{}创建失败", getCacheName());
        }

        return cache;
    }

}
