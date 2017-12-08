/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
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
import com.philips.platform.uid.utils.UIDUtils;

/**
 * Top level layout to provide shadows as per DLS specs below action bar if used as primary shadowType.
 *
 * <p>
 *     If AppBarLayout is used, elevation attribute must be set to 0, failing which results in 2 level shadows.
 * </p>
 *
 *
 *  <p>The attributes mapping follows below table.</p>
 * <table border="2" width="85%" align="center" cellpadding="5">
 * <thead>
 * <tr><th>ResourceID</th> <th>Configuration</th></tr>
 * </thead>
 * <p>
 * <tbody>
 * <tr>
 * <td rowspan="1">uidShadowOffset</td>
 * <td rowspan="1">Offset for the shadow. Default value is actionBar height.</td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidShadowDrawable</td>
 * <td rowspan="1">Drawable to be used as shadow drawable</td>
 * </tr>
 * <tr>
 * <td rowspan="1">uidShadowType</td>
 * <td rowspan="1">Enum value. 1. primary-> Top offset is same as actionBar Height<br>
 *                             2. secondary-> Top offset defaults to 0</td>
 * </tr>
 * <p>
 * </tbody>
 * <p>
 * </table>
 *
 */
public class NavigationContainer extends FrameLayout {
    private Drawable shadowDrawable;
    private int shadowTopOffset;
    private int width;

    public NavigationContainer(Context context) {
        this(context, null, 0);
    }

    public NavigationContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavigationContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.UIDNavigationContainer, 0, 0);

        setDefaultShadowDrawable(context, a);

        shadowDrawable.setCallback(this);
        shadowTopOffset = a.getDimensionPixelSize(R.styleable.UIDNavigationContainer_uidShadowOffset, UIDUtils.getActionBarSize(context));

        setWillNotDraw(shadowDrawable == null);//Necessary to draw the shadow
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        updateShadowDrawableBounds();
    }

    private void updateShadowDrawableBounds() {
        shadowDrawable.setBounds(0, shadowTopOffset, width, shadowTopOffset + shadowDrawable.getIntrinsicHeight());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        shadowDrawable.draw(canvas);
    }

    private void setDefaultShadowDrawable(Context context, TypedArray array) {
        shadowDrawable = array.getDrawable(R.styleable.UIDNavigationContainer_uidShadowDrawable);

        if (shadowDrawable == null) {
            int primary = array.getInt(R.styleable.UIDNavigationContainer_uidShadowType, 0);
            getDefaultDrawable(context, primary);
            setShadowTopOffset(0);
        }
    }

    private void getDefaultDrawable(final Context context, final int primary) {
        int drawableID = primary == 0 ? R.drawable.uid_navigation_dropshadow_primary : R.drawable.uid_navigation_dropshadow_secondary;
        shadowDrawable = ResourcesCompat.getDrawable(getResources(), drawableID, context.getTheme());
    }

    /**
     * Offset from where the shadow drawable should be drawn.
     * @param drawableTopOffset Top offset to be set
     */
    public void setShadowTopOffset(final int drawableTopOffset) {
        this.shadowTopOffset = drawableTopOffset;
        updateShadowDrawableBounds();
    }
}