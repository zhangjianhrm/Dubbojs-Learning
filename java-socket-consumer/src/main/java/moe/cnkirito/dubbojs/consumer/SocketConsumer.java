package moe.cnkirito.dubbojs.consumer;

import sun.jvm.hotspot.runtime.Bytes;

import java.net.Socket;

/**
 * @author 徐靖峰[OF2938]
 * company qianmi.com
 * Date 2018-05-12
 */
public class SocketConsumer {

    public static final short MAGIC = (short) 0xdabb;
    public static final byte MAGIC_HIGH = short2bytes(MAGIC)[0];
    public static final byte MAGIC_LOW = short2bytes(MAGIC)[1];

    //header length
    public static final int DUBBO_HEADER_LENGTH = 16;

    //com.alibaba.dubbo.common.serialize.support.hessian.Hessian2Serialization
    public static final byte HESSIAN2_SERIALIZATION_CONTENT_ID = 2;

    //dubbo最大的body序列化数据的大小
    //com.alibaba.dubbo.common.Constants.DEAULT_PAY_LOAD
    public static final int DEFAULT_PAYLOAD = 8 * 1024 * 1024;

    protected static final byte FLAG_REQUEST = (byte) 0x80;
    protected static final byte FLAG_TWOWAY = (byte) 0x40;
    protected static final byte FLAG_EVENT = (byte) 0x20;
    protected static final int SERIALIZATION_MASK = 0x1f;


    public static byte[] short2bytes(short v) {
        byte[] ret = {0, 0};
        ret[1] = (byte) v;
        ret[0] = (byte) (v >>> 8);
        return ret;
    }

    public static void main(String[] args) {
    }

}
