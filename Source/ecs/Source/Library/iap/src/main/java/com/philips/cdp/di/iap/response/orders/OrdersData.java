package com.philips.cdp.di.iap.response.orders;

import java.util.List;

public class OrdersData {


    private Pagination pagination;
    private List<Orders> orders;

    private List<Sorts> sorts;

    public Pagination getPagination() {
        return pagination;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public List<Sorts> getSorts() {
        return sorts;
    }


    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
