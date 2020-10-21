package com.edu.portal.service;

import com.edu.portal.bean.CartItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CartService {
    List<CartItem> addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response);

    List<CartItem> getAll(HttpServletRequest request, HttpServletResponse response);

    void delCartItem(long itemId, HttpServletRequest request, HttpServletResponse response);
}
