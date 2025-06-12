package com.ruott.rpc.registry;

import com.ruott.rpc.exception.RuottRpcException;
import com.ruott.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心缓存
 */
public class RegistryCache {

    //全局注册中心缓存
    public static volatile Map<String, List<ServiceMetaInfo>> serviceCache;

    private RegistryCache() {
    }


    //写缓存
    public static void writeCache(String nodeKey, List<ServiceMetaInfo> cacheList) {
        if (nodeKey == null || nodeKey.isEmpty())
            throw new RuottRpcException("write cache key is null or empty");
        if (serviceCache == null)
            initCache();
        serviceCache.put(nodeKey, cacheList);
    }

    /**
     * 读缓存
     *
     * @param nodeKye 节点的key
     * @return 符合key的缓存节点集合
     */
    public static List<ServiceMetaInfo> readCache(String nodeKye) {
        if (nodeKye == null)
            throw new RuottRpcException("read cache key is null");
        if (serviceCache == null)
            initCache();
        return serviceCache.get(nodeKye);
    }

    //清空缓存
    public static void clearCache(String nodeKye) {
        if (serviceCache != null)
            serviceCache.remove(nodeKye);
    }

    /**
     * 双检锁单例创建对象
     */
    private static void initCache() {
        if (serviceCache == null) {
            synchronized (RegistryCache.class) {
                if (serviceCache == null) {
                    serviceCache = new ConcurrentHashMap<>();
                }
            }
        }
    }
}
