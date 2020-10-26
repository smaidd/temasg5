package demo02;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)) {
            Settings settings = context.getBean(Settings.class);
            System.out.println(settings.getHost());
        }
    }
}
