package com.edu.portal.interceptor;

import com.edu.bean.TbUser;
import com.edu.common.bean.ShoppingResult;
import com.edu.common.util.CookieUtils;
import com.edu.common.util.HttpClientUtil;
import org.apache.http.cookie.Cookie;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 拦截方法执行之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断用户是否登录

        String token = CookieUtils.getCookieValue(request,"TT_TOKEN",true);
        if(!StringUtils.isEmpty(token)) {
            TbUser user = null;
            // 我拿到了token,我们要去缓存中，取出当前的token所对应的用户
            String tokenStr = HttpClientUtil.doGet("http://localhost:8084/user/token/"+token);
            ShoppingResult result = ShoppingResult.formatToPojo(tokenStr, TbUser.class);
            if(result.getStatus() == 200) {
                user = (TbUser) result.getData();
            }
            if(null == user) {
                // 没有用户，就重定向到登录页面
                response.sendRedirect("http://localhost:8084/user/showLogin?redirect="+request.getRequestURL());
                return false ;
            }
            return true;
        } else {
            response.sendRedirect("http://localhost:8084/user/showLogin?redirect="+request.getRequestURL());
            return false ;
        }
    }

    /**
     * 拦截的过程，在方法返回modelAndView之前执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 这是在方法执行完毕之后，（返回modelandview之后）
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
