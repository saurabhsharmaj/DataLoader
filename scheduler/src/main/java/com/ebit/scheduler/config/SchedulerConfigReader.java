package com.ebit.scheduler.config;

import com.ebit.scheduler.filter.BatchConfigurationManual;
import com.ebit.scheduler.models.ManualSchedulerArgument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class SchedulerConfigReader {

    private final Map<String, BatchConfigurationManual> jobs =
            new HashMap<>();

    private final String configFile;

    public SchedulerConfigReader(
            @Value("${scheduler.config:config/scheduler-config.xml}")
            String configFile) {

        this.configFile = configFile;
        loadConfiguration();
    }

    private void loadConfiguration() {

        try {

            File file = new File(configFile);

            System.out.println(
                    "Loading scheduler configuration: "
                            + file.getAbsolutePath()
            );

            if (!file.exists()) {
                throw new IllegalArgumentException(
                        "Scheduler configuration file not found: "
                                + file.getAbsolutePath()
                );
            }

            Document document =
                    DocumentBuilderFactory
                            .newInstance()
                            .newDocumentBuilder()
                            .parse(file);

            NodeList jobNodes =
                    document.getElementsByTagName("job");

            for (int i = 0; i < jobNodes.getLength(); i++) {

                Element jobElement =
                        (Element) jobNodes.item(i);

                BatchConfigurationManual config =
                        new BatchConfigurationManual();

                String jobName =
                        jobElement.getAttribute("name");

                config.setName(jobName);
                config.setModule(
                        getText(jobElement, "module"));
                config.setMainClass(
                        getText(jobElement, "mainClass"));
                config.setMethod(
                        getText(jobElement, "method"));
                config.setVersion(
                        getText(jobElement, "version"));

                ManualSchedulerArgument arguments =
                        new ManualSchedulerArgument();

                NodeList argumentNodes =
                        jobElement.getElementsByTagName("argument");

                for (int j = 0;
                     j < argumentNodes.getLength();
                     j++) {

                    Element argument =
                            (Element) argumentNodes.item(j);

                    arguments.add(
                            argument.getAttribute("name"),
                            argument.getAttribute("value")
                    );
                }

                config.setArguments(arguments);

                jobs.put(jobName, config);
            }

            System.out.println(
                    "Loaded scheduler jobs: "
                            + jobs.keySet()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to load scheduler configuration: "
                            + configFile,
                    e
            );
        }
    }

    public BatchConfigurationManual getJob(String jobName) {

        BatchConfigurationManual config =
                jobs.get(jobName);

        if (config == null) {

            throw new IllegalArgumentException(
                    "Job not found in scheduler-config.xml: "
                            + jobName
            );
        }

        return config;
    }

    private String getText(
            Element parent,
            String tagName) {

        return parent
                .getElementsByTagName(tagName)
                .item(0)
                .getTextContent()
                .trim();
    }
}