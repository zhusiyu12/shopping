package com.edu.portal.controller;

import com.edu.common.bean.ShoppingResult;
import com.edu.portal.bean.CartItem;
import com.edu.portal.bean.Order;
import com.edu.portal.service.CartService;
import com.edu.portal.service.OrderService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order-cart")
    public String orderCart(Model model, HttpServletRequest request, HttpServletResponse response){
        List<CartItem> cartItems = cartService.getAll(request,response);
        model.addAttribute("cartList",cartItems);
        return "order-cart";
    }

    @RequestMapping("/create")
    public String create(Order order, Model model){
        ShoppingResult result = orderService.createOrder(order);
        if (result.getStatus()==200){
            model.addAttribute("orderId",order.getOrderId());
            model.addAttribute("payment",order.getPayment());
            model.addAttribute("date",new DateTime(order.getCreateTime()).plusDays(3));
            return "success";
        }
        model.addAttribute("message","保存失败");
        return "error/exception";
    }
}
