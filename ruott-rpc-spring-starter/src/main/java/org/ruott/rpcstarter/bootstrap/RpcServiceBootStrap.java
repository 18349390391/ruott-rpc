package org.ruott.rpcstarter.bootstrap;

import com.ruott.rpc.RpcApplication;
import com.ruott.rpc.config.RpcConfig;
import com.ruott.rpc.model.ServiceMetaInfo;
import com.ruott.rpc.registry.LocalRegistry;
import com.ruott.rpc.registry.Registry;
import com.ruott.rpc.registry.RegistryFactory;
import org.ruott.rpcstarter.annotate.RpcService;
import org.ruott.rpcstarter.utils.TargetObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


/**
 * 初始化后处理 - 服务注册
 */
public class RpcServiceBootStrap implements BeanPostProcessor {

    /**
     * 后置处理器
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        /**
         * 获取AOP动态代理的原始类
         * 防止使用AOP相关直接导致无法扫描到注解
         */
        Object targetBean = TargetObject.get(bean);
        if (targetBean == null)
            targetBean = bean;

        //获取class
        Class<?> clazz = targetBean.getClass();
        RpcService rpcService = clazz.getAnnotation(RpcService.class);
        if (rpcService != null) {
            //获取注解中的class类
            Class<?> classInterface = rpcService.interfaceClass();

            //如果为null，获取第0个
            if (classInterface == void.class) {
                classInterface = clazz.getInterfaces()[0];
            }

            //本地注册
            LocalRegistry.register(classInterface.getName(), clazz);

            //获取配置
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();

            //获取注册中心
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistrar());

            ServiceMetaInfo metaInfo = ServiceMetaInfo.builder()
                    .serviceName(classInterface.getName())
                    .serviceHost(rpcConfig.getHost())
                    .servicePort(rpcConfig.getPort())
                    .build();
            //服务注册
            try {
                registry.register(metaInfo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
