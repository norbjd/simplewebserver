package com.norbjd.simplewebserver.item.repository.postgresql;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.UUID;

public abstract class PostgresqlIntegrationTestBase {

    private static String integrationTestUUID = "test" + UUID.randomUUID().toString().replaceAll("-", "");

    static String hostPort;
    static {
        if ((hostPort = System.getenv("POSTGRESQL_HOSTPORT")) == null) {
            throw new IllegalArgumentException("Missing POSTGRESQL_HOSTPORT environment variable");
        }
    }
    static String databaseName = integrationTestUUID;
    static String authorizedUserName = "integrationtest_" + integrationTestUUID.substring(1, 3);

    private static String connectionUrl = "jdbc:postgresql://" + hostPort + "/";
    private static String databaseConnectionUrl = connectionUrl + databaseName;

    private static void waitToBeSurePostgresqlIsUp() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setUp() throws SQLException {
        waitToBeSurePostgresqlIsUp();

        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", "postgres");

        Connection connection = DriverManager.getConnection(connectionUrl, connectionProperties);

        createDatabase(connection);

        connection.close();

        Connection databaseConnection = DriverManager.getConnection(databaseConnectionUrl, connectionProperties);

        createItemsTable(databaseConnection);
        insertDataIntoItemsTable(databaseConnection);
        createAuthorizedUser(databaseConnection);

        databaseConnection.close();
    }

    private static void createDatabase(Connection connection) throws SQLException {
        Statement st = connection.createStatement();
        st.execute("CREATE DATABASE " + databaseName);
        st.close();
    }

    private static void createItemsTable(Connection databaseConnection) throws SQLException {
        Statement st = databaseConnection.createStatement();
        st.execute("CREATE TABLE items(id INTEGER PRIMARY KEY, name VARCHAR)");
        st.close();
    }

    private static void insertDataIntoItemsTable(Connection databaseConnection) throws SQLException {
        Statement st = databaseConnection.createStatement();
        st.execute("INSERT INTO items(id, name) VALUES (1, 'item1')");
        st.execute("INSERT INTO items(id, name) VALUES (2, 'item2')");
        st.close();
    }

    private static void createAuthorizedUser(Connection databaseConnection) throws SQLException {
        Statement st = databaseConnection.createStatement();
        st.execute("CREATE USER " + authorizedUserName);
        st.execute("GRANT SELECT ON items TO " + authorizedUserName);
        st.close();
    }

    @AfterClass
    public static void tearDown() throws SQLException {
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", "postgres");

        Connection connection = DriverManager.getConnection(connectionUrl, connectionProperties);

        closeRemainingConnectionsOnDatabase(connection, databaseName);
        dropTestDatabase(connection);
        dropAuthorizedUser(connection);

        connection.close();
    }

    private static void dropAuthorizedUser(Connection databaseConnection) throws SQLException {
        Statement st = databaseConnection.createStatement();
        st.execute("DROP USER " + authorizedUserName);
        st.close();
    }

    private static void closeRemainingConnectionsOnDatabase(Connection databaseConnection, String databaseName)
            throws SQLException {
        Statement st = databaseConnection.createStatement();
        st.execute("SELECT pg_terminate_backend(pg_stat_activity.pid) " + "FROM pg_stat_activity "
                + "WHERE pg_stat_activity.datname = '" + databaseName + "' " + "AND pid <> pg_backend_pid()");
        st.close();
    }

    private static void dropTestDatabase(Connection databaseConnection) throws SQLException {
        Statement st = databaseConnection.createStatement();
        st.execute("DROP DATABASE " + databaseName);
        st.close();
    }

}
