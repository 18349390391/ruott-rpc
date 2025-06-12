package com.ruott.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * mock动态代理handler
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        log.info("mock result type:{}", returnType);
        return getDefaultObject(returnType);
    }

    private Object getDefaultObject(Class<?> type) {
        if (type == String.class) {
            return "mock result";
        } else if (type == int.class) {
            return 0;
        } else if (type == short.class) {
            return (short) 0;
        } else if (type == long.class) {
            return 0L;
        } else if (type == float.class) {
            return 0F;
        } else if (type == double.class) {
            return 0D;
        } else if (type == boolean.class) {
            return false;
        } else if (type == byte.class) {
            return (byte) 0;
        } else if (type == char.class) {
            return (char) 0;
        } else {
            return null;
        }
    }
}
