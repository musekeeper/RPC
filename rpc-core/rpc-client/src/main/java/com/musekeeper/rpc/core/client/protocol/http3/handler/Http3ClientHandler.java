package com.musekeeper.rpc.core.client.protocol.http3.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.incubator.codec.http3.Http3DataFrame;
import io.netty.incubator.codec.http3.Http3HeadersFrame;
import io.netty.incubator.codec.http3.Http3RequestStreamInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author musekeeper
 * http3客户端处理器
 */
public class Http3ClientHandler extends Http3RequestStreamInboundHandler {

    /**
     * 等待响应结果
     */
    private final CountDownLatch latch = new CountDownLatch(1);
    /**
     * 响应结果
     */
    private byte[] result;



    @Override
    protected void channelRead(ChannelHandlerContext channelHandlerContext, Http3HeadersFrame http3HeadersFrame)  {
        ReferenceCountUtil.release(http3HeadersFrame);
    }

    @Override
    protected void channelRead(ChannelHandlerContext channelHandlerContext, Http3DataFrame http3DataFrame) throws Exception {
        //反序列化
        ByteBuf content = http3DataFrame.content();
        result = new byte[content.readableBytes()];
        content.readBytes(result);
        latch.countDown();
        ReferenceCountUtil.release(http3DataFrame);
    }

    public byte[] getResult() throws InterruptedException {
        if(result!= null){
            return result;
        }else {
            latch.await(2, TimeUnit.SECONDS);
            if(result == null){
                throw new InterruptedException("rpc response timeout");
            }
            return result;
        }
    }
    @Override
    protected void channelInputClosed(ChannelHandlerContext channelHandlerContext) throws Exception {

    }
}
