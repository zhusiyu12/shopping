package com.edu.sso.service.impl;

import com.edu.bean.TbUser;
import com.edu.bean.TbUserExample;
import com.edu.common.bean.ShoppingResult;
import com.edu.common.util.CookieUtils;
import com.edu.common.util.JsonUtils;
import com.edu.mapper.TbUserMapper;
import com.edu.sso.dao.RedisDao;
import com.edu.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private RedisDao redisDao;
    @Value("${REDIS_SESSION_TOKEN_KEY}")
    private String REDIS_SESSION_TOKEN_KEY;
    @Value("${REDIS_SESSION_EXPIRE}")
    private Integer REDIS_SESSION_EXPIRE;
    @Override
    public ShoppingResult getCheckData(String param, Integer type) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if(1 == type){
            criteria.andUsernameEqualTo(param);
        }else if(2 == type){
            criteria.andPhoneEqualTo(param);
        }else if(3 == type){
            criteria.andEmailEqualTo(param);
        }

        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(null != tbUsers && tbUsers.size()>0){
            return ShoppingResult.ok(false);
        }
        return ShoppingResult.ok(true);
    }

    @Override
    public ShoppingResult insertUser(TbUser tbUser) {
        try {
            tbUser.setCreated(new Date());
            tbUser.setUpdated(new Date());
            tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
            tbUserMapper.insertSelective(tbUser);
        }catch (Exception e){
            e.printStackTrace();
            ShoppingResult.build(500,"注册失败");
        }
        return ShoppingResult.ok(true);
    }

    @Override
    public ShoppingResult getLoginByNameAndPwd(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
        if(null == tbUsers && tbUsers.size() == 0){
            return ShoppingResult.build(400,"用户名或密码错误");

        }
        //如果不为空
        TbUser tbUser = tbUsers.get(0);
        if(!tbUser.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
            return ShoppingResult.build(500,"当前用户名或密码错误");
        }
        String token = UUID.randomUUID().toString();
        tbUser.setPassword(null);
        redisDao.set(REDIS_SESSION_TOKEN_KEY+":"+token, JsonUtils.objectToJson(tbUser));
        redisDao.expire(REDIS_SESSION_TOKEN_KEY+":"+token,REDIS_SESSION_EXPIRE);

        CookieUtils.setCookie(request,response,"TT_TOKEN",token,true);

        return ShoppingResult.ok(token);
    }

    @Override
    public ShoppingResult getUsertoken(String token) {
        String s =  redisDao.get(REDIS_SESSION_TOKEN_KEY+":"+token);
        if (StringUtils.isEmpty(s)){
            ShoppingResult.build(400,"seesion已经过期");
        }
        redisDao.expire(REDIS_SESSION_TOKEN_KEY+":"+token,REDIS_SESSION_EXPIRE);

        return ShoppingResult.ok(JsonUtils.jsonToPojo(s,TbUser.class));
    }

    @Override
    public ShoppingResult getLoginOut(String token) {
        redisDao.expire(REDIS_SESSION_TOKEN_KEY+":"+token,0);
        return ShoppingResult.ok();
    }
}
