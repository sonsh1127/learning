import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.junit.Test;

public class JavaNewDate {

    @Test
    public void instantiate() {
        LocalDate date = LocalDate.of(2018, 11, 17);
        String result = DateTimeFormatter.ofPattern("yyyyMMdd").format(date);

        LocalDate date1 = LocalDate.parse("2018-11-17");
        LocalTime time = LocalTime.of(06, 10);
        LocalTime time1 = LocalTime.parse("06:10");
    }

    @Test
    public void localTime() {
        ZoneId zone = ZoneId.of("Europe/London");
        LocalTime time = LocalTime.now(zone);
        System.out.println(time);
    }

    @Test
    public void instant() {
        Instant instant = Clock.systemUTC().instant();
        System.out.println(instant);
    }

}
