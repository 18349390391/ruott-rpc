package org.ruott.consumer.controller;

import com.ruott.common.entity.User;
import com.ruott.common.service.UserService;
import org.ruott.consumer.test.End;
import org.ruott.rpcstarter.annotate.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RpcReference
    private UserService userService;

    /**
     * 测试AOP，查看是否解决注册问题
     * @return
     */
    @End
    @GetMapping("/getUser")
    public String getUser() {
        User resultUser = userService.getUserById(1);
        return resultUser.toString();
    }
}
