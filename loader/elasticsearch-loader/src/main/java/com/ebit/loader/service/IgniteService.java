package com.ebit.loader.service;

import com.ebit.loader.model.User;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.table.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SubmissionPublisher;

@Service
public class IgniteService {

    private final IgniteClient igniteClient;

    public IgniteService(IgniteClient igniteClient) {
        this.igniteClient = igniteClient;
    }

    public void streamUsers(List<User> users) {

        Table table = igniteClient.tables().table("users");

        RecordView<Tuple> view = table.recordView();

        DataStreamerOptions options = DataStreamerOptions.builder()
                .pageSize(1000)
                .autoFlushInterval(1000)
                .retryLimit(3)
                .build();

        CompletableFuture<Void> future;

        try (SubmissionPublisher<DataStreamerItem<Tuple>> publisher = new SubmissionPublisher<>()) {
            future = view.streamData(publisher, options);

            for (User user : users) {
                Tuple tuple = Tuple.create()
                        .set("id", user.getId())
                        .set("created_at", user.getCreatedAt())
                        .set("created_by", user.getCreatedBy())
                        .set("email_address", user.getEmailAddress())
                        .set("first_name", user.getFirstName())
                        .set("last_name", user.getLastName())
                        .set("updated_at", user.getUpdatedAt())
                        .set("updated_by", user.getUpdatedBy());

                publisher.submit(DataStreamerItem.of(tuple));
            }
        }

        future.join();
        System.out.println("All users streamed to Ignite successfully.");
    }
}
