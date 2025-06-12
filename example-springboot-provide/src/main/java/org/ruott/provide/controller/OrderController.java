package org.ruott.provide.controller;

import com.ruott.common.service.OrderService;
import org.ruott.rpcstarter.annotate.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @RpcReference
    private OrderService orderService;

    @GetMapping("/getOrderNo")
    public String getOrderNo(){
        String orderNo = orderService.getOderNumber(1);
        System.out.println(orderNo);
        return orderNo;
    }
}
