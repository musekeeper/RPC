package com.musekeeper.rpc.core.client.protocol.http3.connection.factory;

import com.musekeeper.rpc.core.client.protocol.http3.connection.QuicStreamConnection;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.incubator.codec.http3.Http3;
import io.netty.incubator.codec.http3.Http3ClientConnectionHandler;
import io.netty.incubator.codec.quic.QuicChannel;
import io.netty.incubator.codec.quic.QuicSslContext;
import io.netty.incubator.codec.quic.QuicSslContextBuilder;


import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 创建连接工厂类,用于创建QuicStreamConnection对象
 */
public class QuicStreamConnectionFactory {
    /**
     * SSL配置
     */
    static QuicSslContext sslContext = createSslContext();


    /**
     * 创建QuicStreamConnection对象,用于连接到HTTP3服务器
     * @param serverAddress 服务地址
     * @return QuicStreamConnection 对象
     */
    public static QuicStreamConnection createChannel(InetSocketAddress serverAddress) {
        // 创建QuicStreamConnection对象,存储连接相关信息
        QuicStreamConnection quicStreamConnection = new QuicStreamConnection();
        // 创建EventLoopGroup
        quicStreamConnection.setGroup(new NioEventLoopGroup());
        // 创建HTTP3客户端编码器
        ChannelHandler codec = createCodec();
        try {
            // 创建Bootstrap以创建QUIC连接
            quicStreamConnection.setBootstrap(
                    new Bootstrap()
                    .group(quicStreamConnection.getGroup())
                    .channel(NioDatagramChannel.class)
                    .handler(codec));

            // 配置Bootstrap以创建数据通道并绑定到本地地址
            quicStreamConnection.setUdpChannel(
                    quicStreamConnection.getBootstrap()
                    .bind(0)
                    .sync().channel());

            // 使用指定的远程地址连接到HTTP3服务器
            quicStreamConnection.setQuicChannel(
                    QuicChannel.newBootstrap(quicStreamConnection.getUdpChannel())
                            .handler(new Http3ClientConnectionHandler())
                            .remoteAddress(serverAddress)
                            .connect()
                            .get());


            // 使用QUIC通道创建HTTP3请求流
            quicStreamConnection.setQuicStreamChannel(Http3
                            .newRequestStream(quicStreamConnection.getQuicChannel(), quicStreamConnection.getHttp3ClientHandler())
                            .sync()
                            .getNow());


        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return quicStreamConnection;
    }

    /**
     * 创建SSL配置
     * @return QuicSslContext 对象
     */
    static QuicSslContext createSslContext() {
        return QuicSslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .applicationProtocols(Http3.supportedApplicationProtocols()).build();
    }

    /**
     * 创建HTTP3编解码器
     * @return ChannelHandler 对象,用于HTTP3编解码器
     */
    static ChannelHandler createCodec() {
        return Http3.newQuicClientCodecBuilder()
                .sslContext(sslContext)
                .maxIdleTimeout(5000, TimeUnit.MILLISECONDS)
                .initialMaxData(10000000)
                .initialMaxStreamDataBidirectionalLocal(1000000)
                .build();
    }



}
