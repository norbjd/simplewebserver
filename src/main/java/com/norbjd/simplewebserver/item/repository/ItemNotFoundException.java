package com.norbjd.simplewebserver.item.repository;

public class ItemNotFoundException extends IllegalArgumentException {

    public ItemNotFoundException(int itemId) {
        super("Item not found : id = [" + itemId + "]");
    }

}
