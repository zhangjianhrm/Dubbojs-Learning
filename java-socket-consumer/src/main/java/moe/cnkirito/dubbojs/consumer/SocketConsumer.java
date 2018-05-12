package moe.cnkirito.dubbojs.consumer;

import sun.jvm.hotspot.runtime.Bytes;

import java.net.Socket;

/**
 * @author 徐靖峰[OF2938]
 * company qianmi.com
 * Date 2018-05-12
 */
public class SocketConsumer {

    protected static final short MAGIC = (short) 0xdabb;
    protected static final byte MAGIC_HIGH = short2bytes(MAGIC)[0];
    protected static final byte MAGIC_LOW = short2bytes(MAGIC)[1];

    public static byte[] short2bytes(short v) {
        byte[] ret = {0, 0};
        ret[1] = (byte) v;
        ret[0] = (byte) (v >>> 8);
        return ret;
    }

    public static void main(String[] args) {
    }

}
