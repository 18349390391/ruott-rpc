package com.ruott.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务注册
 * @param <T>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegistryInfo<T> {

    private String serviceName;

    private Class<? extends T> implClass;
}
