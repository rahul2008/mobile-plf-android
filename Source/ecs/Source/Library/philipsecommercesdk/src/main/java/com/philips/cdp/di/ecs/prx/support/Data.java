package com.philips.cdp.di.ecs.prx.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *Data Model class.
 */

public class Data {

@SerializedName("richTexts")
@Expose
private RichTexts richTexts;

/**
*
* @return
* The richTexts
*/
public RichTexts getRichTexts() {
return richTexts;
}

/**
*
* @param richTexts
* The richTexts
*/
public void setRichTexts(RichTexts richTexts) {
this.richTexts = richTexts;
}

}
