package com.norbjd.simplewebserver.item.repository.mongo;

import com.norbjd.simplewebserver.item.model.Item;
import com.norbjd.simplewebserver.item.repository.ItemNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class MongoItemRepositoryIntegrationTest {

    private MongoItemRepository itemRepository;

    private static String hostPort;
    private static String database;
    static {
        if ((hostPort = System.getenv("MONGO_HOSTPORT")) == null) {
            throw new IllegalArgumentException("Missing MONGO_HOSTPORT environment variable");
        }
        if ((database = System.getenv("MONGO_DATABASE")) == null) {
            throw new IllegalArgumentException("Missing MONGO_DATABASE environment variable");
        }
    }

    @Before
    public void instanciateItemRepository() {
        Properties connectionProperties = new Properties();

        itemRepository = new MongoItemRepository(hostPort, database, connectionProperties);
    }

    @Test
    public void itemPresentInRepository() {
        Item item1 = itemRepository.getItem(1);
        Assert.assertEquals("item1", item1.getName());
    }

    @Test(expected = ItemNotFoundException.class)
    public void itemNotFoundInRepository() {
        itemRepository.getItem(3);
    }

}
