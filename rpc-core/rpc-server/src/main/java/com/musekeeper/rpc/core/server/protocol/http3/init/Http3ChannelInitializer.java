package com.musekeeper.rpc.core.server.protocol.http3.init;


import com.musekeeper.rpc.core.server.protocol.http3.handler.Http3ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.incubator.codec.http3.Http3ServerConnectionHandler;
import io.netty.incubator.codec.quic.QuicChannel;
import io.netty.incubator.codec.quic.QuicStreamChannel;

import java.util.Map;

/**
 * @author musekeeper
 * http3服务端初始化器
 */
public class Http3ChannelInitializer extends ChannelInitializer<QuicChannel> {
    /**
     * 缓存服务实例
     */
    private final Map<String, Object> SERVICE_INSTANCE_MAP;

    public Http3ChannelInitializer(Map<String, Object> serviceInstanceMap) {
        SERVICE_INSTANCE_MAP = serviceInstanceMap;
    }

    /**
     * 初始化QuicChannel
     * @param ch QuicChannel
     */
    @Override
    protected void initChannel(QuicChannel ch) {
        ch.pipeline().addLast(new Http3ServerConnectionHandler(
                new ChannelInitializer<QuicStreamChannel>() {
                    @Override
                    protected void initChannel(QuicStreamChannel quicStreamChannel) {
                        quicStreamChannel.pipeline().addLast(new Http3ServerHandler(SERVICE_INSTANCE_MAP));
                    }
                }));
    }
}
