package demo03;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)) {
            Worker worker = context.getBean(Worker.class);
            for(int i = 0; i <10; i++){
                worker.doSomething();
            }
        } catch (Exception e) {
        }
    }
}
