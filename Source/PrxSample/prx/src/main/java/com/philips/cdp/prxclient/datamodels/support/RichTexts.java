package com.philips.cdp.prxclient.datamodels.support;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310190678 on 28-Mar-16.
 */

public class RichTexts {

@SerializedName("richText")
@Expose
private List<RichText> richText = new ArrayList<RichText>();

/**
*
* @return
* The richText
*/
public List<RichText> getRichText() {
return richText;
}

/**
*
* @param richText
* The richText
*/
public void setRichText(List<RichText> richText) {
this.richText = richText;
}

}
