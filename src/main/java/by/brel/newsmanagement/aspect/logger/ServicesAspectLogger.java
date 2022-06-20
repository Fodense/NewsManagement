package by.brel.newsmanagement.aspect.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ServicesAspectLogger {

    @AfterThrowing(value = "by.brel.newsmanagement.aspect.PointCuts.servicesMethods()", throwing = "exception")
    public void afterThrowingServicesMethods(Exception exception) {
        log.info(exception.getMessage());
    }
}
