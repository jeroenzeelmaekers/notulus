package notulus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author Jeroen Zeelmaekers
 * @version 0.0.1
 * @since 23/08/2022
 */
@EnableCaching
@SpringBootApplication
public class NotulusApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotulusApplication.class, args);
    }

}
