package org.ruott.provide.service;

import com.ruott.common.entity.User;
import com.ruott.common.service.UserService;
import org.ruott.rpcstarter.annotate.RpcService;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RpcService
public class UserServiceImpl implements UserService {

    @Override
    public User getUserById(Integer id) {
        System.out.println("用户id为：" + id);
        return new User(id,"ruott",18,new Date());
    }
}
