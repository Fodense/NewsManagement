package by.brel.newsmanagement.aspect.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class ServicesAspectLogger {

    @Around("by.brel.newsmanagement.aspect.PointCuts.servicesMethods()")
    public Object aroundServicesMethods(ProceedingJoinPoint joinPoint) {
        Object target = null;
        StopWatch watch = new StopWatch();

        try {
            watch.start();
            target = joinPoint.proceed();
            watch.stop();

            log.info(joinPoint.getSignature().getName() + " - completed for " + watch.getTotalTimeMillis() + " ms");

        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getMessage());
        }

        return target;
    }
}
