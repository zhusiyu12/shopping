package com.edu.sso.controller;

import com.alibaba.druid.util.StringUtils;
import com.edu.bean.TbUser;
import com.edu.common.bean.ShoppingResult;
import com.edu.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/showRegister")
    public String register() {
        return "register";
    }
    @RequestMapping("/showLogin")
    public String showLogin(String redirect, Model model) {
        model.addAttribute("redirect",redirect);
        return "login";
    }

    @ResponseBody
    @RequestMapping("/check/{param}/{type}")
    public Object checkData(@PathVariable("param") String param,@PathVariable("type") Integer type,String callback){
        ShoppingResult result = null;
        if(type != 1 && type != 2 && type != 3){
            result = ShoppingResult.build(400,"类型不正确");
        }
        if (null != result){
            if(!StringUtils.isEmpty(callback)){
                MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
                jacksonValue.setJsonpFunction(callback);
                return jacksonValue;
            }else {
                return result;
            }

        }
        //如果校验成功
        result =userService.getCheckData(param,type);
        if(!StringUtils.isEmpty(callback)){
            MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
            jacksonValue.setJsonpFunction(callback);
            return jacksonValue;
        }else {
            return result;
        }

    }

    @ResponseBody
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ShoppingResult register(TbUser tbUser){
        return userService.insertUser(tbUser);
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ShoppingResult login(String username, String password, HttpServletRequest request, HttpServletResponse response){
        return userService.getLoginByNameAndPwd(username,password,request,response);
    }

    @ResponseBody
    @RequestMapping("/token/{token}")
    public Object token(@PathVariable String token,String callback){
        ShoppingResult result = userService.getUsertoken(token);
        if (StringUtils.isEmpty(callback)){
            return result;
        }
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        jacksonValue.setJsonpFunction(callback);
        return jacksonValue;
    }

    @ResponseBody
    @RequestMapping("/logout/{token}")
    public Object loginOut(@PathVariable String token,String callback){
        ShoppingResult result = userService.getLoginOut(token);
        if (StringUtils.isEmpty(callback)){
            return result;
        }
        MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
        jacksonValue.setJsonpFunction(callback);
        return jacksonValue;
    }

}
