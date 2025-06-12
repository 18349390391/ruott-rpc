package com.ruott.rpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {

    //加载配置文件，存储map 文件接口名称 => {key => 实例}
    private static final Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();

    //对象存储缓存，避免重复new
    private static final Map<String, Object> instanceMap = new ConcurrentHashMap<>();

    //系统SPI目录
    private static final String SIP_SYSTEM_PATH = "META-INF/ruott-rpc/system/";

    //用户SPI目录
    private static final String SIP_CUSTOM_PATH = "META-INF/ruott-rpc/custom/";

    //扫描路径
    private static final String[] SIP_PATHS = new String[]{SIP_SYSTEM_PATH, SIP_CUSTOM_PATH};


    /**
     * spi获取实例
     *
     * @param key   spi key
     * @param clazz 接口class
     * @param <T>   spi实例
     * @return
     */
    public static <T> T getInstance(String key, Class<T> clazz) {
        if (key == null || clazz == null) return null;
        Map<String, Class<?>> stringClassMap = loaderMap.get(clazz.getName());
        if (stringClassMap == null)
            throw new RuntimeException(String.format("load spi class error,class interface name :%s", clazz.getName()));
        if (!stringClassMap.containsKey(key))
            throw new RuntimeException(String.format("load spi class error,class key name :%s", key));
        Class<?> tarClass = stringClassMap.get(key);

        //先判断缓存是否存在
        if (!instanceMap.containsKey(tarClass.getName())) {
            //反射实例化
            try {
                Object targetObject = tarClass.getDeclaredConstructor().newInstance();
                instanceMap.put(tarClass.getName(), targetObject);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        return (T) instanceMap.get(tarClass.getName());
    }


    /**
     * 按需加载
     *
     * @param clazz 接口class文件
     * @return
     */
    public static Map<String, Class<?>> load(Class<?> clazz) {
        if (clazz == null) return null;
        log.info("load spi class is name：{}", clazz.getName());

        Map<String, Class<?>> keyClassMap = new HashMap<String, Class<?>>();

        //循环路径
        for (String sipPath : SIP_PATHS) {
            List<URL> resources = ResourceUtil.getResources(sipPath + clazz.getName());
            resources.forEach(resource -> {
                //读取文件内容
                try (InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        //去除首尾空白
                        line = line.trim();
                        String[] split = line.split("=");
                        if (split.length != 2)
                            continue;
                        String key = split[0];
                        String className = split[1];
                        keyClassMap.put(key, Class.forName(className));
                    }
                } catch (Exception e) {
                    log.error("spi resource load error", e);
                }
            });
        }
        loaderMap.put(clazz.getName(), keyClassMap);
        return keyClassMap;
    }

}
