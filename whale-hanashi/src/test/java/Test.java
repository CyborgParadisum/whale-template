import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import lombok.val;

public class Test {

    public static void main(String[] args) throws Exception {


    }
}

class TestJobManager {
    public static void main(String[] args) {
        JobManager.builder()
            .config(ImmutableMap.of(
                "port", "414"))
//            .afterStarted(() -> {
//                new Thread() {
//                    @SneakyThrows
//                    @Override
//                    public void run() {
//                        driver.run();
//                    }
//                }.start();
//            })
            .build()
            .run();
    }
}

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
