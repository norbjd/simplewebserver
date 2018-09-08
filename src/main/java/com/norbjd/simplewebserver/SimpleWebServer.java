package com.norbjd.simplewebserver;

import com.norbjd.simplewebserver.item.model.Item;
import com.norbjd.simplewebserver.item.repository.ItemNotFoundException;
import com.norbjd.simplewebserver.item.repository.ItemRepository;
import spark.Spark;

public class SimpleWebServer {

    private ItemRepository itemRepository;

    SimpleWebServer(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void start() {
        registerRoutes();
    }

    private void registerRoutes() {
        registerItemsRoute();
        registerHealthRoute();
    }

    private void registerHealthRoute() {
        Spark.get("/health", (request, response) -> {
            response.status(200);
            return "OK";
        });
    }

    private void registerItemsRoute() {
        Spark.get("/items/:id", (request, response) -> {
            int itemId = Integer.parseInt(request.params(":id"));

            try {
                Item item = itemRepository.getItem(itemId);
                return item.getName();
            } catch (ItemNotFoundException infe) {
                response.status(400);
                return infe.getMessage();
            }
        });
    }

}
