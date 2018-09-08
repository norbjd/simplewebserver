package com.norbjd.simplewebserver.item.repository.postgresql;

import com.norbjd.simplewebserver.item.model.Item;
import com.norbjd.simplewebserver.item.repository.ItemNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Properties;

public class PostgresqlItemRepositoryIntegrationTest extends PostgresqlIntegrationTestBase {

    private PostgresqlItemRepository itemRepository;

    @Before
    public void instanciateItemRepository() throws SQLException {
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", authorizedUserName);

        itemRepository = new PostgresqlItemRepository(hostPort, databaseName, connectionProperties);
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
