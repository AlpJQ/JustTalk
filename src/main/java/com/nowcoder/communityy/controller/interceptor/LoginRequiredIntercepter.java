package com.nowcoder.communityy.controller.interceptor;

import com.nowcoder.communityy.annotation.LoginRequired;
import com.nowcoder.communityy.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginRequiredIntercepter implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //拦截的有很多东西，这里我们只要拦截方法就可以了
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();//从HandlerMethod中拿到方法
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);//拿到我们设置好的注解LoginRequired
            //如果可以拿到，即loginRequired不为空，说明用户请求的就是登陆页面。再判断是否可以拿到用户数据，没有用户数据就是没有登陆的用户
            if(loginRequired != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }
}
