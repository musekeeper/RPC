package com.example.comsumer.controller;


import com.example.rpc.api.IUserService;
import com.example.rpc.pojo.User;
import com.musekeeper.rpc.core.common.anno.RpcReference;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {
    @RpcReference(serviceName = "RpcProvider")
    IUserService userService;

    @RequestMapping("/getUserById")
    public User getUserById() {
        long start = System.currentTimeMillis();
        User user = userService.getById(1);
        long end = System.currentTimeMillis();
        System.out.println("时间:" + (end - start));
        return user;
    }
}
