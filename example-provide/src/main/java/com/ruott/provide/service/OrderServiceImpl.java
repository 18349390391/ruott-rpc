package com.ruott.provide.service;

import com.ruott.common.service.OrderService;

import java.util.UUID;

public class OrderServiceImpl implements OrderService {
    @Override
    public String getOderNumber(Integer id) {
        System.out.println("传入的id为：" + id);
        return UUID.randomUUID().toString();
    }
}
