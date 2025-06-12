package com.ruott.customer.controller;

import com.ruott.common.service.OrderService;
import com.ruott.rpc.bootstarter.ConsumerBootStrap;
import com.ruott.rpc.proxy.ServiceProxyFactory;

public class OrderController {

    public static void main(String[] args) {
        //初始化
        ConsumerBootStrap.init();

        //调用动态代理
        OrderService proxy = ServiceProxyFactory.getProxy(OrderService.class);
        System.out.println(proxy.getOderNumber(1));
    }
}
