package com.philips.cdp.serviceapi.productinformation.overview;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Overview {

	private String mUrl = "http://www.philips.co.uk/prx/product/B2C/en_GB/CONSUMER/products/RQ1250/17.overview";

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
	public Overview() {
	}

	/**
	 * 
	 * @param data
	 * @param success
	 */
	public Overview(boolean success, Data data) {
		this.success = success;
		this.data = data;
	}

	/**
	 * 
	 * @return The success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 
	 * @param success
	 *            The success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Overview withSuccess(boolean success) {
		this.success = success;
		return this;
	}

	/**
	 * 
	 * @return The data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 *            The data
	 */
	public void setData(Data data) {
		this.data = data;
	}

	public Overview withData(Data data) {
		this.data = data;
		return this;
	}

}
