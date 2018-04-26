public class Counting {
    public static void main(String[] args) throws InterruptedException {
        class Counter {
            private int count = 0;
            public synchronized void increase() { count++; }
            public int getCount() { return count;}
        }

        final Counter counter = new Counter();

        Runnable increase = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.increase();
            }
        };

        Thread thread1 = new Thread(increase);
        Thread thread2 = new Thread(increase);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(counter.getCount());
    }
}
