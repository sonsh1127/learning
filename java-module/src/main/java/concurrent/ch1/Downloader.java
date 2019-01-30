package concurrent.ch1;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Downloader extends Thread {

    private InputStream in;
    private OutputStream out;
    private List<ProgressListener> listeners;

    public Downloader(URL url, String outputFile) throws IOException {
        this.in = url.openConnection().getInputStream();
        this.out = new FileOutputStream(outputFile);
        this.listeners = new ArrayList<>();
    }

    public synchronized void addListener(ProgressListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ProgressListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void run() {
        int n, total = 0;
        byte[] buffer = new byte[1024];

        try {
            while (true) {
                if (!((n = in.read(buffer)) != -1)) {
                    break;
                }
                out.write(buffer, 0, n);
                total += n;
                updateProgress(total);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // onProgress may cause deadlock
    private synchronized void updateProgress(int total) {
        listeners.forEach(l -> l.onProgress(total));
    }
}


interface ProgressListener {
    void onProgress(int n);
}
