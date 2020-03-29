package com.nowcoder.communityy.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {

    //生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    //MD5加密
    //hello -> ahsjdhlkl12
    //hello + sf452 -> ahsjdhlkl12sf452
    public static String md5(String key) {
        //使用org.apache.commons.lang3.StringUtils的类对象判别key,key为空和空字符串都为blank，比较方便
        if (StringUtils.isBlank(key)) {
            return null;
        }
        //使用spring自带的工具DigestUtils将key经过md5加密
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 得到浏览器传过来的json数据
     *
     * @param code 编码
     * @param msg  提示信息
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            // 把map中存放的信息放入json对象
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

    // 测试一下上面编写的getJSONString方法
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", 23);
        System.out.println(getJSONString(0, "ok", map));
    }
}
