package com.musekeeper.rpc.core.client.protocol.http3.connection;

import com.musekeeper.rpc.core.client.protocol.http3.handler.Http3ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.incubator.codec.quic.QuicChannel;
import io.netty.incubator.codec.quic.QuicStreamChannel;
import lombok.Data;

/**
 * @author musekeeper
 * 存储连接相关信息
 */
@Data
public class QuicStreamConnection {
    /**
     * Http3连接处理器
     */
    Http3ClientHandler http3ClientHandler = new Http3ClientHandler();
    /**
     * 事件循环组
     */
    NioEventLoopGroup group;
    /**
     * 启动引导
     */
    Bootstrap bootstrap;
    /**
     * UDP 通道
     */
    Channel udpChannel;
    /**
     * QUIC 连接
     */
    QuicChannel quicChannel;
    /**
     * QUIC 流
     */
    QuicStreamChannel quicStreamChannel;

    /**
     * 关闭连接
     */
    public void close() {

        if (quicStreamChannel != null) {
            quicStreamChannel.close();
        }
        if (quicChannel!= null) {
            quicChannel.close();
        }
        if (udpChannel!= null) {
            udpChannel.close();
        }
        if (group!= null) {
            group.shutdownGracefully();
        }
    }
}
