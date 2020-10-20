package demo5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class City {
    private String name;
    private Country country;

    public City(@Value("Bucharest") String name, @Autowired @Lazy Country country) {
        this.name = name;
        this.country = country;
        System.out.println("DEGUG: City constructor");
    }

    public Country getCountry() {
        System.out.println("DEGUG: City getCountry");
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", country=" + getCountry().getName() +
                '}';
    }
}
