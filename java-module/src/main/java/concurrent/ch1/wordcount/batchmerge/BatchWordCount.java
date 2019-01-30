package concurrent.ch1.wordcount.batchmerge;

import concurrent.ch1.wordcount.Doc;
import concurrent.ch1.wordcount.PoisonPill;
import concurrent.ch1.wordcount.produceconsume.Parser;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BatchWordCount {
    private static final int NUM_COUNTERS = 4;

    public static void main(String[] args) throws InterruptedException {

        ArrayBlockingQueue<Doc> queue = new ArrayBlockingQueue<>(1024);
        ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newCachedThreadPool();

        for (int i = 0; i < NUM_COUNTERS; ++i)
            executor.execute(new BatchCounter(queue, counts));
        Thread parser = new Thread(new Parser(queue));
        long start = System.currentTimeMillis();
        parser.start();
        parser.join();
        for (int i = 0; i < NUM_COUNTERS; ++i)
            queue.put(new PoisonPill());
        executor.shutdown();
        executor.awaitTermination(10L, TimeUnit.MINUTES);
        long end = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (end - start) + "ms");
        System.out.println(counts.size());
    }

}
