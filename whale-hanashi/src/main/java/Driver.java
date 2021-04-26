import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author User
 */
@Builder
@Log
public class Driver {
    Map<String, String> config;
    static Plan.MyFunc<Integer, Boolean> func = (Integer n) -> {
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

    public void run() {
        val plan = Plan.builder()
            .func(func)
            .setRange(1, 100)
            .build();

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

    public static void main(String[] args) throws Exception {

        String host = "localhost";
        String port = "414";

        Map config = ImmutableMap.of("host", host,
            "port", port);
        Driver.builder()
            .config(config)
            .build()
            .run();
    }
}
