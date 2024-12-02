package com.musekeeper.rpc.core.common.res;

import lombok.Data;

import java.io.Serializable;

/**
 * @author musekeeper
 * 响应对象
 */
@Data
public class RpcResponse implements Serializable {
    private String requestId;// 请求id
    private Object result;// 响应结果
    private String exception;// 异常信息
    private Class<?> ReturnType; // 调用方法的返回类型
}


