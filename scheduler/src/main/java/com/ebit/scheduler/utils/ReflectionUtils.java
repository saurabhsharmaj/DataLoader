package com.ebit.scheduler.utils;

import com.ebit.scheduler.models.ManualSchedulerArgument;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static Object invoke(
            ApplicationContext applicationContext,
            String className,
            String methodName,
            ManualSchedulerArgument arguments) {

        try {

            Class<?> clazz =
                    Class.forName(className);

            Object bean =
                    applicationContext.getBean(clazz);

            /*
             * 1. Try:
             *
             * loadUsers(Map<String, String>)
             */
            try {

                Method method =
                        clazz.getMethod(
                                methodName,
                                Map.class
                        );

                System.out.println(
                        "Invoking: "
                                + className
                                + "."
                                + methodName
                                + "(Map)"
                );

                return method.invoke(
                        bean,
                        arguments.getArguments()
                );

            } catch (NoSuchMethodException ignored) {

                /*
                 * 2. Fallback:
                 *
                 * loadUsers()
                 */
                Method method =
                        clazz.getMethod(methodName);

                System.out.println(
                        "Invoking: "
                                + className
                                + "."
                                + methodName
                                + "()"
                );

                return method.invoke(bean);
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to invoke "
                            + className
                            + "."
                            + methodName,
                    e
            );
        }
    }
}