package com.aizuda.trans.aspect;

import com.aizuda.trans.handler.TranslatorHandle;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;

/**
 * 字典翻译 在方法上定义@Tanslator注解，对方法返回值进行翻译
 *
 * @author nn200433
 * @date 2023-05-25 04:21:27
 */
@Slf4j
@Aspect
@Component
public class TranslateAspect {

    @Autowired
    private GenericConversionService genericConversionService;

    @Pointcut("@annotation(com.aizuda.trans.annotation.Translator)")
    public void pointCut() {
    }

    @AfterReturning(pointcut = "pointCut()", returning = "returnValue")
    public void doAfter(JoinPoint joinPoint, Object returnValue) {
        if (null == returnValue) {
            return;
        }
        // MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // Translator      config    = signature.getMethod().getAnnotation(Translator.class);

        // 先从容器中获取的转换器进行返回值解包，注意此处返回结果可能是Bean也可能是集合。然后再进行翻译
        TranslatorHandle.transform(genericConversionService.convert(returnValue, Object.class));
    }

}
