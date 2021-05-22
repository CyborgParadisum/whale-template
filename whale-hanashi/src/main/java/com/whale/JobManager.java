package com.whale;

import javafx.util.Pair;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;
import com.whale.network.demo.socket.SocketAtomic;
import com.whale.network.demo.socket.SocketServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

//@Builder
@Log
public class JobManager {
    Map<String, String> config;
    //    com.whale.Server server;

    //    com.whale.VoidSupplier afterStarted;
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
//        socketAtomic = new SocketAtomic(config);
//        socketAtomic.startServer();
        socketServer = new SocketServer(config);
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
//        com.whale.Plan.MyFunc<Integer, Boolean> func = plan.getFunc();
//        val result = new com.whale.Result();
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


        List<Pair<String, Integer>> address = Arrays.stream(config.get("workers")
            .split(","))
            .map(a -> {
                val aa = a.split(":");
                return new Pair<>(aa[0], Integer.parseInt(aa[1]));
            })
            .collect(Collectors.toList());
        Socket socket1 = new Socket(address.get(0).getKey(), address.get(0).getValue());
        send(socket1, plan1);
        Socket socket2 = new Socket(address.get(1).getKey(), address.get(1).getValue());
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
