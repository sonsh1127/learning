package concurrent.ch1.wordcount.seq;

import concurrent.ch1.wordcount.Doc;
import concurrent.ch1.wordcount.Docs;
import concurrent.ch1.wordcount.Words;
import java.util.HashMap;

public class SequentialWordCount {

    private static final HashMap<String, Integer> counts = new HashMap<>();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Iterable<Doc> pages = new Docs(2000000, "wiki-dump.xml");
        pages.forEach(doc -> {
            Iterable<String> words = new Words(doc.getText());
            words.forEach(
                    word -> countWord(word)
            );
        });

        System.out.println("elapsed time : " + (System.currentTimeMillis() - start));
        System.out.println("ended");
        System.out.println(counts.size());
    }

    static void countWord(String word) {
        Integer currentCount = counts.get(word);
        if (currentCount == null) {
            counts.put(word, 1);
        } else {
            counts.put(word, currentCount + 1);
        }
    }
}
