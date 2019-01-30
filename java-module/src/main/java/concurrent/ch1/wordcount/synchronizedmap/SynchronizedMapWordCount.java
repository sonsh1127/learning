package concurrent.ch1.wordcount.synchronizedmap;

import concurrent.ch1.wordcount.Doc;
import concurrent.ch1.wordcount.PoisonPill;
import concurrent.ch1.wordcount.produceconsume.Parser;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SynchronizedMapWordCount {

    private static int NUM_COUNTERS = 4;

    public static void main(String[] args) throws InterruptedException {

        ArrayBlockingQueue<Doc> queue = new ArrayBlockingQueue<>(100);
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < NUM_COUNTERS; ++i)
            executor.execute(new SharedCounter(queue, counts));
        Thread parser = new Thread(new Parser(queue));
        // END:mainloop
        long start = System.currentTimeMillis();
        // START:mainloop
        parser.start();
        parser.join();
        for (int i = 0; i < NUM_COUNTERS; ++i)
            queue.put(new PoisonPill());
        executor.shutdown();
        executor.awaitTermination(10L, TimeUnit.MINUTES);
        // END:mainloop
        long end = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (end - start) + "ms");
    }

}
