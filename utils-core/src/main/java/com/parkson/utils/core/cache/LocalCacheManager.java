package com.parkson.utils.core.cache;

import com.google.common.cache.LoadingCache;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName LocalCacheManager
 * @Description TO DO
 * @Author cheneason
 * @Date 2020/8/6 15:43
 * @Version 1.0
 */
public class LocalCacheManager {

    private static CopyOnWriteArrayList<LoadingCache> cacheList;

    static {
        cacheList = new CopyOnWriteArrayList<LoadingCache>();
    }

    /**
     * 注册新的本地缓存
     *
     * @param cache
     */
    public static void register(LoadingCache cache) {
        cacheList.add(cache);
    }

    /**
     * 移除新的本地缓存
     *
     * @param cache
     */
    public static void remove(LoadingCache cache) {
        cacheList.remove(cache);
    }

    /**
     * 刷新所有缓存
     */
    public static void refreshAll() {
        for (LoadingCache cache : cacheList) {
            cache.invalidateAll();
        }
    }
}
