package concurrent.ch1.wordcount.produceconsume;

import concurrent.ch1.wordcount.Doc;
import concurrent.ch1.wordcount.Docs;
import java.util.concurrent.BlockingQueue;

public class Parser implements Runnable {

    private BlockingQueue<Doc> docs;

    public Parser(BlockingQueue<Doc> docs) {
        this.docs = docs;
    }

    @Override
    public void run() {
        Iterable<Doc> pages = new Docs(2000000, "wiki-dump.xml");
        pages.forEach(doc -> {
            try {
                docs.put(doc);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}
