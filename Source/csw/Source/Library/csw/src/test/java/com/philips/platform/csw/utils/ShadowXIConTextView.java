package com.philips.platform.csw.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.philips.cdp.registration.ui.customviews.XIconTextView;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowView;

@Implements(XIconTextView.class)
public class ShadowXIConTextView extends ShadowView{
    @Implementation
    public void __constructor__(Context context) {
        // NOP
    }
    @Implementation
    public void __constructor__(Context context, AttributeSet attrs) {
        // NOP
    }
    @Implementation
    public void __constructor__(Context context, AttributeSet attrs, int i) {
        // NOP
    }

    @Implementation
    private void applyAttributes(TextView view, String fontAssetName) {
        //
    }
}
