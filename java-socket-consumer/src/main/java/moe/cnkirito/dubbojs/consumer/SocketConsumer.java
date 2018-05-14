package moe.cnkirito.dubbojs.consumer;

import com.alibaba.dubbo.demo.UserRequest;
import com.alibaba.dubbo.demo.UserResponse;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import sun.jvm.hotspot.runtime.Bytes;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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

    public static void long2bytes(long v, byte[] b, int off) {
        b[off + 7] = (byte) v;
        b[off + 6] = (byte) (v >>> 8);
        b[off + 5] = (byte) (v >>> 16);
        b[off + 4] = (byte) (v >>> 24);
        b[off + 3] = (byte) (v >>> 32);
        b[off + 2] = (byte) (v >>> 40);
        b[off + 1] = (byte) (v >>> 48);
        b[off + 0] = (byte) (v >>> 56);
    }

    public static void int2bytes(int v, byte[] b, int off) {
        b[off + 3] = (byte) v;
        b[off + 2] = (byte) (v >>> 8);
        b[off + 1] = (byte) (v >>> 16);
        b[off + 0] = (byte) (v >>> 24);
    }

    public static int bytes2int(byte[] b, int off) {
        return ((b[off + 3] & 0xFF) << 0) +
                ((b[off + 2] & 0xFF) << 8) +
                ((b[off + 1] & 0xFF) << 16) +
                ((b[off + 0]) << 24);
    }

    public static void main(String[] args) {
        try {
            byte[] header = new byte[DUBBO_HEADER_LENGTH];
            // 设置 magic number 高位
            header[0] = MAGIC_HIGH;
            // 设置 magic number 低位
            header[1] = MAGIC_LOW;
            // 设置 flag 标志位
            header[2] = FLAG_REQUEST | HESSIAN2_SERIALIZATION_CONTENT_ID | FLAG_TWOWAY;
            long requestId = 123456L;
            // 设置 requestId
            long2bytes(requestId,header,4);

            RpcInvocation inv = initInvocation();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Hessian2Output out = new Hessian2Output(bos);
            out.writeString(inv.getAttachments().get("dubbo"));
            out.writeString(inv.getAttachments().get("path"));
            out.writeString(inv.getAttachments().get("version"));

            out.writeString(inv.getMethodName());
            out.writeString("Lcom/alibaba/dubbo/demo/UserRequest;");
            Object[] invArguments = inv.getArguments();
            if (invArguments != null)
                for (int i = 0; i < invArguments.length; i++) {
                    out.writeObject(invArguments[i]);
                }
            out.writeObject(inv.getAttachments());
            out.flush();
            byte[] body = bos.toByteArray();
            int2bytes(body.length, header, 12);

            Socket client = new Socket("127.0.0.1",20880);
            OutputStream outputStream = client.getOutputStream();
            outputStream.write(header);
            outputStream.write(body);
            InputStream inputStream = client.getInputStream();

            byte[] respHeader = new byte[DUBBO_HEADER_LENGTH];
            inputStream.read(respHeader);
            int lenth = bytes2int(respHeader, 12);
            byte[] respBody = new byte[lenth];
            inputStream.read(respBody);
            inputStream.close();
            outputStream.close();
            client.close();


            ByteArrayInputStream bis = new ByteArrayInputStream(respBody);
            Hessian2Input in = new Hessian2Input(bis);
            int flag = in.readInt();
            UserResponse userResponse = (UserResponse) in.readObject(UserResponse.class);
            System.out.println(userResponse);
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static RpcInvocation initInvocation() {
        RpcInvocation rpcInvocation = new RpcInvocation();
        Map<String,String> attachments = new HashMap<String, String>();
        attachments.put("dubbo","2.5.7");
        attachments.put("path","com.alibaba.dubbo.demo.DemoProvider" );
        attachments.put("version","1.0.0");
        rpcInvocation.setAttachments(attachments);
        rpcInvocation.setMethodName("getUserInfo");
        rpcInvocation.setParameterTypes(new Class[]{UserRequest.class});
        UserRequest request = new UserRequest();
        request.setId(1);
        request.setEmail("test@qianimi.com");
        request.setName("node-dubbo");
        rpcInvocation.setArguments(new Object[]{request});
        return rpcInvocation;
    }

}
