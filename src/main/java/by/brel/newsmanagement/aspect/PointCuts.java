package by.brel.newsmanagement.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PointCuts {

    @Pointcut("execution(* by.brel.newsmanagement.controller.*.*(..))")
    public void controllersMethods() {}

    @Pointcut("execution(* by.brel.newsmanagement.service.impl.*.*(..))")
    public void servicesMethods() {}
}
