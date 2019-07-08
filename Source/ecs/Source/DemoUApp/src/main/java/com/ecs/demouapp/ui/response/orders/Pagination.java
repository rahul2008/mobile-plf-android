package com.ecs.demouapp.ui.response.orders;

public class Pagination {
    private int currentPage;
    private int pageSize;
    private String sort;
    private int totalPages;
    private int totalResults;

    public int getCurrentPage() {
        return currentPage;
    }


    public int getPageSize() {
        return pageSize;
    }

    public String getSort() {
        return sort;
    }


    public int getTotalPages() {
        return totalPages;
    }


    public int getTotalResults() {
        return totalResults;
    }

}
