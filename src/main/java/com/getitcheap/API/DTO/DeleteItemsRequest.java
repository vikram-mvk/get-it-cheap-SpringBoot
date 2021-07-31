package com.getitcheap.API.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DeleteItemsRequest {

    @JsonProperty("ids")
    List<Long> ids;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
