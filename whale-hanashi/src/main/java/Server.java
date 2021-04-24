import lombok.val;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class Server {
    String host = "localhost";

    public static void main(String[] args) throws Exception {

    }

    public static void run(Supplier callback) throws Exception {
        int port = 414;
        ServerSocket serverSocket = new ServerSocket(port);
        callback.get();
        Socket socket = serverSocket.accept();
        val input = socket.getInputStream();
        byte[] sizeB = new byte[4];
        input.read(sizeB);
        int size = ByteBuffer.wrap(sizeB).getInt();
        System.out.println("size " + size);
        byte[] buffer = new byte[size];
        int n;
        while ((n = input.read(buffer)) != -1) {
            System.out.println("read " + n + " bytes.");
        }
        System.out.println(buffer);
        ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
        val in = new ObjectInputStream(bis);
        Object o = in.readObject();
        Driver.MyFunc<Integer, Boolean> func = (Driver.MyFunc<Integer, Boolean>) o;
        for (int i = 0; i < 100; i++) {
            val res = func.apply(i);
            if (res) {
                System.out.println(i);
            }

        }
    }
}
