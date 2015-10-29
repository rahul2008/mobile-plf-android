package com.philips.cdp.serviceapi.productinformation.disclaimers;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Disclaimers {
	
	private String mUrl = "http://www.philips.co.uk/prx/product/B2C/en_GB/CONSUMER/products/55PFL8008S/12.disclaimers";

@SerializedName("success")
@Expose
private boolean success;
@SerializedName("data")
@Expose
private Data data;

/**
* No args constructor for use in serialization
* 
*/
public Disclaimers() {
}

/**
* 
* @param data
* @param success
*/
public Disclaimers(boolean success, Data data) {
this.success = success;
this.data = data;
}

/**
* 
* @return
* The success
*/
public boolean isSuccess() {
return success;
}

/**
* 
* @param success
* The success
*/
public void setSuccess(boolean success) {
this.success = success;
}

public Disclaimers withSuccess(boolean success) {
this.success = success;
return this;
}

/**
* 
* @return
* The data
*/
public Data getData() {
return data;
}

/**
* 
* @param data
* The data
*/
public void setData(Data data) {
this.data = data;
}

public Disclaimers withData(Data data) {
this.data = data;
return this;
}

}

