package com.github.sbstnc.example.otel.jooq;

import java.sql.Connection;
import java.util.Properties;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.meta.postgres.PostgresDatabase;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.jdbc.JDBCUtils;
import org.postgresql.Driver;
import org.testcontainers.containers.PostgreSQLContainer;

public class LiquibasePostgreSqlDatabase extends PostgresDatabase {
  static {
    System.getProperties().setProperty("org.jooq.no-logo", "true");
  }

  private static final JooqLogger LOG = JooqLogger.getLogger(LiquibasePostgreSqlDatabase.class);
  private static final String DEFAULT_CHANGELOG_LOCATION = "classpath:db/changelog-master.xml";
  private static final String DEFAULT_DOCKER_IMAGE = "postgres:13-alpine";

  private PostgreSQLContainer<?> container;
  private Connection connection;

  @Override
  protected DSLContext create0() {
    if (connection == null) {
      try {
        LOG.info("Starting PostgreSQL container for jOOQ code generation");
        container =
            new PostgreSQLContainer<>(DEFAULT_DOCKER_IMAGE)
                .withDatabaseName("database")
                .withUsername("jOOQ")
                .withPassword("jOOQ");
        container.start();

        LOG.info("Going to connect to " + container.getJdbcUrl());
        final var driver = new Driver();
        Properties properties = new Properties();
        properties.put("user", container.getUsername());
        properties.put("password", container.getPassword());
        connection = driver.connect(container.getJdbcUrl(), properties);

        LOG.info("Running liquibase migrations");
        final var db =
            DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        final var liquibase =
            new Liquibase(DEFAULT_CHANGELOG_LOCATION, new ClassLoaderResourceAccessor(), db);
        liquibase.update(new Contexts(), new LabelExpression());

        LOG.info("Executed migrations successfully");
        setConnection(connection);
      } catch (final Exception e) {
        LOG.error("Unable to start container and migrate schemas {}", e.getMessage());
        throw new DataAccessException("Unable to start container and migrate schemas", e);
      }
    }
    return DSL.using(connection, SQLDialect.POSTGRES);
  }

  @Override
  public void close() {
    LOG.info("Going to disconnect from the SQL server");
    JDBCUtils.safeClose(connection);
    connection = null;
    super.close();

    LOG.info("Going to stop the SQL server");
    container.stop();
  }
}
