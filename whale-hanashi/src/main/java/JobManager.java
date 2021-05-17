import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;
import network.socket.SocketAtomic;
import network.socket.SocketServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

//@Builder
@Log
public class JobManager {
    Map<String, String> config;
    //    Server server;

    //    VoidSupplier afterStarted;
    // https://zh.wikipedia.org/wiki/Volatile%E5%8F%98%E9%87%8F
    volatile boolean isOpen = false;
    SocketAtomic socketAtomic;
    SocketServer socketServer;
    List<Socket> socketClientList;

    public JobManager(Map config) {
        this.config = config;
    }

    @SneakyThrows
    public void start() {
        socketAtomic = new SocketAtomic(config);
        socketAtomic.startServer();
        socketServer = new SocketServer(config);
        socketServer.init();
        isOpen = true;
    }

    @SneakyThrows
    public void processWorker() {
        log.info("server accept");
//        val input = server.receive();
        log.info("wait input");
        val input = socketServer.receive();
        val in = new ObjectInputStream(input);
        Plan plan = (Plan) in.readObject();
//        Plan.MyFunc<Integer, Boolean> func = plan.getFunc();
//        val result = new Result();
//        log.info("calc");
//        List<Integer> sushu = new ArrayList<>();
//        for (int i = plan.start; i <= plan.end; i++) {
//            if (func.apply(i)) {
//                sushu.add(i);
//            }
//        }

        int end = plan.end / 2;
        Plan plan1 = new Plan(plan.start, end, plan.func);
        Plan plan2 = new Plan(end + 1, plan.end, plan.func);

        String host = "localhost";
        int port;
        Socket socket1 = new Socket(host, 9001);
        send(socket1, plan1);
        Socket socket2 = new Socket(host, 9002);
        send(socket2, plan2);
        val in1 = receive(socket1);

        val in2 = receive(socket2);
        val res1 = (Result) new ObjectInputStream(in1).readObject();
        val res2 = (Result) new ObjectInputStream(in2).readObject();
        List<Integer> sushu = new ArrayList<>();
        sushu.addAll((List<Integer>) res1.getResult());
        sushu.addAll((List<Integer>) res2.getResult());
        val result = new Result();
        result.setResult(sushu);
        socketServer.send(result);
    }

    @SneakyThrows
    public void run() {
        start();
        log.info("server start ");
//        afterStarted.apply();
        while (isOpen) {
//            val socket = socketAtomic.accept();
            socketServer.accept();
            processWorker();
        }
    }

    @SneakyThrows
    public void over(Result result) {
        log.info("over");
        socketServer.send(result);
    }

    public void close() {
        isOpen = false;
//        server.close();
    }

    public void send(Socket socket, Object object) throws IOException {
        val output = new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(object);
        output.flush();
    }

    public InputStream receive(Socket socket) throws IOException {
        return socket.getInputStream();
    }
}
