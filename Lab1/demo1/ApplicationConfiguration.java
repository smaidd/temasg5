package demo1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    //@Scope("prototype")
    public Country createCountry(){
        return new Country("Romania", "Bucharest", "Europa");
    }
}
