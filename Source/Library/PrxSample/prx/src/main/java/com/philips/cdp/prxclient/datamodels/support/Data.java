package com.philips.cdp.prxclient.datamodels.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by 310190678 on 28-Mar-16.
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
