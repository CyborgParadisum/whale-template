import lombok.val;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * @author User
 */
public class Driver {

    interface MyFunc<T, R> extends Serializable, Function<T, R> {
    }

    public static void main(String[] args) throws Exception {
        MyFunc<Integer, Boolean> func = (Integer n) -> {
            if (n < 2) {
                return false;
            }
            for (int i = 2; i <= Math.sqrt(n); i++) {
                if (n % i == 0) {
                    return false;
                }
            }
            return true;
        };

        String host = "localhost";
        int port = 414;
        Socket socket = new Socket(host, port);
        val output = socket.getOutputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        val out = new ObjectOutputStream(bos);
        out.writeObject(func);
        byte[] funcB = bos.toByteArray();
        int size = funcB.length;
        byte[] sizeB = ByteBuffer.allocate(4).putInt(size).array();
        System.out.println(size);
        output.write(sizeB);
        output.write(funcB);
        output.close();
    }
}
