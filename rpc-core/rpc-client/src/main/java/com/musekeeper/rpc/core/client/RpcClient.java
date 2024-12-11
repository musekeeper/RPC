package com.musekeeper.rpc.core.client;

import com.musekeeper.rpc.core.client.protocol.http3.connection.QuicStreamConnection;

import com.musekeeper.rpc.core.client.protocol.http3.handler.Http3ClientHandler;

import com.musekeeper.rpc.core.common.enums.serialization.SerializationEnum;
import io.netty.buffer.Unpooled;
import io.netty.incubator.codec.http3.*;
import io.netty.incubator.codec.quic.QuicChannel;
import io.netty.incubator.codec.quic.QuicStreamChannel;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Objects;

/**
 * @author musekeeper
 * RPC客户端
 */
@Log4j2
public class RpcClient {
    /**
     * 静态方法，发送消息
     * @param msg 消息
     * @param ServerAddress 服务地址
     * @param serializationEnum 序列化类型
     * @return 响应
     */
    public static byte[] send(byte[] msg, InetSocketAddress ServerAddress, SerializationEnum serializationEnum) {

        try (QuicStreamConnection quicStreamConnection = QuicStreamConnection.getConnection(ServerAddress)){
            // 获取QuicStreamChannel
            QuicStreamChannel streamChannel = quicStreamConnection.getQuicStreamChannel();
            // 获取QuicChannel
            QuicChannel quicChannel = quicStreamConnection.getQuicChannel();
            // 获取Http3ClientHandler
            Http3ClientHandler http3ClientHandler = quicStreamConnection.getHttp3ClientHandler();
            // 设置请求头
            Http3HeadersFrame headersFrame = new DefaultHttp3HeadersFrame();

            // 设置请求头
            headersFrame.headers().method("GET")
                    .path("/")
                    .scheme("https")
                    .authority(Objects.requireNonNull(quicChannel.remoteAddress()).toString())
                    .add("serialization", serializationEnum.name());

            DefaultHttp3DataFrame dataFrame = new DefaultHttp3DataFrame(Unpooled.wrappedBuffer(msg));
            log.info("发送请求时间：{}", new Date());
            // 发送请求头
            streamChannel.write(headersFrame);

            // 发送请求数据
            streamChannel.writeAndFlush(dataFrame);


            // 等待响应
            byte[] result = http3ClientHandler.getResult();
            log.info("收到响应时间：{}", new Date());
            return result;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
