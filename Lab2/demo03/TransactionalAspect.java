package demo03;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class TransactionalAspect {
    @Around("execution (* *(..)) && @annotation(annotation)")
    public void aroundMethod(ProceedingJoinPoint joinPoint, Transactional annotation){
        Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
        System.out.println("Beginning transaction..");
        System.out.println(String.format("Calling %s : %s ", method.getDeclaringClass().getName(), method.getName()));
        try {
            joinPoint.proceed();
            System.out.println("Committing transaction..");
        } catch (Throwable throwable) {
            System.err.println("Roll-back transaction..");
        }
    }
}
