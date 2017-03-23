package com.covilla.cache;

/**
 * 缓存接口, 为了兼容AbstractCache 和 DefAbstractCache, 作为这两个类的实现接口
 */
public interface CacheInterface {
    public void init();

    public String getCacheName();
}
