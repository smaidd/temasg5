package demo4;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)){
            Country country = context.getBean(Country.class);
            System.out.println("country:" + country.getName());
            System.out.println("capital:" + country.getCapital().getName());

        }
    }
}
