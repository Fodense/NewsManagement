package by.brel.newsmanagement.aspect.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class ControllersAspectLogger {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Around("by.brel.newsmanagement.aspect.PointCuts.controllersMethods()")
    public Object aroundForControllersMethods(ProceedingJoinPoint joinPoint) {
        Object target = null;

        log.info(request.getMethod() + " " + request.getRequestURI());

        Map<String, String[]> stringMap = request.getParameterMap();

        if (!stringMap.isEmpty()) {
            log.info("Parameters:");

            for (Map.Entry<String, String[]> entry : stringMap.entrySet()) {
                log.info(entry.getKey() + "=" + Arrays.toString(entry.getValue()));
            }
        }

        try {
            target = joinPoint.proceed();

        } catch (Throwable throwable) {
            log.info(throwable.getMessage());

            throw new RuntimeException(throwable.getMessage());
        }

        log.info(String.valueOf(response.getStatus()));

        return target;
    }
}
