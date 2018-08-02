import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Philosopher extends Thread {

    private Chopstick left, right;

    public Philosopher(Chopstick left, Chopstick right) {
        this.left = left;
        this.right = right;
    }

    public static void main(String[] args) {
        int count = 5;
        List<Philosopher> philosophers = new ArrayList<>(count);
        List<Chopstick> chopsticks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            chopsticks.add(new Chopstick());
        }
        for (int i = 0; i < count; i++) {
            philosophers.add(new Philosopher(chopsticks.get(i), chopsticks.get((i + 1) % count)));
        }
        philosophers.forEach(p -> p.start());
    }

    @Override
    public void run() {
        try {
            while (true) {
                think();
                synchronized (left) {
                    System.out.println("left chopsitck picked!! " + Thread.currentThread().getName());
                    synchronized (right) {
                        eat();
                    }
                }
            }
        } catch (InterruptedException ie) {
        }
    }

    private void eat() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1);
        System.out.println("Thread :" + Thread.currentThread().getName() + ", " + "eat");
    }

    private void think() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1);
        System.out.println("Thread :" + Thread.currentThread().getName() + ", " + "think");
    }
}


class Chopstick {

}
