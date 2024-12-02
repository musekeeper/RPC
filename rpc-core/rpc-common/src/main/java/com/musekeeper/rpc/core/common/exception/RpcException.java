package com.musekeeper.rpc.core.common.exception;

/**
 * @author musekeeper
 * RpcException
 */
public class RpcException extends RuntimeException {
    public RpcException(String message) {
        super(message);
    }
    public RpcException(Throwable cause) {
        super(cause);
    }
    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
