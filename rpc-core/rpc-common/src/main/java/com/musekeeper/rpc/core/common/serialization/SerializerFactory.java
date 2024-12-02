package com.musekeeper.rpc.core.common.serialization;

import com.musekeeper.rpc.core.common.enums.serialization.SerializationEnum;
import com.musekeeper.rpc.core.common.serialization.impl.*;

/**
 * @author musekeeper
 * 序列化工厂类
 */
public class SerializerFactory {
    public static Serializer getSerializer(SerializationEnum serializationEnum) {
        return switch (serializationEnum) {
            case JSON -> new JsonSerializer();
            case KRYO -> new KryoSerializer();
            case PROTOBUF -> new ProtostuffSerializer();
            case HESSIAN -> new HessianSerializer();
            default -> new JdkSerializer();
        };
    }
}
