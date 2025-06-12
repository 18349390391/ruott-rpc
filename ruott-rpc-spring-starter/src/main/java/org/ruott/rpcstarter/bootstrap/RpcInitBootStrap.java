package org.ruott.rpcstarter.bootstrap;

import com.ruott.rpc.RpcApplication;
import com.ruott.rpc.config.RpcConfig;
import com.ruott.rpc.server.Server;
import com.ruott.rpc.server.VertxTcpServer;
import org.ruott.rpcstarter.annotate.EnableRuottRpc;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class RpcInitBootStrap implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        //扫描注解，获取注解参数
        boolean needServer = (boolean)metadata.getAnnotationAttributes(EnableRuottRpc.class.getName()).get("needServer");

        //获取配置文件
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        //启动服务
        if (needServer) {
            Server server = new VertxTcpServer();
            server.doService(rpcConfig.getPort());
        }

    }
}
