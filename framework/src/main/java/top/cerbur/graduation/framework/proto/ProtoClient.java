package top.cerbur.graduation.framework.proto;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.Delegate;
import io.protostuff.runtime.RuntimeEnv;
import io.protostuff.runtime.RuntimeSchema;

import java.sql.Timestamp;

/**
 * @author cerbur
 */
public class ProtoClient<T> {

    private final Class<T> tClass;

    public ProtoClient(Class<T> tClass) {
        this.tClass = tClass;
    }

    private final static Delegate<Timestamp> TIMESTAMP_DELEGATE = new TimestampDelegate();
    private final static DefaultIdStrategy ID_STRATEGY = ((DefaultIdStrategy) RuntimeEnv.ID_STRATEGY);

    static {
        ID_STRATEGY.registerDelegate(TIMESTAMP_DELEGATE);
    }

    public byte[] getProtoBuff(T data) {
        Schema<T> schema = RuntimeSchema.getSchema(this.tClass);
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        // ser
        final byte[] protostuff;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(data, schema, buffer);
        } finally {
            buffer.clear();
        }
        return protostuff;
    }

    public T getModel(byte[] bytes) {
        Schema<T> schema = RuntimeSchema.getSchema(this.tClass);
        T data = schema.newMessage();
        if (bytes != null) {
            ProtostuffIOUtil.mergeFrom(bytes, data, schema);
        }
        return data;
    }
}
