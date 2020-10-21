package com.edu.order.controller;

import com.edu.common.bean.ShoppingResult;
import com.edu.order.bean.Order;
import com.edu.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ResponseBody
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ShoppingResult create(@RequestBody Order order){
        String orderId = orderService.createOrder(order);
        return ShoppingResult.ok(orderId);
    }
}
