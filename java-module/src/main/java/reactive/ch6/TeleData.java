package reactive.ch6;

import java.time.LocalDateTime;

public class TeleData {

    private LocalDateTime time;

    public TeleData(long timestamp) {
        time = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "TeleData{" +
                "time=" + time +
                '}';
    }
}
