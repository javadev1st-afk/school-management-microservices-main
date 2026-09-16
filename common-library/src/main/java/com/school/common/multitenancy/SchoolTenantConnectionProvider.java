package com.school.common.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(DataSource.class)
public class SchoolTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

    private final DataSource dataSource;
    private final String defaultSchema;

    public SchoolTenantConnectionProvider(
            DataSource dataSource,
            @Value("${app.tenant.default-schema:school_management}") String defaultSchema) {
        this.dataSource = dataSource;
        this.defaultSchema = defaultSchema;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        switchSchema(connection, defaultSchema);
        return connection;
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        releaseConnection(connection);
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            switchSchema(connection, tenantIdentifier);
            return connection;
        } catch (SQLException | RuntimeException exception) {
            connection.close();
            throw exception;
        }
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        releaseConnection(connection);
    }

    private void releaseConnection(Connection connection) throws SQLException {
        try {
            switchSchema(connection, defaultSchema);
        } finally {
            connection.close();
        }
    }

    private void switchSchema(Connection connection, String schema) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("USE `" + schema + "`");
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return unwrapType.isAssignableFrom(getClass())
                || unwrapType.isAssignableFrom(MultiTenantConnectionProvider.class);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return unwrapType.cast(this);
        }
        throw new IllegalArgumentException("Cannot unwrap tenant connection provider as " + unwrapType);
    }
}
