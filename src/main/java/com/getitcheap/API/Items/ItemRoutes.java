package com.getitcheap.API.Items;

public @interface ItemRoutes {

    String GET_ITEMS = "/items";
    String NEW_ITEM = "/item";
    String GET_ITEM = "/item/{id}";
    String USER_ITEM = "/items/:username";
    String SEARCH_ITEMS = "/items/search";

}
