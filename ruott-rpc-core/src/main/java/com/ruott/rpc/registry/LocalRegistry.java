package com.ruott.rpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册器
 */
public class LocalRegistry {

    /**
     * 服务注册列表存储
     */
    private final static Map<String, Class<?>> map = new ConcurrentHashMap<>();


    /**
     * 注册服务
     *
     * @param registerName 服务名称
     * @param clazz        服务class
     */
    public static void register(String registerName, Class<?> clazz) {
        map.put(registerName, clazz);
    }

    /**
     * 获取服务
     *
     * @param registerName 服务名称
     * @return 服务class
     */
    public static Class<?> get(String registerName) {
        return map.get(registerName);
    }

    /**
     * 删除服务
     *
     * @param registerName 服务名称
     * @return 是否成功
     */
    public static boolean remove(String registerName) {
        return map.remove(registerName) != null;
    }

}
