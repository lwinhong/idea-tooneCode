package com.tooneCode.core.model.model;

import com.google.gson.annotations.JsonAdapter;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.jsonrpc.json.adapters.JsonElementTypeAdapter;

public class InitializeResultExt extends InitializeResult {
    @JsonAdapter(JsonElementTypeAdapter.Factory.class)
    private Object experimental;

    public InitializeResultExt() {
    }

    public Object getExperimental() {
        return this.experimental;
    }

    public void setExperimental(Object experimental) {
        this.experimental = experimental;
    }
}
