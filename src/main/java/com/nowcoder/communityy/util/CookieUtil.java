package com.nowcoder.communityy.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/*
    这是一个从浏览器的request中获取指定name的cookie的工具类
 */
public class CookieUtil {

    public static String getValue(HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw new IllegalArgumentException("参数为空!");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
}
