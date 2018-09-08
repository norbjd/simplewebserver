package com.norbjd.simplewebserver.item.repository.mongo;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.norbjd.simplewebserver.item.model.Item;
import com.norbjd.simplewebserver.item.repository.ItemNotFoundException;
import com.norbjd.simplewebserver.item.repository.ItemRepository;
import org.bson.Document;

import java.util.Properties;

public class MongoItemRepository implements ItemRepository {

    private MongoCollection<Document> items;

    public MongoItemRepository(String hostPort, String database, Properties properties) {
        String url = "mongodb://" + hostPort;
        MongoClient mongoClient = MongoClients.create(url);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        items = mongoDatabase.getCollection("items");
    }

    @Override
    public Item getItem(int itemId) throws ItemNotFoundException {
        MongoCursor<Document> docs = items.find().filter(Filters.eq("id", itemId)).limit(1).iterator();

        if (!docs.hasNext()) {
            throw new ItemNotFoundException(itemId);
        }

        String itemName = docs.next().getString("name");
        return new Item(itemId, itemName);
    }
}
