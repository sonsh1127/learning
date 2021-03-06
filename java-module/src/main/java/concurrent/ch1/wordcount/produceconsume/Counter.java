package concurrent.ch1.wordcount.produceconsume;

import concurrent.ch1.wordcount.Doc;
import concurrent.ch1.wordcount.Words;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Counter implements Runnable {

    private BlockingQueue<Doc> queue;
    private Map<String, Integer> counts;

    public Counter(BlockingQueue<Doc> queue, Map<String, Integer> counts) {
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
        Integer currentCount = counts.get(word);
        if (currentCount == null) {
            counts.put(word, 1);
        } else {
            counts.put(word, currentCount + 1);
        }
    }
}
