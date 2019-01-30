package concurrent.ch1;

import java.io.IOException;
import java.net.URL;
import org.junit.Test;

public class DownloaderTest {
    @Test
    public void makeDeadLock() throws IOException, InterruptedException {
        Downloader downloader = new Downloader(new URL("https://www.google.com"), "google.txt");
        downloader.addListener(n -> {
            Thread t = new Thread(() -> downloader.addListener(n1 -> System.out.println("currentProgress: "+ n1)));
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        downloader.start();
        downloader.join();
        System.out.println("ended");
    }
}