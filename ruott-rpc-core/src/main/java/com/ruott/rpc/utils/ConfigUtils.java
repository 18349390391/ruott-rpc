package com.ruott.rpc.utils;

import cn.hutool.core.io.resource.NoResourceException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.yaml.YamlUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ConfigUtils {


    /**
     * 加载配置类
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix) {
        return loadConfig(clazz, prefix, "");
    }

    public static <T> T loadConfig(Class<T> clazz, String prefix, String env) {
        StringBuilder configFileBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(env)) {
            configFileBuilder.append("-").append(env);
        }

        // 定义文件加载尝试
        List<Supplier<T>> loadAttempts = Arrays.asList(
                () -> {
                    Props props = new Props(buildFileName(configFileBuilder, ".properties"));
                    return props.toBean(clazz, prefix);
                },
                () -> YamlUtil.loadByPath(buildFileName(configFileBuilder, ".yaml")).getByPath(prefix, clazz),
                () -> YamlUtil.loadByPath(buildFileName(configFileBuilder, ".yml")).getByPath(prefix, clazz)
        );

        // 尝试加载，捕获NoResourceException并继续
        for (Supplier<T> attempt : loadAttempts) {
            try {
                T t = attempt.get();
                return t;
            } catch (NoResourceException ignored) {
            }
        }
        // 所有尝试都失败
        throw new NoResourceException("not found config file " + configFileBuilder);
    }

    private static String buildFileName(StringBuilder baseBuilder, String suffix) {
        String baseName = baseBuilder.toString();
        int lastDotIndex = baseName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            baseName = baseName.substring(0, lastDotIndex);
        }
        return baseName + suffix;
    }

}
