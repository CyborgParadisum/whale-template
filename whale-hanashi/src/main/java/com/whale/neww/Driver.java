package com.whale.neww;

import com.whale.Plan;
import com.whale.Result;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class Driver {

    /**
     * executors : 127.0.0.1:45667,sucicada.cf:4565
     */
    Map<String, String> config;
    List<Runnable> tasks = new ArrayList<>();
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void send(Socket socket, Object object) throws IOException {
        val output = new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(object);
        output.flush();
    }

    public InputStream receive(Socket socket) throws IOException {
        return socket.getInputStream();
    }

    @SneakyThrows
    public Result submit(Plan plan) {

        List<Pair<String, Integer>> addressList = Arrays.stream(config.get("executors")
            .split(","))
            .map(a -> {
                val aa = a.split(":");
                return new Pair<>(aa[0], Integer.parseInt(aa[1]));
            })
            .collect(Collectors.toList());

        int executorNum = addressList.size();
        int partition = (plan.end - plan.start) / executorNum;
        List<Future<List<Integer>>> futures = new ArrayList<>();

        for (int i = 0; i < executorNum; i++) {
            Pair<String, Integer> address = addressList.get(i);
            int start = plan.start + i * partition;
            int end = i + 1 == executorNum ? plan.end : start + partition;

            val callable = new Callable<List<Integer>>() {
                @Override
                public List<Integer> call() throws Exception {
                    Plan plan1 = new Plan(start, end, plan.func);
                    Socket socket1 = new Socket(address.getKey(), address.getValue());

                    send(socket1, plan1);

                    val in1 = receive(socket1);
                    val res = (Result) new ObjectInputStream(in1).readObject();
                    return (List<Integer>) res.getResult();
                }

            };

            val future = executorService.submit(callable);
            futures.add(future);
//            tasks.add(thread);

        }
        List<Integer> res = new ArrayList<>();
        for (val future : futures) {
            res.addAll(future.get());
        }
        val result = new Result();
        result.setResult(res);
        return result;
    }

    public static void main(String[] args) {

    }
}


