package com.philips.cdp.prodreg.model_request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attributes implements Serializable {

	@SerializedName("serialNumber")
	private String serialNumber;

	@SerializedName("productId")
	private String productId;

	@SerializedName("purchased")
	private String purchased;

	@SerializedName("catalog")
	private String catalog;

	@SerializedName("locale")
	private String locale;

	@SerializedName("sector")
	private String sector;

	@SerializedName("micrositeId")
	private int micrositeId;

	public void setSerialNumber(String serialNumber){
		this.serialNumber = serialNumber;
	}

	public String getSerialNumber(){
		return serialNumber;
	}

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setPurchased(String purchased){
		this.purchased = purchased;
	}

	public String getPurchased(){
		return purchased;
	}

	public void setCatalog(String catalog){
		this.catalog = catalog;
	}

	public String getCatalog(){
		return catalog;
	}

	public void setLocale(String locale){
		this.locale = locale;
	}

	public String getLocale(){
		return locale;
	}

	public void setSector(String sector){
		this.sector = sector;
	}

	public String getSector(){
		return sector;
	}

	public void setMicrositeId(int micrositeId){
		this.micrositeId = micrositeId;
	}

	public int getMicrositeId(){
		return micrositeId;
	}

	@Override
 	public String toString(){
		return 
			"Attributes{" + 
			"serialNumber = '" + serialNumber + '\'' + 
			",productId = '" + productId + '\'' + 
			",purchased = '" + purchased + '\'' + 
			",catalog = '" + catalog + '\'' + 
			",locale = '" + locale + '\'' + 
			",sector = '" + sector + '\'' + 
			",micrositeId = '" + micrositeId + '\'' + 
			"}";
		}
}