package com.ebit.loader.loader;

import com.ebit.loader.common.UserRowMapper;
import com.ebit.loader.model.User;
import com.ebit.loader.service.IgniteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserLoader {

    private final JdbcTemplate jdbcTemplate;
    private final IgniteService igniteService;

    @Value("${loader.batch-size:100}")
    private int batchSize;

    public UserLoader(
            JdbcTemplate jdbcTemplate,
            IgniteService igniteService) {

        this.jdbcTemplate = jdbcTemplate;
        this.igniteService = igniteService;
    }

    /*
     * Existing REST API method.
     * Do NOT change this.
     */
    public void loadUsers() {
        loadUsers(0);
    }

    /*
     * Scheduler-compatible method.
     */
    public void loadUsers(Map<String, String> arguments) {

        int offset = 0;

        if (arguments != null &&
                arguments.containsKey("dataoffset")) {

            offset = Integer.parseInt(
                    arguments.get("dataoffset")
            );
        }

        loadUsers(offset);
    }

    /*
     * Common implementation.
     */
    private void loadUsers(int offset) {

        String sql = """
                SELECT
                    id,
                    created_at,
                    created_by,
                    email_address,
                    first_name,
                    last_name,
                    updated_at,
                    updated_by
                FROM user_database.users
                ORDER BY id
                LIMIT ? OFFSET ?
                """;

        while (true) {

            List<User> users = jdbcTemplate.query(
                    sql,
                    new UserRowMapper(),
                    batchSize,
                    offset
            );

            if (users.isEmpty()) {
                break;
            }

            System.out.println(
                    "Loading batch: offset=" + offset +
                            ", size=" + users.size()
            );

            igniteService.streamUsers(users);

            offset += users.size();

            if (users.size() < batchSize) {
                break;
            }
        }
    }
}