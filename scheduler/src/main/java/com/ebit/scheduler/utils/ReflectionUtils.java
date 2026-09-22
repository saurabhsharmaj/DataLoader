package com.ebit.scheduler.utils;

import com.ebit.scheduler.models.ManualSchedulerArgument;

import java.lang.reflect.Method;
import org.springframework.context.ApplicationContext;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Object invoke(
            ApplicationContext applicationContext,
            String className,
            String methodName,
            ManualSchedulerArgument arguments) {

        try {

            Class<?> clazz = Class.forName(className);

            Object bean = applicationContext.getBean(clazz);

            Method method = clazz.getMethod(
                    methodName,
                    ManualSchedulerArgument.class
            );

            return method.invoke(bean, arguments);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to invoke " +
                            className + "." +
                            methodName,
                    e
            );
        }
    }
}