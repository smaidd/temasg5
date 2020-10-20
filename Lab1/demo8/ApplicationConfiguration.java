package demo8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ComponentScan
public class ApplicationConfiguration {

    @Bean
    @Order(3)
    public String getFirstCity(){
        return "Bucharest";
    }
    @Bean
    @Order(1)
    public String getSecondCity(){
        return "Cluj";
    }
    @Bean
    @Order(2)
    public String getThirdCity(){
        return "Timisoara";
    }

}
