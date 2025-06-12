package org.ruott.rpcstarter.config;


import com.ruott.rpc.registry.RegistryKeys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ruott.rpc.registry-config")
public class RegistryConfig {

    //注册中心类型
    private String registrar = RegistryKeys.ETCD;

    //注册中心地址
    private String address = "http://127.0.0.1:2379";

    //用户名
    private String username;

    //密码
    private String password;

    //连接超时时间
    private Long timeout = 10000L;
}
