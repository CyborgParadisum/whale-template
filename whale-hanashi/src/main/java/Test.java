import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;

import java.util.function.Supplier;

public class Test {
    public static void main(String[] args) throws Exception {
        Server.builder()
            .config(ImmutableMap.of(
                "port", "414"))
            .afterStarted(() -> {
                new Thread() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        Driver.main(null);
                    }
                }.start();

            })
            .build()
            .run();
    }
}
