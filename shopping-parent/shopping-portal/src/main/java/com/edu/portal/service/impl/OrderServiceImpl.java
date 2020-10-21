package com.edu.portal.service.impl;

import com.edu.common.bean.ShoppingResult;
import com.edu.common.util.HttpClientUtil;
import com.edu.common.util.JsonUtils;
import com.edu.portal.bean.Order;
import com.edu.portal.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public ShoppingResult createOrder(Order order) {
        String str = HttpClientUtil.doPostJson("http://localhost:8085/order/create", JsonUtils.objectToJson(order));
        // 调用远程的服务
        return ShoppingResult.format(str);
    }


}
