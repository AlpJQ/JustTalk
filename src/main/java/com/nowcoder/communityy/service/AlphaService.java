package com.nowcoder.communityy.service;

import com.nowcoder.communityy.dao.AlphaDao;
import com.nowcoder.communityy.dao.DiscussPostMapper;
import com.nowcoder.communityy.dao.UserMapper;
import com.nowcoder.communityy.entity.DiscussPost;
import com.nowcoder.communityy.entity.User;
import com.nowcoder.communityy.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
//@Scope("prototype")
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public AlphaService() {
        System.out.println("实例化AlphaService");
    }

    @PostConstruct
    public void init() {
        System.out.println("初始化AlphaService");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }

    // 测试事务管理-->声明式事务
    // 事务的传播机制propagation，举例3种
    // REQUIRED : 支持当前事务（当前事务也叫外部事物：B被A调用，对于B来说，A就是当前事务），
    // B被A调用，如果A有事务，就按照A的来，如果A没有事务，那么B就创建新的事务
    // REQUIRES_NEW : 创建一个新事务，并且暂停当前事务（外部事务）
    // NESTED ：如果当前存在事务（外部事务）A，则嵌套在该事务A中执行（B有独立的提交和回滚），否则就和REQUIRED一样
    // 【NESTED说明】比如B事务调用A事务，则B事务嵌套在A事务执行，但是B事务有独立的提交和回滚
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1() {
        // 新增用户
        User user = new User();
        user.setUsername("Alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setEmail("Alpha@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("Hello");
        post.setContent("新人报道");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        // 人为制造异常,检验事务是否有效
        Integer.valueOf("abc");//字符串不能转换为整数

        return "ok";
    }

    // 测试事务管理-->编程式事务
    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        // transactionTemplate.execute()中的参数是一个接口callback
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                // 新增用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("新人来了");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                // 人为制造异常
                Integer.valueOf("abc");
                return "ok";
            }
        });
    }
}
