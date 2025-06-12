package com.ruott.provide.service;

import com.ruott.common.entity.User;
import com.ruott.common.service.UserService;

import java.util.Date;

public class UserServiceImpl implements UserService {


    @Override
    public User getUserById(Integer id) {
        if(id == 1){
            //模拟从数据库获取数据
            return User.builder()
                    .name("ruott")
                    .age(18)
                    .id(id)
                    .createTime(new Date())
                    .build();
        }
        return null;
    }
}
