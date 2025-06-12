package org.ruott.rpcstarter.utils;

import org.springframework.aop.framework.Advised;

public class TargetObject {

    // 获取代理对象背后的真实目标对象
    public static Object get(Object proxy) {
        try {
            //查看传入的对象是否为代理对象，如果是代理对象则返回真实对象。如果不是代理对象则直接返回。
            if (proxy instanceof Advised advised) {
                return advised.getTargetSource().getTarget();
            }
            return proxy;
        } catch (Exception e) {
            System.err.println("获取目标对象失败: " + e.getMessage());
            return proxy;
        }
    }
}
