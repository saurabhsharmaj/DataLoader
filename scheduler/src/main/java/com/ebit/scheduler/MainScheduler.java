package com.ebit.scheduler;


import com.ebit.scheduler.filter.BatchConfigurationManual;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class MainScheduler {

    private final ApplicationContext applicationContext;

    public MainScheduler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void execute(BatchConfigurationManual config) {

        System.out.println("=================================");
        System.out.println("Starting Scheduler");
        System.out.println("Job       : " + config.getName());
        System.out.println("Module    : " + config.getModule());
        System.out.println("Main Class: " + config.getMainClass());
        System.out.println("Method    : " + config.getMethod());
        System.out.println("Version   : " + config.getVersion());
        System.out.println("Arguments : " + config.getArguments());
        System.out.println("=================================");

        com.ebit.scheduler.utils.ReflectionUtils.invoke(
                applicationContext,
                config.getMainClass(),
                config.getMethod(),
                config.getArguments()
        );

        System.out.println(
                "Job completed: " + config.getName()
        );
    }
}