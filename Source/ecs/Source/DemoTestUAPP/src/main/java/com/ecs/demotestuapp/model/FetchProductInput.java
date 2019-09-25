package com.ecs.demotestuapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FetchProductInput implements Serializable {

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @SerializedName("pageNumber")
    public int pageNumber;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @SerializedName("pageSize")
    public int pageSize;


}
