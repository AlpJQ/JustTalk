package com.nowcoder.communityy.service;

import com.nowcoder.communityy.dao.UserMapper;
import com.nowcoder.communityy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
