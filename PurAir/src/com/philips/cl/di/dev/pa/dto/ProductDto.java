package com.philips.cl.di.dev.pa.dto;

public class ProductDto {

	private String mProductName;
	private String mProductPrice;
	private String mAvailableFrom;
	private int mProductImg;
	
	public ProductDto(String productName, String productPrice, String availableFrom, int productImg) {
		mProductName=productName;
		mProductPrice=productPrice;
		mAvailableFrom=availableFrom;
		mProductImg=productImg;
	}
	
	public String getProductName(){
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
