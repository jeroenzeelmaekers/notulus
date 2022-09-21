package notulus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class NotulusApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotulusApplication.class, args);
    }

}
