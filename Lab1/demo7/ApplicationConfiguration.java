package demo7;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ApplicationConfiguration {

    @Bean("largestCity")
    public String getLargestCity(){
        return "Bucharest";
    }
    @Bean("smallestCity")
    public String getSmallestCity(){
        return "Tusnad";
    }
}
