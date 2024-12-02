package com.musekeeper.rpc.core.common.req;


import lombok.Data;

import java.io.Serializable;

/**
 * @author musekeeper
 * RPC 请求对象
 * */
@Data
public class RpcRequest implements Serializable {
    private String requestId; // 请求的唯一标识符
    private String className; // 目标类的名称
    private String methodName; // 目标方法的名称
    private Class<?>[] parameterTypes; // 目标方法的参数类型
    private Object[] parameters; // 目标方法的参数值
    private Class<?> ReturnType; // 目标方法的返回类型
}
