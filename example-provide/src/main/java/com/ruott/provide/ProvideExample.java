package com.ruott.provide;

import com.ruott.common.service.OrderService;
import com.ruott.common.service.UserService;
import com.ruott.provide.service.OrderServiceImpl;
import com.ruott.provide.service.UserServiceImpl;
import com.ruott.rpc.bootstarter.ProvideBootStarter;
import com.ruott.rpc.model.ServiceRegistryInfo;

import java.util.ArrayList;
import java.util.List;

public class ProvideExample {

    public static void main(String[] args){
        List<ServiceRegistryInfo<?>> registryInfos = new ArrayList<>() {{
            add(new ServiceRegistryInfo<UserService>(UserService.class.getName(), UserServiceImpl.class));
            add(new ServiceRegistryInfo<OrderService>(OrderService.class.getName(), OrderServiceImpl.class));
        }};

        ProvideBootStarter.init(registryInfos);
    }
}
