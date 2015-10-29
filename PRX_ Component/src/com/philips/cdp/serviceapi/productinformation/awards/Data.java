package com.philips.cdp.serviceapi.productinformation.awards;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Data {

@SerializedName("awards")
@Expose
private List<Award> awards = new ArrayList<Award>();

/**
* No args constructor for use in serialization
* 
*/
public Data() {
}

/**
* 
* @param awards
*/
public Data(List<Award> awards) {
this.awards = awards;
}

/**
* 
* @return
* The awards
*/
public List<Award> getAwards() {
return awards;
}

/**
* 
* @param awards
* The awards
*/
public void setAwards(List<Award> awards) {
this.awards = awards;
}

public Data withAwards(List<Award> awards) {
this.awards = awards;
return this;
}

}
