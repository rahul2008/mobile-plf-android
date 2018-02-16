package com.philips.platform.mya.mock;

import android.content.Context;
import android.util.AttributeSet;

import com.philips.platform.uid.view.widget.Label;

import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowView;

@Implements(Label.class)
public class LabelShadow extends ShadowView {

    public void __constructor__(Context context) {
        // no-op
    }

    public void __constructor__(Context context, AttributeSet attrs) {
        // no-op
    }
}