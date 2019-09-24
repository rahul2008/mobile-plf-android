package com.philips.cdp.di.ecs.model.orders;

import java.io.Serializable;
import java.util.List;

public class ECSOrderHistory implements Serializable {


    private Pagination pagination;
    private List<ECSOrders> orders;

    private List<Sorts> sorts;

    public Pagination getPagination() {
        return pagination;
    }

    public List<ECSOrders> getOrders() {
        return orders;
    }

    public void setOrders(List<ECSOrders> orders) {
        this.orders = orders;
    }

    public List<Sorts> getSorts() {
        return sorts;
    }


    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}