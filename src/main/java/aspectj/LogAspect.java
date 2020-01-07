package aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("@annotation(annotation.SystemLog)")
    public void systemPoint(){}

    @Before("systemPoint()")
    public void doBefore(JoinPoint joinPoint){
        String className = joinPoint.getTarget().getClass().getName();
        String methodName =  joinPoint.getSignature().getName();
        String params = Arrays.toString(joinPoint.getArgs());
        logger.info(className);
        logger.info(methodName);
        logger.info(params);
    }

    @AfterReturning(returning = "returnValue",pointcut = "systemPoint()")
    public void doAfterRetuning(Object returnValue){
        logger.info("处理完返回结果");
    }

    @AfterThrowing(pointcut = "systemPoint()", throwing = "throwable")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable throwable){
        logger.info("抛出异常信息");
    }
}
