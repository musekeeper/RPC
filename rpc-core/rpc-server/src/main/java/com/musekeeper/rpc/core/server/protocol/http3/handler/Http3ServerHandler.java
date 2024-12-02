package com.musekeeper.rpc.core.server.protocol.http3.handler;


import com.musekeeper.rpc.core.common.enums.serialization.SerializationEnum;
import com.musekeeper.rpc.core.common.req.RpcRequest;
import com.musekeeper.rpc.core.common.res.RpcResponse;
import com.musekeeper.rpc.core.common.serialization.Serializer;
import com.musekeeper.rpc.core.common.serialization.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.incubator.codec.http3.*;

import io.netty.incubator.codec.quic.QuicStreamChannel;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * @author musekeeper
 * http3服务端处理器
 */
@Log4j2
public class Http3ServerHandler extends Http3RequestStreamInboundHandler {

    /**
     * 缓存服务实例
     */
    private final Map<String, Object> SERVICE_INSTANCE_MAP;
    /**
     * 序列化类型
     */
    private SerializationEnum serializationEnum;
    /**
     * 序列化器
     */
    private Serializer serializer;

    public Http3ServerHandler(Map<String, Object> serviceInstanceMap) {

        SERVICE_INSTANCE_MAP = serviceInstanceMap;
    }

    /**
     * 处理请求
     * @param ctx 管道上下文
     * @param headersFrame Http3头帧
     */
    @Override
    protected void channelRead(ChannelHandlerContext ctx, Http3HeadersFrame headersFrame) {
        serializationEnum = SerializationEnum.valueOf(headersFrame.headers().get("serialization").toString());
        serializer = SerializerFactory.getSerializer(serializationEnum);
    }

    /**
     * 处理请求数据
     * @param ctx 管道上下文
     * @param dataFrame Http3数据帧
     * @throws Exception 异常
     */
    @Override
    protected void channelRead(ChannelHandlerContext ctx, Http3DataFrame dataFrame) throws Exception {
        RpcResponse rpcResponse = new RpcResponse();
        ByteBuf resultBytes;
        try {
            //成员变量
            RpcRequest rpcRequest;
            rpcResponse = new RpcResponse();
            // 读取请求数据
            ByteBuf content = dataFrame.content();
            // 获取 ByteBuf 的字节数组
            byte[] byteArray = new byte[content.readableBytes()];
            content.readBytes(byteArray);

            //反序列化请求数据
            rpcRequest = serializer.deserialize(byteArray, RpcRequest.class);

            rpcResponse.setRequestId(rpcRequest.getRequestId());
            rpcResponse.setReturnType(rpcRequest.getReturnType());


//            //模拟异常
//            int i = 1 / 0;


            Object handlerResult = handler(rpcRequest);
            // 处理请求
            rpcResponse.setResult(handlerResult);
            //序列化响应数据

        } catch (Exception e) {
            rpcResponse.setException(e.getMessage());
        } finally {

            // 序列化RpcResponse
            byte[] result = serializer.serialize(rpcResponse);

            resultBytes = Unpooled.wrappedBuffer(result);
            // 释放资源
            dataFrame.release();
            // 发送响应数据
            sendResponse(ctx, resultBytes);

        }

    }

    @Override
    protected void channelInputClosed(ChannelHandlerContext ctx) throws Exception {

    }

    /**
     * 发送响应数据
     * @param ctx  ChannelHandlerContext
     * @param payload 响应数据
     * @throws InterruptedException 异常
     */
    private void sendResponse(ChannelHandlerContext ctx, ByteBuf payload) throws InterruptedException {
        DefaultHttp3HeadersFrame headersFrame = new DefaultHttp3HeadersFrame();
        headersFrame.headers().add("serialization", serializationEnum.name());
        headersFrame.headers().status("200");
        headersFrame.headers().add("content-length",String.valueOf(payload.readableBytes()));
        DefaultHttp3DataFrame defaultHttp3DataFrame = new DefaultHttp3DataFrame(payload);

        log.info("发送响应数据时间:{}", new Date());
        ctx.write(headersFrame);
        ctx.writeAndFlush(defaultHttp3DataFrame)
                .addListener(QuicStreamChannel.SHUTDOWN_OUTPUT).sync();
    }
    /**
     * 处理请求
     * @param  rpcRequest 请求数据
     * @return 处理结果
     */

    private Object handler(RpcRequest rpcRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(SERVICE_INSTANCE_MAP.isEmpty()){
            throw new RuntimeException("服务列表为空");
        }
        // 根据传过来的beanName从缓存中查找
        Object serviceBean = SERVICE_INSTANCE_MAP.get(rpcRequest.getClassName());
        // 判断是否存在
        if (serviceBean == null) {
            throw new RuntimeException("未找到服务");
        }
        //获取参数类型
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        //获取参数
        Object[] parameters = rpcRequest.getParameters();
        // 通过反射调用bean的方法
        return serviceBean.getClass().getMethod(rpcRequest.getMethodName(), parameterTypes).invoke(serviceBean, parameters);
    }
}
