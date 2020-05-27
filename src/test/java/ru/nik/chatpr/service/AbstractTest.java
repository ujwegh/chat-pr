package ru.nik.chatpr.service;

import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class AbstractTest {

    @Container
    public static final GenericContainer redis = new FixedHostPortGenericContainer<>("redis:6.0.3-alpine")
            .withFixedExposedPort(6379, 6379);

    @Container
    public static final GenericContainer cassandra = new FixedHostPortGenericContainer<>("cassandra:latest")
            .withFixedExposedPort(9042, 9042);

    @Container
    public static final GenericContainer postgres = new FixedHostPortGenericContainer<>("postgres:9.6.18-alpine")
            .withFixedExposedPort(5432, 5432)
            .withEnv("POSTGRES_USER","postgres")
            .withEnv("POSTGRES_PASSWORD","postgres")
            .withEnv("POSTGRES_DB","chat-dev");


}
