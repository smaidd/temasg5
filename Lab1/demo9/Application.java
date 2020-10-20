package demo9;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)){
           for(int i = 0; i< 10; i++){
               System.out.println(context.getBean(Integer.class));
           }
        }
    }
}
