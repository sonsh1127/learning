import com.nins.interfaces.MyBean2;
import javax.inject.Inject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig2.class)
public class UnitTestTwo {

    @Inject
    private MyBean2 myBean2;

    @Test
    public void run() {

        myBean2.process();
        System.out.println("test 2 endede");
    }


    @BeforeClass
    public static void beforeClass() {
        System.out.println("$$$$$$$$ Test222 BeforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("$$$$$$$$ Test222 AfterClass");
    }

}
