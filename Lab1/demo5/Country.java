package demo5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class Country {
    private String name;
    private City capital;

    public Country(@Value("Romania") String name, @Autowired @Lazy City capital){
        this.name = name;
        this.capital =capital;
        System.out.println("DEGUG: Country constructor");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCapital() {

        System.out.println("DEGUG: Country getCity");
        return capital;
    }

    public void setCapital(City capital) {
        this.capital = capital;
        capital.setCountry(this);
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", capital='" + getCapital().getName() + '\'' +
                '}';
    }
}
