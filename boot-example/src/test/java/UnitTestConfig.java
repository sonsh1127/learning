import com.nins.interfaces.MyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnitTestConfig {

    @Bean(destroyMethod = "shutdown")
    MyBean myBean() {
        return new MyBean();
    }

}
