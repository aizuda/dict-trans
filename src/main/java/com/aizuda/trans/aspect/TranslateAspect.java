package com.aizuda.trans.aspect;

import com.aizuda.trans.annotation.Translator;
import com.aizuda.trans.handler.TranslatorHandle;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 字典翻译
 * 在方法上定义@Tanslator注解，对方法返回值进行翻译
 *
 * @author luozhan
 * @create 2020-04
 * @see Translator
 */
@Slf4j
@Aspect
@Component
public class TranslateAspect {
    
    @Pointcut("@annotation(com.aizuda.trans.annotation.Translator)")
    public void pointCut() {
    }
    
    @AfterReturning(pointcut = "pointCut()", returning = "object")
    public void doAfter(JoinPoint joinPoint, Object object) {
        if (null == object) {
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Translator      config    = signature.getMethod().getAnnotation(Translator.class);
        TranslatorHandle.parse(object, config.value());
    }
    
}
