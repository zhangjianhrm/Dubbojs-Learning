package moe.cnkirito.dubbojs.consumer;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

/**
 * RPC Invocation.
 *
 * @author qian.lei
 * @serial Don't change the class name and properties.
 */
@Data
public class RpcInvocation implements Serializable {

    private static final long serialVersionUID = -4355285085441097045L;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    private Map<String, String> attachments;


    @Override
    public String toString() {
        return "RpcInvocation [methodName=" + methodName + ", parameterTypes="
                + Arrays.toString(parameterTypes) + ", arguments=" + Arrays.toString(arguments)
                + ", attachments=" + attachments + "]";
    }

}