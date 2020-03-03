package com.philips.cdp.prodreg.model_request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserProfile implements Serializable {

	@SerializedName("zip")
	private int zip;

	@SerializedName("country")
	private String country;

	@SerializedName("address3")
	private String address3;

	@SerializedName("address2")
	private String address2;

	@SerializedName("city")
	private String city;

	@SerializedName("phone")
	private String phone;

	@SerializedName("address1")
	private String address1;

	@SerializedName("houseNumber")
	private int houseNumber;

	@SerializedName("optIn")
	private boolean optIn;

	@SerializedName("state")
	private String state;

	public void setZip(int zip){
		this.zip = zip;
	}

	public int getZip(){
		return zip;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setAddress3(String address3){
		this.address3 = address3;
	}

	public String getAddress3(){
		return address3;
	}

	public void setAddress2(String address2){
		this.address2 = address2;
	}

	public String getAddress2(){
		return address2;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setAddress1(String address1){
		this.address1 = address1;
	}

	public String getAddress1(){
		return address1;
	}

	public void setHouseNumber(int houseNumber){
		this.houseNumber = houseNumber;
	}

	public int getHouseNumber(){
		return houseNumber;
	}

	public void setOptIn(boolean optIn){
		this.optIn = optIn;
	}

	public boolean isOptIn(){
		return optIn;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	@Override
 	public String toString(){
		return 
			"UserProfile{" + 
			"zip = '" + zip + '\'' + 
			",country = '" + country + '\'' + 
			",address3 = '" + address3 + '\'' + 
			",address2 = '" + address2 + '\'' + 
			",city = '" + city + '\'' + 
			",phone = '" + phone + '\'' + 
			",address1 = '" + address1 + '\'' + 
			",houseNumber = '" + houseNumber + '\'' + 
			",optIn = '" + optIn + '\'' + 
			",state = '" + state + '\'' + 
			"}";
		}
}