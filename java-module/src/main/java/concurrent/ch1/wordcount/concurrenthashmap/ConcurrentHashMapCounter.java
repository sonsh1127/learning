package concurrent.ch1.wordcount.concurrenthashmap;

import concurrent.ch1.wordcount.Doc;
import concurrent.ch1.wordcount.Words;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapCounter implements Runnable{
    private BlockingQueue<Doc> queue;
    private ConcurrentHashMap<String, Integer> counts;

    public ConcurrentHashMapCounter(BlockingQueue<Doc> queue, ConcurrentHashMap<String, Integer> counts) {
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
        while (true) {
            Integer currentCount = counts.get(word);
            if (currentCount == null) {
                if (counts.putIfAbsent(word, 1) == null)
                    break;
            } else if (counts.replace(word, currentCount, currentCount + 1)) {
                break;
            }
        }
    }
}
