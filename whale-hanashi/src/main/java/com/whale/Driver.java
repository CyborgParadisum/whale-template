package com.whale;

import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @author User
 */
@Builder
@Log
public class Driver {

    Map<String, String> config;
    Plan plan ;

    public static void main(String[] args) {
        // noop
    }

    public void run() {
        submit(plan);
    }

    public void submit(Plan plan) {
        val res = talk(plan);
        System.out.println("res " + res.getResult());
    }

    @SneakyThrows
    public Result talk(Object object) {
        String host = config.get("host");

        int port = Integer.parseInt(config.get("port"));

        Socket socket = new Socket(host, port);

        log.info("start");
        val output = socket.getOutputStream();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        val out = new ObjectOutputStream(output);
        out.writeObject(object);
        log.info("output");

        val input = new ObjectInputStream(socket.getInputStream());
        return (Result) input.readObject();
        //        byte[] funcB = bos.toByteArray();
//        int size = funcB.length;
//        byte[] sizeB = ByteBuffer.allocate(4).putInt(size).array();
//        System.out.println(size);
//        output.write(sizeB);
//        output.write(funcB);
//        output.close();
//        socket.getInputStream();
    }
}
