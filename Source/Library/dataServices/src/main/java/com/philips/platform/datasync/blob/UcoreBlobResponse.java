package com.philips.platform.datasync.blob;

import com.google.gson.annotations.Expose;

/**
 * Created by philips on 4/5/17.
 */

public class UcoreBlobResponse {

    @Expose
    private String itemId;

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }
}
