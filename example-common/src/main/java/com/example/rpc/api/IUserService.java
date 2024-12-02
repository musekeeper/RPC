package com.example.rpc.api;

import com.example.rpc.pojo.User;
import com.musekeeper.rpc.core.common.anno.RpcService;


@RpcService
public interface IUserService {
    User getById(Integer id);

}
