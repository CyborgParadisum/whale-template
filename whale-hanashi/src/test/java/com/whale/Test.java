package com.whale;

import com.google.common.collect.ImmutableMap;
import com.whale.Driver;
import com.whale.JobManager;
import com.whale.Plan;
import com.whale.Worker;
import lombok.val;

class TestDriver {
    public static Driver createDriver() {
        Plan.MyFunc<Integer, Boolean> func = (Integer n) -> {
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
        String port = "414";

        val config = ImmutableMap.of(
            "host", host,
            "port", port);
        val plan = Plan.builder()
            .func(func)
            .setRange(1, 100)
            .build();

        val driver = Driver.builder()
            .config(config)
            .plan(plan)
            .build();

        return driver;
    }

    public static void main(String[] args) {
        val a = createDriver();
        a.run();
    }
}
class TestJobManager {
    public static void main(String[] args) {
        new JobManager(ImmutableMap.of(
            "port", "414",
            "workers","localhost:7001,localhost:7002"))
            .run();
    }
}

class TestWork1 {
    public static void main(String[] args) {
        new Worker(ImmutableMap.of(
            "port", "7001"))
            .start();
    }
}

class TestWork2 {
    public static void main(String[] args) {
        new Worker(ImmutableMap.of(
            "port", "7002"))
            .start();
    }
}
