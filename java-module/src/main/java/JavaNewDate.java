import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Test;

public class JavaNewDate {

    @Test
    public void instantiate() {
        LocalDate date = LocalDate.of(2018, 11, 17);
        LocalDate date1 = LocalDate.parse("2018-11-17");
        LocalTime time = LocalTime.of(06, 10);
        LocalTime time1 = LocalTime.parse("06:10");
    }
}
