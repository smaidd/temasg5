package demo8;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Country {
    private String name;

    @Autowired
    private List<String> cities;


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
               "cities= " + cities.stream().collect(Collectors.joining(", ")) +
                '}';
    }
}