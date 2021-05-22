package com.whale;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;
import com.whale.network.demo.socket.SocketServer;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log
public class Worker {
    Map<String, String> config;
    SocketServer socketServer;
    List<Socket> socketClientList;

    public Worker(Map config) {
        this.config = config;
    }

    @SneakyThrows
    public void start() {
        socketServer = new SocketServer(config);
//        socketServer.init();
        while (true) {
            socketServer.accept();
            processWorker();
        }
    }

    @SneakyThrows
    public void processWorker() {
        log.info("server accept");
        log.info("wait input");
        val input = socketServer.receive();
        val in = new ObjectInputStream(input);
        Plan plan = (Plan) in.readObject();


        Plan.MyFunc<Integer, Boolean> func = plan.getFunc();
        log.info("calc");
        List<Integer> sushu = new ArrayList<>();
        for (int i = plan.start; i <= plan.end; i++) {
            if (func.apply(i)) {
                sushu.add(i);
            }
        }
//        String host = "localhost";
//        int port;
//        Socket socket1 = new Socket(host, 9001);
//        send(socket1, plan1);
//        Socket socket2 = new Socket(host, 9002);
//        send(socket2, plan2);
//        val in1 = receive(socket1);
//        val in2 = receive(socket2);
//        socketClientList
//
        val result = new Result();
        result.setResult(sushu);
        socketServer.send(result);
    }


}
