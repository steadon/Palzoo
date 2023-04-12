package com.sipc.messageserver.utils.lark;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tzih
 * @version 1.0
 * @since 2023/4/10 14:55
 */
@Aspect
@Component
public class LarkAop {

    @Resource
    LarkRobot larkRobot;

    @Pointcut("execution(* *..controller.*.*(..))")
    public void lark(){

    }
    @AfterThrowing(pointcut = "lark()", throwing = "exception")
    public void larkRobot(JoinPoint point, Exception exception){
        if (larkRobot.getDev()) return;
//        larkRobot.sendExceptionMessage(point, exception);
    }



}