package com.philips.uid.model.component

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Control {

    @SerializedName("state")
    @Expose
    private List<String> state = null;
    @SerializedName("parent")
    @Expose
    private List<String> parent = null;
    @SerializedName("context")
    @Expose
    private String context;
    @SerializedName("property")
    @Expose
    private ControlProperty controlProperty;
    @SerializedName("component")
    @Expose
    private String component;

    public List<String> getState() {
        return state;
    }

    public void setState(List<String> state) {
        this.state = state;
    }

    public List<String> getParent() {
        return parent;
    }

    public void setParent(List<String> parent) {
        this.parent = parent;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public ControlProperty getControlProperty() {
        return controlProperty;
    }

    public void setControlProperty(ControlProperty controlProperty) {
        this.controlProperty = controlProperty;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    def getBrushName() {
        controlProperty.value
    }
}