package demo6;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class ApplicationConfiguration {
    @Bean("president")
    public Person createPresident(@Value("Klaus Iohannis") String name){
        return new President(name);
    }
    @Bean("king")
    public Person createKing(@Value("Norway's king") String name){
        return new King(name);
    }
}
