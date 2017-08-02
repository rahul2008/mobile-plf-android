/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.*;

import com.philips.cdp.registration.ui.utils.FontLoader;

public class XButton extends Button {

	private static final String XMLNS = "http://reg.lib/schema";

	public XButton(Context context) {
		super(context);
	}

	public XButton(Context context, AttributeSet attrs) {
		super(context, attrs);

		String fontAssetName = attrs.getAttributeValue(XButton.XMLNS, "fontAssetName");
		applyAttributes(this, context, fontAssetName);
	}

	private void applyAttributes(TextView view, Context context, String fontAssetName) {
		FontLoader.getInstance().setTypeface(view, fontAssetName);
	}

	public void setTypeface(String fontAssetName) {
		FontLoader.getInstance().setTypeface(this, fontAssetName);
	}
}
