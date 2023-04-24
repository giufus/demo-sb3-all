package com.giufus.demo.services.containers;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public interface AbstractDatasourceContainer {

    @Container
    PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("demo_sb3_all");
}
