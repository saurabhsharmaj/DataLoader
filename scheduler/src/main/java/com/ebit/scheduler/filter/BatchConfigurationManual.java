package com.ebit.scheduler.filter;

import com.ebit.scheduler.models.ManualSchedulerArgument;

public class BatchConfigurationManual {

    private String name;
    private String module;
    private String mainClass;
    private String method;
    private String version;

    private ManualSchedulerArgument arguments =
            new ManualSchedulerArgument();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ManualSchedulerArgument getArguments() {
        return arguments;
    }

    public void setArguments(ManualSchedulerArgument arguments) {
        this.arguments = arguments;
    }
}
