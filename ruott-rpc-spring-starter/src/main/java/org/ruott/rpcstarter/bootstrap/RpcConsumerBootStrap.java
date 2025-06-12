package org.ruott.rpcstarter.bootstrap;

import com.ruott.rpc.proxy.ServiceProxyFactory;
import org.ruott.rpcstarter.annotate.RpcReference;
import org.ruott.rpcstarter.utils.TargetObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * 服务发现
 */
public class RpcConsumerBootStrap implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object targetBean = TargetObject.get(bean);
        if (targetBean == null)
            targetBean = bean;

        Class<?> clazz = targetBean.getClass();
        //获取所有成员变量
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            //获取注解
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                //获取类型
                Class<?> aClass = rpcReference.interfaceClass();
                if (aClass == void.class)
                    aClass = field.getType();

                //判断是否开起mock
                Object resultObject = null;

                //执行动态代理
                if(rpcReference.mock()){
                    ServiceProxyFactory.getMockProxy(aClass);
                } else {
                    resultObject= ServiceProxyFactory.getProxy(aClass);
                }
                try {
                    field.setAccessible(true);
                    field.set(targetBean, resultObject);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为对象注入代理失败");
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
