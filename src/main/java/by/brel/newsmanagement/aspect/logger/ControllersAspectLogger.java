package by.brel.newsmanagement.aspect.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Before("by.brel.newsmanagement.aspect.PointCuts.controllersMethods()")
    public void beforeControllersMethods(JoinPoint joinPoint) {
        log.info(request.getMethod() + " " + request.getRequestURI());

        Map<String, String[]> stringMap = request.getParameterMap();

        if (!stringMap.isEmpty()) {
            log.info("Parameters:");

            for (Map.Entry<String, String[]> entry : stringMap.entrySet()) {
                log.info(entry.getKey() + "=" + Arrays.toString(entry.getValue()));
            }
        }
    }

    @AfterReturning(value = "by.brel.newsmanagement.aspect.PointCuts.controllersMethods()")
    public void afterReturningControllersMethods() {
        log.info(String.valueOf(response.getStatus()));
    }
}
