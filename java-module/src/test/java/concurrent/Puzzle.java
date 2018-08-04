package concurrent;

public class Puzzle {

    static boolean done = false;
    static int answer = 0;

    static Thread t1 = new Thread() {
        @Override
        public void run() {
            done = true;
            Thread.yield();
            answer++;
        }
    };

    static Thread t2 = new Thread() {
        @Override
        public void run() {
            if (done) {
                System.out.println("The answer is " + answer);
            } else {
                System.out.println("not ready");
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    public static void print(boolean res) {
        if (res) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
