package com.ebit.loader.service;

import com.ebit.loader.model.User;
import com.ebit.loader.model.UserRequest;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.sql.ResultSet;
import org.apache.ignite.sql.SqlRow;
import org.apache.ignite.table.*;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SubmissionPublisher;

@Service
public class IgniteService {

    private final IgniteClient igniteClient;

    public IgniteService(IgniteClient igniteClient) {
        this.igniteClient = igniteClient;
    }

    public void saveUserReq(UserRequest userRequest) {

        Table table = igniteClient.tables()
                .table("userrequest");

        RecordView<Tuple> view = table.recordView();

        Tuple user = Tuple.create()
                .set("id", 1)
                .set("name", userRequest.getName())
                .set("email", userRequest.getEmail());

        view.upsert(null, user);
    }

    public UserRequest getUserReq(Integer id) {

        Table table = igniteClient.tables()
                .table("userrequest");

        RecordView<Tuple> view = table.recordView();

        Tuple key = Tuple.create()
                .set("id", id);

        Tuple value = view.get(null, key);

        UserRequest response = new UserRequest();

        if (value != null) {
            response.setName(value.stringValue("name"));
            response.setEmail(value.stringValue("email"));
        }

        return response;
    }

    public void streamUserReq(List<UserRequest> users) {

        Table table = igniteClient.tables().table("userrequest");

        RecordView<Tuple> view = table.recordView();

        DataStreamerOptions options = DataStreamerOptions.builder()
                .pageSize(1000)
                .autoFlushInterval(1000)
                .retryLimit(3)
                .build();

        CompletableFuture<Void> future;

        try (SubmissionPublisher<DataStreamerItem<Tuple>> publisher =
                     new SubmissionPublisher<>()) {

            future = view.streamData(publisher, options);

            for(UserRequest userRequest: users) {

                Tuple user = Tuple.create()
                        .set("id", userRequest.getId())
                        .set("name", userRequest.getName())
                        .set("email", userRequest.getEmail());

                publisher.submit(DataStreamerItem.of(user));
            }
        }

        future.join();
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

        try (SubmissionPublisher<DataStreamerItem<Tuple>> publisher =
                     new SubmissionPublisher<>()) {

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

                publisher.submit(
                        DataStreamerItem.of(tuple)
                );
            }
        }

        future.join();
    }

    public @Nullable List<UserRequest> getStreamUserReq() {

        List<UserRequest> users = new ArrayList<>();

        try (ResultSet<SqlRow> resultSet =
                     igniteClient.sql().execute(
                             null,
                             "SELECT id, name, email FROM userrequest"
                     )) {

            while (resultSet.hasNext()) {

                SqlRow row = resultSet.next();

                UserRequest user = new UserRequest();
                user.setId(row.intValue("ID"));
                user.setName(row.stringValue("NAME"));
                user.setEmail(row.stringValue("EMAIL"));

                users.add(user);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to read users from Ignite",
                    e
            );
        }

        return users;
    }
}