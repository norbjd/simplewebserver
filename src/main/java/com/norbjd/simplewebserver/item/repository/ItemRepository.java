package com.norbjd.simplewebserver.item.repository;

import com.norbjd.simplewebserver.item.model.Item;

public interface ItemRepository {

    Item getItem(int itemId) throws ItemNotFoundException;

}
