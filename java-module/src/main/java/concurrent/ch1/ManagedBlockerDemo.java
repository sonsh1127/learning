package concurrent.ch1;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinPool.ManagedBlocker;
import java.util.concurrent.locks.ReentrantLock;

public class ManagedBlockerDemo {

    public static void main(String[] args) throws Exception{

        BlockingQueue<String> bq = new ArrayBlockingQueue<>(2);
        bq.put("A");
        bq.put("B");
        QueueManagedBlocker<String> blocker = new QueueManagedBlocker<String>(bq);
        ForkJoinPool.managedBlock(blocker);
        System.out.println(blocker.getValue());



        //ForkJoinPool.managedBlock(blocker);
    }


    static public class Locker implements ManagedBlocker {
        final ReentrantLock rtlock;
        boolean isLocked = false;
        Locker(ReentrantLock rtlock) {
            this.rtlock = rtlock;
        }
        public boolean block() {
            if (!isLocked){
                rtlock.lock();
            }
            return true;
        }
        public boolean isReleasable() {
            return isLocked || (isLocked = rtlock.tryLock());
        }
    }

    static public class QueueManagedBlocker<T> implements ManagedBlocker {
        final BlockingQueue<T> queue;
        volatile T value = null;
        QueueManagedBlocker(BlockingQueue<T> queue) {
            this.queue = queue;
        }
        public boolean block() throws InterruptedException {
            System.out.println(Thread.currentThread().getName());
            if (value == null)
                value = queue.take();
            return true;
        }
        public boolean isReleasable() {
            return value != null || (value = queue.poll()) != null;
        }
        public T getValue() {
            return value;
        }
    }

}


