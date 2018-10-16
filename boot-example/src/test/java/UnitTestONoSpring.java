import org.junit.Assert;
import org.junit.Test;

public class UnitTestONoSpring {

    @Test
    public void doWork() {
        String str = "nins world";
        Assert.assertTrue(str.startsWith("nins"));
    }

}
