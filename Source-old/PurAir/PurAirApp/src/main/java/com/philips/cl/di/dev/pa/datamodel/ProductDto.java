package com.philips.cl.di.dev.pa.datamodel;

public class ProductDto {

	private int mProductName;
	private String mProductPrice;
	private String mAvailableFrom;
	private int mProductImg;
	
	public ProductDto(int buyPhilipsSmartAirPurifier, String productPrice, String availableFrom, int productImg) {
		mProductName=buyPhilipsSmartAirPurifier;
		mProductPrice=productPrice;
		mAvailableFrom=availableFrom;
		mProductImg=productImg;
	}
	
	public int getProductName(){
		return mProductName;
	}
	
	public String getProductPrice(){
		return mProductPrice;
	}
	
	public String getAvailability(){
		return mAvailableFrom;
	}
	
	public int getProductImg(){
		return mProductImg;
	}
}
