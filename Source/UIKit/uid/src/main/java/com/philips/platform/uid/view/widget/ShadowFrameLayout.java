/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.philips.platform.uid.R;

public class ShadowFrameLayout extends FrameLayout {
    private Drawable shadowDrawable;
    private int shadowTopOffset;

    public ShadowFrameLayout(Context context) {
        this(context, null, 0);
    }

    public ShadowFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.UIDShadowFrameLayout, 0, 0);

        setDefaultShadowDrawable(context, a);

        shadowDrawable.setCallback(this);
        shadowTopOffset = a.getDimensionPixelSize(R.styleable.UIDShadowFrameLayout_uidShadowOffset, getActionBarSize());

        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        shadowDrawable.setBounds(0, shadowTopOffset, w, shadowTopOffset + shadowDrawable.getIntrinsicHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        shadowDrawable.draw(canvas);
    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme()
                .obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return size;
    }

    private void setDefaultShadowDrawable(Context context, TypedArray array) {
        shadowDrawable = array.getDrawable(R.styleable.UIDShadowFrameLayout_uidShadowDrawable);

        if (shadowDrawable == null) {
            int primary = array.getInt(R.styleable.UIDShadowFrameLayout_uidShadowType, 0);
            int drawableID = primary == 0 ? R.drawable.uid_navigation_shadow_primary : R.drawable.uid_navigation_shadow_secondary;
            shadowDrawable = ResourcesCompat.getDrawable(getResources(), drawableID, context.getTheme());
        }
    }
}