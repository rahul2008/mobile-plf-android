package com.ecs.demouapp.ui.response.orders;

public class Orders {
    private String code;
    private String guid;
    private String placed;
    private String status;
    private String statusDisplay;

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
