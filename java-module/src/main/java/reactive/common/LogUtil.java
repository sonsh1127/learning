package reactive.common;

public class LogUtil {

    public static void log(Object label) {
        System.out.println(System.currentTimeMillis() + "\t| " + Thread.currentThread().getName() + "\t| " + label);
    }

}
