package com.philips.cdp.serviceapi.productinformation.summary;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Summary {
	
	
	private String mUrl = "http://www.philips.co.uk/prx/product/B2C/en_GB/CONSUMER/products/RQ1250/17.summary";

@SerializedName("success")
@Expose
private Boolean success;
@SerializedName("data")
@Expose
private Data data;

/**
* 
* @return
* The success
*/
public Boolean getSuccess() {
return success;
}

/**
* 
* @param success
* The success
*/
public void setSuccess(Boolean success) {
this.success = success;
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

}