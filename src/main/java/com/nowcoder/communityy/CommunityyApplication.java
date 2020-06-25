package com.nowcoder.communityy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommunityyApplication {

    //这个注解用来管理bean的生命周期，管理bean的初始化方法
    //被这个注解修饰的方法，会在构造器调用之后被执行
    @PostConstruct
    public void init() {
        // 解决netty启动冲突问题
        // see Netty4Utils.setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {
        SpringApplication.run(CommunityyApplication.class, args);
    }

}
