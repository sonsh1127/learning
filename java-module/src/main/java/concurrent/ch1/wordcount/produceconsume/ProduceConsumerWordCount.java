package concurrent.ch1.wordcount.produceconsume;

import concurrent.ch1.wordcount.Doc;
import concurrent.ch1.wordcount.PoisonPill;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProduceConsumerWordCount {

    public static void main(String[] args) throws InterruptedException {

        BlockingQueue<Doc> queue = new ArrayBlockingQueue<>(100);
        HashMap<String, Integer> counts = new HashMap<>();

        Thread counter = new Thread(new Counter(queue, counts));
        Thread parser = new Thread(new Parser(queue));

        long start = System.currentTimeMillis();
        // START:mainloop

        counter.start();
        parser.start();
        parser.join();
        queue.put(new PoisonPill());
        counter.join();
        // END:mainloop
        long end = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (end - start) + "ms");


    }

}
