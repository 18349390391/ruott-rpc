package com.ruott.rpc.utils;


import lombok.extern.slf4j.Slf4j;

/**
 * 字符串截取工具类
 */
@Slf4j
public class StrCapture {

    /**
     * 将指定的etcd的nodekey截取为类的全限定名
     * @param registryNodeKey
     * @return
     */
    public static String capture(String registryNodeKey) {
        try{
            int startIndex = registryNodeKey.lastIndexOf('/') + 1;
            int endIndex = registryNodeKey.indexOf(':', startIndex);
            return  registryNodeKey.substring(startIndex, endIndex);
        }catch(Exception e){
            log.error("String type does not match");
        }
       return "";
    }
}
