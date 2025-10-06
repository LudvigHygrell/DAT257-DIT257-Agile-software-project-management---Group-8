package com.backend.tests.filtering;

import org.springframework.boot.test.context.SpringBootTest;

import com.backend.database.entities.Comment;
import com.backend.database.filtering.Filter;
import com.backend.database.filtering.FilteredQuery;
import com.backend.database.filtering.JsonToFilterConverter;
import com.backend.tests.ResourceLoader;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(webEnvironment=NONE)
public class FilteringTest {

    @PersistenceContext
    private EntityManager entityManager;

    private final Logger logger = LoggerFactory.getLogger(FilteringTest.class);

    @Test
    public void testFiltering() throws Exception {

        JsonNode json = ResourceLoader.loadJson("filtering-test-filters.json");
        for (JsonNode filters : json) {
            
            logger.info("Applying filters: {}", filters);

            FilteredQuery<Comment> q = new FilteredQuery<>(entityManager, Comment.class);
            Filter<Comment> compiledFilter = JsonToFilterConverter.filterFromJson(q.getFilterBuilder(), filters);
            
            List<Comment> comments = q.runQuery(compiledFilter);
            logger.info("Query complete. {} filters in total.", comments.size());
            for (Comment comment : comments) {
                logger.info(comment.toString());
            }
        }
    }
}
