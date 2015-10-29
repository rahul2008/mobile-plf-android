package com.philips.cdp.serviceapi.productinformation.accessories;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Accessories {
	
	
	private String mUrl = "http://www.philips.co.uk/prx/product/B2C/en_GB/CONSUMER/products/RQ1250/17.accessories";

@SerializedName("success")
@Expose
private boolean success;
@SerializedName("data")
@Expose
private List<Datum> data = new ArrayList<Datum>();

/**
* No args constructor for use in serialization
* 
*/
public Accessories() {
}

/**
* 
* @param data
* @param success
*/
public Accessories(boolean success, List<Datum> data) {
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

public Accessories withSuccess(boolean success) {
this.success = success;
return this;
}

/**
* 
* @return
* The data
*/
public List<Datum> getData() {
return data;
}

/**
* 
* @param data
* The data
*/
public void setData(List<Datum> data) {
this.data = data;
}

public Accessories withData(List<Datum> data) {
this.data = data;
return this;
}

}

