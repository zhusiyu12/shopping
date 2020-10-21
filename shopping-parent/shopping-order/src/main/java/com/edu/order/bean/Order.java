package com.edu.order.bean;

import com.edu.bean.TbOrder;
import com.edu.bean.TbOrderItem;
import com.edu.bean.TbOrderShipping;

import java.util.List;

public class Order extends TbOrder {
    private List<TbOrderItem> orderItems;//订单对应的订单项
    private TbOrderShipping orderShipping;//订单对应的物流信息

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
