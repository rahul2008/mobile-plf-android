package com.philips.cdp.serviceapi.productinformation.navigation;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Navigation {
	
	
	private String mUrl = "http://www.philips.co.uk/prx/product/B2C/en_GB/CONSUMER/products/RQ1250/17.navigation";

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
public Navigation() {
}

/**
* 
* @param data
* @param success
*/
public Navigation(boolean success, Data data) {
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

public Navigation withSuccess(boolean success) {
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

public Navigation withData(Data data) {
this.data = data;
return this;
}

}