import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public class RegExpLearningTest {
    @Test
    public void simpleTest() {
        Pattern pattern = Pattern.compile("\\d{1,2}");
        Matcher matcher = pattern.matcher("1a2b14_sf6");
        System.out.println(matcher.groupCount());

        while (matcher.find()) {
            System.out.println(matcher.start() + " " +  matcher.end() + " "+ matcher.group());
        }

    }
}
