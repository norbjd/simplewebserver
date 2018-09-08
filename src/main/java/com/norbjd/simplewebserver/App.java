package com.norbjd.simplewebserver;

import com.norbjd.simplewebserver.item.repository.ItemRepository;
import com.norbjd.simplewebserver.item.repository.mongo.MongoItemRepository;
import com.norbjd.simplewebserver.item.repository.postgresql.PostgresqlItemRepository;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class App {

    private static ItemRepository getPostgresqlItemRepository() {
        Map<String, String> env = System.getenv();
        String postgresqlHostPort = env.getOrDefault("POSTGRESQL_HOSTPORT", null);
        String postgresqlDatabase = env.getOrDefault("POSTGRESQL_DATABASE", null);
        String postgresqlUser = env.getOrDefault("POSTGRESQL_USER", null);

        ItemRepository itemRepository = null;

        try {
            Properties postgresqlConnectionProperties = new Properties();
            postgresqlConnectionProperties.setProperty("user", postgresqlUser);
            itemRepository = new PostgresqlItemRepository(postgresqlHostPort, postgresqlDatabase,
                    postgresqlConnectionProperties);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.exit(-1);
        }

        return itemRepository;
    }

    private static ItemRepository getMongoItemRepository() {
        Map<String, String> env = System.getenv();
        String mongoHostPort = env.getOrDefault("MONGO_HOSTPORT", null);
        String mongoDatabase = env.getOrDefault("MONGO_DATABASE", null);

        ItemRepository itemRepository;

        Properties mongoProperties = new Properties();
        itemRepository = new MongoItemRepository(mongoHostPort, mongoDatabase, mongoProperties);

        return itemRepository;
    }

    private static ItemRepository chooseItemRepository() {
        Map<String, String> env = System.getenv();
        if (env.containsKey("POSTGRESQL_HOSTPORT")) {
            return getPostgresqlItemRepository();
        } else if (env.containsKey("MONGO_HOSTPORT")) {
            return getMongoItemRepository();
        } else {
            throw new IllegalArgumentException(
                    "POSTGRESQL_HOSTPORT or MONGO_HOSTPORT must be set to guess used backend");
        }
    }

    public static void main(String[] args) {
        ItemRepository itemRepository = chooseItemRepository();

        SimpleWebServer simpleWebServer = new SimpleWebServer(itemRepository);
        simpleWebServer.start();
    }
}
