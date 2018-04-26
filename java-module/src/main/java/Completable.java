package main.java;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Completable {


    public static void main(String[] args) throws Exception{

        CompletableFuture<String> name = CompletableFuture
                .completedFuture("adb")
                .thenApply(msg -> first(msg));
        System.out.println(name);
    }

    public static String first(String arg) {
        return arg + "first";
    }

    public static String second(String arg) {
        return "";
    }

    private static void callbackHell() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.submit(() -> {
            firstTask();
            executorService.submit(() -> {
                secondTask();
                executorService.submit(() -> {
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

    static void thridTask() {
    }
}
