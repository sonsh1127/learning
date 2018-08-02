package concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadUnSafeCode {


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        final StringBuilder sb = new StringBuilder();
        Future<?> aResult = executorService.submit(() -> {
            for (int i = 0; i < 100; i++) {
                sb.append("AAA");
                TimeUnit.MILLISECONDS.sleep(1);
            }
            return "A";
        });

        Future<?> bResult = executorService.submit(() -> {
            for (int i = 0; i < 100; i++) {
                sb.append("BBB");
                TimeUnit.MILLISECONDS.sleep(1);
            }
            return "A";
        });

        aResult.get();
        bResult.get();
        String result = sb.toString();

        for (int i =0; i < result.length() ; i+= 3){
            String part =  result.substring(i, i+3);
            if (!"AAA".equals(part) && !"BBB".endsWith(part)){
                System.out.println(part + " " + i + " th part is invalid");
            }
        }
        System.out.println(result);
        executorService.shutdown();

    }

}
