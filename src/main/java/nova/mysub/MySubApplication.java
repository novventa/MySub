package nova.mysub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableFeignClients
@ImportAutoConfiguration(FeignAutoConfiguration.class)
@PropertySource("classpath:.env")
public class MySubApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySubApplication.class, args);
    }

}
