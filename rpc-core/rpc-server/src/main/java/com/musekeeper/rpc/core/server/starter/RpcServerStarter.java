package com.musekeeper.rpc.core.server.starter;

import com.musekeeper.rpc.core.server.protocol.http3.init.Http3ChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.ssl.*;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.incubator.codec.http3.Http3;
import io.netty.incubator.codec.quic.InsecureQuicTokenHandler;
import io.netty.incubator.codec.quic.QuicSslContext;
import io.netty.incubator.codec.quic.QuicSslContextBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author musekeeper
 * 服务启动器
 */
@Log4j2
public class RpcServerStarter {


    /**
     * 缓存服务实例
     */
    private final static Map<String, Object> SERVICE_INSTANCE_MAP = new ConcurrentHashMap<>();

    public Map<String, Object> getServiceInstanceMap() {
        return SERVICE_INSTANCE_MAP;
    }

    /**
     * 端口
     */
    @Getter
    private final Integer port;
    /**
     * 地址
     */
    @Getter
    private final String host;
    /**
     * 事件循环组
     */
    EventLoopGroup group = new NioEventLoopGroup();


    private QuicSslContext sslCtx;

    /**
     * 构造方法
     * @param host 地址
     * @param port 端口
     */
    public RpcServerStarter(String host, Integer port) {

        this.port = port;
        this.host = host;
        try {
            sslCtx = createSSLContext();
        } catch (SSLException | CertificateException e) {
            log.error("create ssl context error", e);
        }
    }

    /**
     * 启动服务
     */
    public void start() {

        ChannelHandler codec = Http3.newQuicServerCodecBuilder()
                .sslContext(sslCtx)
                .maxIdleTimeout(5000, TimeUnit.MILLISECONDS)
                .initialMaxData(10000000)
                .initialMaxStreamDataBidirectionalLocal(1000000)
                .initialMaxStreamDataBidirectionalRemote(1000000)
                .initialMaxStreamsBidirectional(100)
                .initialMaxStreamsUnidirectional(100)
                .tokenHandler(InsecureQuicTokenHandler.INSTANCE)
                .handler(new Http3ChannelInitializer(SERVICE_INSTANCE_MAP))
                .build();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(codec);

            ChannelFuture channelFuture = bootstrap.bind(host, port).sync();

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            stop();
        }


    }

    /**
     * 停止服务
     */
    public void stop() {
        if (group!= null) {
            group.shutdownGracefully();
        }
    }

    /**
     * 创建SSLContext
     * @return SSL
     * @throws SSLException   SSL异常
     * @throws CertificateException 证书异常
     */
    private QuicSslContext createSSLContext() throws SSLException, CertificateException {
        SslProvider provider =  SslProvider.isAlpnSupported(SslProvider.OPENSSL)  ? SslProvider.OPENSSL : SslProvider.JDK;
        log.info("SslProvider: " + provider);
        SelfSignedCertificate ssc = new SelfSignedCertificate();
        return QuicSslContextBuilder
                .forServer(ssc.key(),null,ssc.cert())
                .applicationProtocols(Http3.supportedApplicationProtocols()).build();


    }
}
