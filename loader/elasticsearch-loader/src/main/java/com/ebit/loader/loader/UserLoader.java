package com.ebit.loader.loader;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ebit.loader.model.User;
import com.ebit.loader.service.IgniteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class UserLoader {

    @Value("${loader.batch-size:100}")
    private int batchSize;

    private final IgniteService igniteService;

    private final ElasticsearchClient client;

    public UserLoader(ElasticsearchClient client, IgniteService igniteService) {
        this.igniteService = igniteService;
        this.client = client;
    }

    public List<User> loadPage(int page) {
        try {
            SearchResponse<User> response = client.search(s -> s
                    .index("users")
                    .from(page * batchSize)
                    .size(batchSize)
                    .sort(so -> so
                            .field(f -> f.field("id")
                                    .order(SortOrder.Asc)))
                    .query(q -> q.matchAll(m -> m)),
                    User.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load users from Elasticsearch", e);
        }
    }

    public void loadUsers() {
        int page = 0;

        while (true) {
            List<User> batch = loadPage(page);
            if (batch.isEmpty()) break;
            System.out.println("Streaming batch: page=" + page + ", size=" + batch.size());
            igniteService.streamUsers(batch);
            if (batch.size() < batchSize) break;
            page++;
        }
    }
}
