package demo2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Country {
    private String name;
    private String capital;
    private String continent;

    public Country(@Value("Romania") String name, @Value("Bucharest") String capital, @Value("Europa") String continent){
        this.capital=capital;
        this.continent =continent;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }
    public void changeCountry(String name, String continent, String capital){
        this.name = name;
        this.continent = continent;
        this.capital =capital;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", capital='" + capital + '\'' +
                ", continent='" + continent + '\'' +
                '}';
    }
}
