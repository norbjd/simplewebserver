package com.norbjd.simplewebserver.item.repository.postgresql;

import com.norbjd.simplewebserver.item.model.Item;
import com.norbjd.simplewebserver.item.repository.ItemNotFoundException;
import com.norbjd.simplewebserver.item.repository.ItemRepository;

import java.sql.*;
import java.util.Properties;

public class PostgresqlItemRepository implements ItemRepository {

    private Connection conn;

    public PostgresqlItemRepository(String hostPort, String database, Properties properties) throws SQLException {
        String url = "jdbc:postgresql://" + hostPort + "/" + database;

        conn = DriverManager.getConnection(url, properties);
    }

    @Override
    public Item getItem(int itemId) throws ItemNotFoundException {
        Item item = null;

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT name FROM items WHERE id = " + itemId + " LIMIT 1");
            if (rs.next()) {
                String itemName = rs.getString("name");
                item = new Item(itemId, itemName);
            }
            rs.close();
            st.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        if (item == null) {
            throw new ItemNotFoundException(itemId);
        }

        return item;
    }
}
