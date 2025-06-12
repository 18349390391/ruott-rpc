package org.ruott.consumer.service;


import com.ruott.common.service.OrderService;
import org.ruott.rpcstarter.annotate.RpcService;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RpcService
public class OrderServiceImpl implements OrderService {


    @Override
    public String getOderNumber(Integer id) {
        System.out.println("OrderServiceImpl.getOderNumber id:" + id);
        return UUID.randomUUID().toString();
    }
}
