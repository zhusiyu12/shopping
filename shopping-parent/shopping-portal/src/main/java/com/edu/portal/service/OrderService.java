package com.edu.portal.service;

import com.edu.common.bean.ShoppingResult;
import com.edu.portal.bean.Order;

public interface OrderService {
    ShoppingResult createOrder(Order order);
}
