package com.ebit.scheduler;

import com.ebit.scheduler.filter.BatchConfigurationManual;
import com.ebit.scheduler.models.ManualSchedulerArgument;
import com.ebit.scheduler.utils.ReflectionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainScheduler {

    private final ApplicationContext applicationContext;

    public MainScheduler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void execute(BatchConfigurationManual configuration) {

        System.out.println(
                "Executing job: " +
                        configuration.getName()
        );

        System.out.println(
                "Module: " +
                        configuration.getModule()
        );

        System.out.println(
                "Class: " +
                        configuration.getMainClass()
        );

        System.out.println(
                "Method: " +
                        configuration.getMethod()
        );

        ManualSchedulerArgument arguments =
                configuration.getArguments();

        ReflectionUtils.invoke(
                applicationContext,
                configuration.getMainClass(),
                configuration.getMethod(),
                arguments
        );
    }
}