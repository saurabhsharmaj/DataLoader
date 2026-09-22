package com.ebit.scheduler;

import com.ebit.scheduler.config.SchedulerConfigReader;
import com.ebit.scheduler.filter.BatchConfigurationManual;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.ebit.scheduler",
        "com.ebit.loader"
})
public class SchedulerApplication implements CommandLineRunner {

    private final MainScheduler mainScheduler;
    private final SchedulerConfigReader configReader;
    private final ConfigurableApplicationContext context;

    public SchedulerApplication(
            MainScheduler mainScheduler,
            SchedulerConfigReader configReader,
            ConfigurableApplicationContext context) {

        this.mainScheduler = mainScheduler;
        this.configReader = configReader;
        this.context = context;
    }

    public static void main(String[] args) {

        SpringApplication app =
                new SpringApplication(SchedulerApplication.class);

        app.setWebApplicationType(
                WebApplicationType.NONE
        );

        app.run(args);
    }

    @Override
    public void run(String... args) {

        String jobName = null;

        for (String arg : args) {

            if (arg.startsWith("--job=")) {
                jobName = arg.substring("--job=".length());
            }
        }

        if (jobName == null || jobName.isBlank()) {

            System.out.println("No scheduler job specified.");
            System.out.println("Example: --job=userLoader");

            context.close();
            return;
        }

        try {

            BatchConfigurationManual config =
                    configReader.getJob(jobName);

            mainScheduler.execute(config);

            System.out.println(
                    "================================="
            );
            System.out.println(
                    "Scheduler job completed successfully: "
                            + jobName
            );
            System.out.println(
                    "Stopping scheduler..."
            );
            System.out.println(
                    "================================="
            );

        } finally {

            context.close();
        }
    }
}