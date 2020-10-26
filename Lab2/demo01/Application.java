package demo01;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)){
            Person person = context.getBean(Person.class);
            person.setFirstName("Silvia");
            person.setLastname("Ilie");
            person.setGender('f');
        }
    }
}
