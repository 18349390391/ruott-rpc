package com.ruott.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务注册 元数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMetaInfo {

    //服务名称
    private String serviceName;

    //服务域名
    private String serviceHost;

    //服务端口
    private Integer servicePort;

    //服务分组
    private String serviceGroup = "default";


    public String getServiceKey() {
        return String.format("%s:%s:%s", serviceName, serviceHost, servicePort);
    }

    public String getRequestPath() {
        return String.format("http://%s:%s", serviceHost, servicePort);
    }
}
