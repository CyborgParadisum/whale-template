import java.util.function.Supplier;

public class Test {


    public static void main(String[] args) throws Exception {
        Server.run(() -> {
            try {
                Driver.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
