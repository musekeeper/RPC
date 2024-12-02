package com.musekeeper.rpc.core.common.serialization.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musekeeper.rpc.core.common.res.RpcResponse;
import com.musekeeper.rpc.core.common.serialization.Serializer;

import java.io.IOException;

/**
 * @author musekeeper
 * Json序列化器
 */
public class JsonSerializer implements Serializer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        if(obj == null) {
            throw new IllegalArgumentException("obj is null");
        }
        // 特殊处理RpcResponse
        if (obj instanceof RpcResponse) {
            obj = (T) handleSerializeResponse((RpcResponse) obj);
        }
        return OBJECT_MAPPER.writeValueAsBytes(obj);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws IOException {
        T obj = OBJECT_MAPPER.readValue(bytes, clazz);
        if (obj instanceof RpcResponse) {
            obj = (T) handleDeserializeResponse((RpcResponse) obj);
        }
        return obj;
    }


    /**
     * 由于Object 的原始对象会被擦除，导致反序列化时 LinkedHashMap 无法转换为 原始对象，这里需要特殊处理
     *
     * @param rpcResponse
     * @return
     * @throws IOException
     */
    private RpcResponse handleSerializeResponse(RpcResponse rpcResponse) throws IOException {
        Object result = rpcResponse.getResult();
        if (result!= null) {
            rpcResponse.setResult(OBJECT_MAPPER.writeValueAsString(result));
        }
        return rpcResponse;
    }
    /**
     * 由于Object 的原始对象会被擦除，导致反序列化时 LinkedHashMap 无法转换为 原始对象，这里需要特殊处理
     *
     * @param rpcResponse
     * @return
     * @throws IOException
     */
    private RpcResponse handleDeserializeResponse(RpcResponse rpcResponse) throws IOException {
        Object result = rpcResponse.getResult();
        if (result!= null) {
            result = OBJECT_MAPPER.readValue(result.toString(), rpcResponse.getReturnType());
            rpcResponse.setResult(result);
        }
        return rpcResponse;
    }
}


