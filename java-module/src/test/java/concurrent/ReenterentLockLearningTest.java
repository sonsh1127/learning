package concurrent;

import java.lang.Thread.State;
import java.sql.SQLOutput;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;

public class ReenterentLockLearningTest {

    @Test
    public void simpleDeadLock() throws InterruptedException {
        Object o1 = new Object();
        Object o2 = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (o1) {
                try {
                    Thread.sleep(1000);
                    synchronized (o2) {
                        System.out.println("t1 ended");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (o2) {
                try {
                    Thread.sleep(1000);
                    synchronized (o1) {
                        System.out.println("t2 ended");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        Thread.sleep(10 * 1000);
        System.out.println(t1.getState() + ", " + t2.getState());
        if (t1.getState() == State.BLOCKED && t2.getState() == State.BLOCKED) {
            t1.interrupt();
            t2.interrupt();
        }

        if (t1.getState() == State.BLOCKED && t2.getState() == State.BLOCKED) {
            System.out.println("interrupt is useless");
        }
    }


    @Test
    public static void main(String[] args) throws InterruptedException {

        ReentrantLock l1 = new ReentrantLock();
        ReentrantLock l2 = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            try {
                l1.lockInterruptibly();
                Thread.sleep(1000);
                l2.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                l2.lockInterruptibly();
                Thread.sleep(1000);
                l1.lockInterruptibly();
                System.out.println("t2 ended");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();
        Thread.sleep(10 * 1000);
        System.out.println(t1.getState() + ", " + t2.getState());
        if (t1.getState() == State.WAITING && t2.getState() == State.WAITING) {
            System.out.println("both try to interrupt ");
            t1.interrupt();
            t2.interrupt();
        }

        Thread.sleep(1000);

        if (t1.getState() == State.WAITING && t2.getState() == State.WAITING) {
            System.out.println("interrupt is useless");
        }else{
            System.out.println("Thread ended");
        }
    }

}
