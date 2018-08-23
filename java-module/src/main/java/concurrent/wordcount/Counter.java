package concurrent.wordcount;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Counter implements Runnable {

    private BlockingQueue<Page> queue;
    private Map<String, Integer> counts;

    public Counter(BlockingQueue<Page> queue, Map<String, Integer> counts) {
        this.queue = queue;
        this.counts = counts;
    }

    @Override
    public void run() {

        while (true) {
            Page page = null;
            try {
                page = queue.take();
                if (page.isPoisonPill())
                    break;
               // Iterable<String> words = new Words(page.getText());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

    }
}
