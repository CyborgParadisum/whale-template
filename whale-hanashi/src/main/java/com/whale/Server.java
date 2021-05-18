package com.whale;

import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Log
public class Server {
    Map<String, String> config;
    private int port = 414;

    VoidSupplier afterStarted;

    private ServerSocket serverSocket;
    private Socket socket;

    @SneakyThrows
    public void start() {
        int port = Integer.parseInt(config.get("port"));
        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    public void over(Result result) {
        val output = new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(result);
        output.close();
    }

    public void run() throws Exception {
        start();
        log.info("server start ");
        afterStarted.apply();
        socket = serverSocket.accept();
        log.info("server accept");
        val input = socket.getInputStream();
//        byte[] sizeB = new byte[4];
//        input.read(sizeB);
//        int size = ByteBuffer.wrap(sizeB).getInt();
//        System.out.println("size " + size);
//        byte[] buffer = new byte[size];
//        int n;
//        while ((n = input.read(buffer)) != -1) {
//            System.out.println("read " + n + " bytes.");
//        }
//        System.out.println(buffer);

//        ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
        log.info("wait input");
        val in = new ObjectInputStream(input);
        Plan plan = (Plan) in.readObject();
        Plan.MyFunc<Integer, Boolean> func = plan.getFunc();
        val result = new Result();
        List<Integer> sushu = new ArrayList<>();
        result.setResult(sushu);
        for (int i = plan.start; i <= plan.end; i++) {
            if (func.apply(i)) {
                sushu.add(i);
            }
        }
        over(result);
    }

    public static void main(String[] args) throws Exception {

    }
}
