package com.philips.cdp.serviceapi.productinformation.summary;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Brand {

@SerializedName("brandLogo")
@Expose
private String brandLogo;

/**
* 
* @return
* The brandLogo
*/
public String getBrandLogo() {
return brandLogo;
}

/**
* 
* @param brandLogo
* The brandLogo
*/
public void setBrandLogo(String brandLogo) {
this.brandLogo = brandLogo;
}

}

