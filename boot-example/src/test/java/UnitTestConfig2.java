import com.nins.interfaces.MyBean2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnitTestConfig2 {

    @Bean(destroyMethod = "shutdown")
    public MyBean2 myBean2() {
        return new MyBean2();

    }

}
