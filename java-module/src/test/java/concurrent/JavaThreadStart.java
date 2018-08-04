package concurrent;

public class JavaThreadStart {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("");
        });
        thread.start();
        System.out.println("Hello from main Thread");
        thread.join();
    }
}
