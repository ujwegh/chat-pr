package ru.nik.chatpr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {
    @Value("${spring.data.cassandra.contact-points:placeholder}")
    private String contactPoints;

    @Value("${spring.data.cassandra.port:0000}")
    private int port;

    @Value("${spring.data.cassandra.keyspace-name:placeholder}")
    private String keySpace;

    @Value("${spring.data.cassandra.schema-action}")
    private String schemaAction;

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(schemaAction.toUpperCase());
    }

    @Override
    protected String getLocalDataCenter() {
        return "datacenter1";
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification =
                CreateKeyspaceSpecification.createKeyspace(keySpace)
                        .ifNotExists()
                        .withSimpleReplication();
        return List.of(specification);
    }

    @Override
    protected List<String> getStartupScripts() {
        return Arrays.asList("USE " + keySpace,
                "CREATE TABLE IF NOT EXISTS messages (" +
                        "roomId text," +
                        "dateTime timestamp," +
                        "fromUserEmail text," +
                        "toUserEmail text," +
                        "text text," +
                        "creator text," +
                        "PRIMARY KEY ((fromUserEmail, roomId), dateTime, creator)" +
                        ") WITH CLUSTERING ORDER BY (dateTime ASC)");
    }
}
