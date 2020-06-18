package com.nowcoder.communityy.controller.advice;

import com.nowcoder.communityy.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
    处理异常的类
 */
// annotations = Controller.class表示只会处理标有controller注解的bean
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    /*
        ExceptionHandler用于修饰方法，该方法会在Controller出现异常之后被调用，用于
        处理捕获到的异常
     */
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 将捕获到的异常记录到日志中
        logger.error("服务器发生异常: " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

        /*
            判断是异步请求出现异常，还是普通请求（重定向）
            需要分开处理
         */
        String xRequestedWith = request.getHeader("x-requested-with");// 固定写法，记住就好了
        // 如果是XMLHttpRequest表示异步请求，要返回JSON数据
        if ("XMLHttpRequest".equals(xRequestedWith)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器异常!"));
        } else {
            // 普通请求异常，重定向到错误页面
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }

}
