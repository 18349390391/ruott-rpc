package com.ruott.customer.controller;

import com.ruott.common.entity.User;
import com.ruott.common.service.UserService;
import com.ruott.rpc.bootstarter.ConsumerBootStrap;
import com.ruott.rpc.proxy.ServiceProxyFactory;

public class UserController {


    public static void main(String[] args) {
        ConsumerBootStrap.init();
        for (int i = 0; i < 10; i++) {
            extracted();
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }
    }

    private static void extracted() {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = userService.getUserById(1);
        System.out.println(user);
    }
}
