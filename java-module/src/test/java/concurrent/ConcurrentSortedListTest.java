package concurrent;

import concurrent.ch1.ConcurrentSortedList;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class ConcurrentSortedListTest {

    @Test
    public void insert() throws InterruptedException {
        ConcurrentSortedList list = new ConcurrentSortedList();
        Random random = new Random();
        class InsertThread extends Thread {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    list.insert(random.nextInt());
                }
            }
        }
        InsertThread t1 = new InsertThread();
        InsertThread t2 = new InsertThread();

        t1.start(); t2.start();
        t1.join(); t2.join();

        Assert.assertEquals(20000, list.size());
        Assert.assertTrue(list.isSorted());
    }
}