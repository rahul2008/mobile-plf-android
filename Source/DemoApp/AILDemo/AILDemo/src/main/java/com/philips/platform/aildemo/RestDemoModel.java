package com.philips.platform.aildemo;

/**
 * Created by 310238655 on 8/31/2016.
 */
public class RestDemoModel {

    public String getObject_or_array() {
        return object_or_array;
    }

    public void setObject_or_array(String object_or_array) {
        this.object_or_array = object_or_array;
    }

    public String getEmpty() {
        return empty;
    }

    public void setEmpty(String empty) {
        this.empty = empty;
    }

    public String getParse_time_nanoseconds() {
        return parse_time_nanoseconds;
    }

    public void setParse_time_nanoseconds(String parse_time_nanoseconds) {
        this.parse_time_nanoseconds = parse_time_nanoseconds;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    String object_or_array;
    String empty;
    String parse_time_nanoseconds;
    String validate;
    String size;
}
