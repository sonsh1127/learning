package reactive.spring;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
public class RxJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContextConfig.class, args);
    }
}
