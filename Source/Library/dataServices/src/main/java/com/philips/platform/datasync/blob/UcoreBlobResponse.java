package com.philips.platform.datasync.blob;

import com.google.gson.annotations.Expose;

public class UcoreBlobResponse {

    @Expose
    private String itemId;

    void setItemId(String itemId) {
        this.itemId = itemId;
    }

    String getItemId() {
        return itemId;
    }
}
