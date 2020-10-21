package com.edu.sso.service;

import com.edu.bean.TbUser;
import com.edu.common.bean.ShoppingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {
    ShoppingResult getCheckData(String param, Integer type);

    ShoppingResult insertUser(TbUser tbUser);

    ShoppingResult getLoginByNameAndPwd(String username, String password, HttpServletRequest request, HttpServletResponse response);

    ShoppingResult getUsertoken(String token);

    ShoppingResult getLoginOut(String token);
}
