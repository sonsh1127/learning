import com.nins.interfaces.MyBean;
import javax.inject.Inject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UnitTestConfig.class)
@DirtiesContext
public class UnitTestOne {

    @Inject
    private MyBean myBean;

    @Test
    public void process() {
        Assert.assertTrue(myBean.process() >= 0);
        System.out.println("test one ended");
    }

    @BeforeClass
    public static void beforeClass() {
        System.out.println("$$$$$$$$ TestOne BeforeClass");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("$$$$$$$$ TestOne AfterClass");
    }
}
