package demo7;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Country {
    private String name;

    @Autowired
    private String largestCity;
    @Autowired
    private String smallestCity;


    public Country(@Value("Romania") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", largestCity='" + largestCity + '\'' +
                ", smallestCity='" + smallestCity + '\'' +
                '}';
    }
}