
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Completable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> name = CompletableFuture
                .completedFuture("adb")
                .thenApply(msg -> first(msg))
                .thenApply(msg -> second(msg))
                .thenApply(msg -> third(msg));

        System.out.println(name.get());
    }

    public static String first(String arg) {
        return arg + "first";
    }
    public static String second(String arg) {
        return arg + " second";
    }
    public static String third(String arg) {
        return arg + " thrird";
    }

    private static void callbackHell() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(() -> {
            firstTask();
            executorService.submit(() -> {
                secondTask();
                return executorService.submit(() -> {
                    thridTask();
                });
            });
        });
    }

    static Object firstTask() {
        return null;
    }

    static void secondTask() {

    }

    static String thridTask() {
        return "";
    }
}
