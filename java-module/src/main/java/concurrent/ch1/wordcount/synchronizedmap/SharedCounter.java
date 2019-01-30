package concurrent.ch1.wordcount.synchronizedmap;

import concurrent.ch1.wordcount.Doc;
import concurrent.ch1.wordcount.Words;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class SharedCounter implements Runnable {

    private BlockingQueue<Doc> queue;
    private Map<String, Integer> counts;

    private static ReentrantLock lock = new ReentrantLock();

    public SharedCounter(BlockingQueue<Doc> queue, Map<String, Integer> counts) {
        this.queue = queue;
        this.counts = counts;
    }

    @Override
    public void run() {
        while (true) {
            Doc doc = null;
            try {
                doc = queue.take();
                if (doc.isPoisonPill()) {
                    break;
                }
                Iterable<String> words = new Words(doc.getText());
                updateMap(words);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMap(Iterable<String> words) {
        for (String word : words) {
            update(word);
        }
    }

    private void update(String word) {
        lock.lock();

        try {
            Integer currentCount = counts.get(word);
            if (currentCount == null)
                counts.put(word, 1);
            else
                counts.put(word, currentCount + 1);
        } finally {
            lock.unlock();
        }
    }
}
