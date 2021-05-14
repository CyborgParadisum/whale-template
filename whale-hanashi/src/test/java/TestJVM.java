

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.lang.management.ManagementFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestJVM {
    public static void main(String[] args) throws Exception {
        String javaHome = System.getProperty("java.home");
        String javaBin = Joiner.on(File.separator)
            .join(Lists.newArrayList(javaHome, "bin", "java"));
        String classpath = System.getProperty("java.class.path");
        classpath = classpath.concat("./src/test/resources");
        Class clazz = TargetMain.class;
        String className = clazz.getCanonicalName();
        ProcessBuilder builder =
            new ProcessBuilder(javaBin, "-cp", classpath, className);
        builder.inheritIO();
        Process process = null;
        process = builder.start();
        System.out.println("over");
        Thread.sleep(5000);
    }
}

class TargetMain {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("hello");
        Thread.sleep(10000);
    }
}
