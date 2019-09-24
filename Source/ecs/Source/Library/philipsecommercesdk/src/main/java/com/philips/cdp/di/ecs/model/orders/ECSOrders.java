package com.philips.cdp.di.ecs.model.orders;



import java.io.Serializable;

public class ECSOrders implements Serializable {
    private String code;
    private String guid;
    private String placed;
    private String status;
    private String statusDisplay;
    private ECSOrderDetail orderDetail;


    public ECSOrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(ECSOrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    private Total total;

    public String getCode() {
        return code;
    }

    public String getGuid() {
        return guid;
    }

    public String getPlaced() {
        return placed;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public Total getTotal() {
        return total;
    }


}

