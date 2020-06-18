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

    /*MD5加密，特点：相同字符串加密后的结果是相同的
        把字符串hello加密成ahsjdhlkl12，黑客有简单密码库，可以轻易破解，所以要加点措施
        user表中有一个salt字段，可以在字符串hello后面加一个随机字符串，比如
        hello + sf452 -> ahsjdhlkl12sf452
        这样子就不会被轻易破解了。下面参数key就是密码加salt之后的值
    */
    public static String md5(String key) {
        //使用org.apache.commons.lang3.StringUtils的类对象判别key,key为空和空字符串都为blank，比较方便
        if (StringUtils.isBlank(key)) {
            return null;
        }
        //使用spring自带的工具DigestUtils将key经过md5加密成16进制的字符串
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * 将服务器返回给浏览器的数据转换为JSON格式的字符串
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
