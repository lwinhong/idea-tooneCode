package com.tooneCode.core.model.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Generated;

public class Recommendation {
    @JSONField(
        name = "type"
    )
    private String type;
    @JSONField(
        name = "item"
    )
    private ItemClass item;

    public Recommendation() {
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public ItemClass getItem() {
        return this.item;
    }

    @Generated
    public void setType(String type) {
        this.type = type;
    }

    @Generated
    public void setItem(ItemClass item) {
        this.item = item;
    }
}

