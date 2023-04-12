package com.sipc.messageserver.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.TypeVariable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 20:31
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {

//    @Pointcut("execution(* com.sipc.messageserver.controller.*.*(..))")
//    public void webLog() {
//
//    }
//
//    @Before("webLog()")
//    public void doBefore(JoinPoint joinPoint) {
//
//        startTime = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
//
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (requestAttributes == null) {
//            return;
//        }
//        HttpServletRequest request = requestAttributes.getRequest();
//
//        WebLog webLog = new WebLog();
//
//        webLog.setUri(request.getRequestURI());
//        webLog.setMethod(request.getMethod());
//        webLog.setIp(request.getRemoteAddr());
//        webLog.setDescription("class:" + joinPoint.getSignature().getDeclaringTypeName() +
//                "," + "function:" + joinPoint.getSignature().getName());
//        webLog.setParameter(joinPoint.getArgs());
//        webLog.setStartTime(LocalDateTime.ofEpochSecond(startTime, 0 ,
//                ZoneOffset.ofHours(8)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//
//        log.info("收到请求: {}", webLog);
//
//    }
//
//    @Around(value = "webLog()")
//    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
//
//        WebLog webLog = new WebLog();
//
//        //获取HttpServletRequest
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        if (requestAttributes == null) {
//            return;
//        }
//        HttpServletRequest request = requestAttributes.getRequest();
//
//        LocalDateTime startTime = LocalDateTime.now();
//
//        Object proceed = joinPoint.proceed();
//        //获取请求参数
//        Object[] args = joinPoint.getArgs();
//        //设置请求参数类
//        List<Object> argList = new ArrayList<>();
//        //获取请求参数
//        Class declaringType = joinPoint.getSignature().getDeclaringType();
//        int i = 0;
//        for (TypeVariable typeParameter : declaringType.getTypeParameters()) {
//            //获取@RequestBody注解的参数
//            RequestBody annotation = typeParameter.getAnnotation(RequestBody.class);
//            if (annotation != null) {
//                argList.add(args[i]);
//                ++i;
//                continue;
//            }
//
//            //获取@RequestParam注解的参数
//            RequestParam annotation1 = typeParameter.getAnnotation(RequestParam.class);
//            if (annotation1 != null) {
//                Map<String, Object> map = new HashMap<>();
//                map.put(typeParameter.getName(), args[i]);
//                argList.add(map);
//            }
//            ++i;
//        }
//
//        argList.add(args);
//
//        webLog.setIp();
//
//    }
//
//    @AfterReturning(pointcut = "webLog()", returning = "ret")
//    public void after(JoinPoint joinPoint, Object ret) {
//        WebLog webLog = new WebLog();
//
//        webLog.setResult(ret);
//        webLog.setSpendTime(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8)) - startTime);
//
//        log.info("请求完成: {}", webLog);
//    }

}
