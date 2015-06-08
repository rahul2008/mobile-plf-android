/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.
Project           : Registration
Description       : Custom view for text loading different type face
----------------------------------------------------------------------------*/

package com.philips.cl.di.reg.ui.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.reg.ui.utils.FontLoader;

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
