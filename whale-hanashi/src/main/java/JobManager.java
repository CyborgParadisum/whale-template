import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;
import network.Server;
import network.socket.SocketServer;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Builder
@Log
public class JobManager {
    Map<String, String> config;
    //    private int port = 414;
    Server server;
    VoidSupplier afterStarted;

//    private ServerSocket serverSocket;
//    private Socket socket;

    @SneakyThrows
    public void start() {
        server = new SocketServer(config);
        //        int port = Integer.parseInt(config.get("port"));
//        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    public void over(Result result) {
        server.send(result);
        log.info("over");
        //        val output = new ObjectOutputStream(socket.getOutputStream());
//        output.writeObject(result);
//        output.close();
    }

    @SneakyThrows
    public void run() {
        start();
        log.info("server start ");
//        afterStarted.apply();
        //        socket = serverSocket.accept();
        log.info("server accept");
        val input = server.receive();
        log.info("wait input");
        val in = new ObjectInputStream(input);
        Plan plan = (Plan) in.readObject();
        Plan.MyFunc<Integer, Boolean> func = plan.getFunc();
        val result = new Result();
        log.info("calc");
        List<Integer> sushu = new ArrayList<>();
        for (int i = plan.start; i <= plan.end; i++) {
            if (func.apply(i)) {
                sushu.add(i);
            }
        }
        result.setResult(sushu);
        over(result);
    }

    public void close() {
        server.close();
    }
}
