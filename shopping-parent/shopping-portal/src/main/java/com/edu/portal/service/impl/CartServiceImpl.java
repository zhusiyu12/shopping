package com.edu.portal.service.impl;

import com.edu.bean.TbItem;
import com.edu.common.bean.ShoppingResult;
import com.edu.common.util.CookieUtils;
import com.edu.common.util.HttpClientUtil;
import com.edu.common.util.JsonUtils;
import com.edu.portal.bean.CartItem;
import com.edu.portal.service.CartService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{
    @Value("${CONTENT_BASE_URL}")
    private String CONTENT_BASE_URL;
    @Value("${ITEM_BASE_URL}")
    private String ITEM_BASE_URL;

    @Override
    public List<CartItem> addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        //查询所有缓存数据
        List<CartItem> cartItems = getAllCartItem(request);
        //判断当前的商品不在缓存中
        CartItem item = null;
        for (CartItem cartItem:cartItems){
            if(cartItem.getId() == itemId){
                cartItem.setNum(cartItem.getNum()+num);
                item  = cartItem;
                break;
            }
        }

        if(null == item){
            item = new CartItem();
            String tbItemString = HttpClientUtil.doGet(CONTENT_BASE_URL+ITEM_BASE_URL+itemId);
            ShoppingResult shoppingResult = ShoppingResult.formatToPojo(tbItemString, TbItem.class);
            if(shoppingResult.getStatus() == 200){
                TbItem tbItem =  (TbItem) shoppingResult.getData();
                item.setId(tbItem.getId());
                item.setTitle(tbItem.getTitle());
                item.setImage(tbItem.getImage());
                item.setPrice(tbItem.getPrice());
                item.setNum(num);
                cartItems.add(item);
            }
        }

        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(cartItems),true);
        return cartItems;
    }

    @Override
    public List<CartItem> getAll(HttpServletRequest request, HttpServletResponse response) {
        return getAllCartItem(request);
    }

    @Override
    public void delCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {
        List<CartItem> cartItems = getAllCartItem(request);
        for (CartItem cartItem:cartItems){
            if(cartItem.getId() == itemId){
               cartItems.remove(cartItem);
               break;
            }
        }
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(cartItems),true);
    }

    private List<CartItem> getAllCartItem(HttpServletRequest request) {
        //从cookie中去查询
        String cartString = CookieUtils.getCookieValue(request,"TT_CART",true);
        if(StringUtils.isEmpty(cartString)){
            return  new ArrayList<>();
        }
        return JsonUtils.jsonToList(cartString,CartItem.class);
    }
}
