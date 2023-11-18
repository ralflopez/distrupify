package com.distrupify;

import com.distrupify.services.InventoryDepositService;
import com.google.errorprone.annotations.Immutable;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.jboss.logging.Logger;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PostgresResource implements
        QuarkusTestResourceLifecycleManager {
    static PostgreSQLContainer<?> db =
            new PostgreSQLContainer<>("postgres:16.0")
                    .withDatabaseName("distrupify_test")
                    .withUsername("test")
                    .withPassword("test");

    @Override
    public Map<String, String> start() {
        db.start();
        Map<String, String> properties = new HashMap<>();
        properties.put("quarkus.datasource.username",db.getUsername());
        properties.put("quarkus.datasource.password", db.getPassword());
        properties.put("quarkus.datasource.jdbc.url", db.getJdbcUrl());
        return properties;
//        return ImmutableMap.of(
//                "quarkus.datasource.username",db.getUsername(),
//                "quarkus.datasource.password", db.getPassword(),
//                "quarkus.datasource.jdbc.url", db.getJdbcUrl()
//        );
    }

    @Override
    public void stop() {
        db.stop();
    }
}