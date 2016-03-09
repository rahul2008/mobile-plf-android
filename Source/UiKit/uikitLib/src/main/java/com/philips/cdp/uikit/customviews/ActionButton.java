/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.UikitUtils;

/**
 * <b>Action Buttons are available in 2 shapes</b>
 * <br> <pre>
 *     1. Circle
 *     2. Square</pre>
 *  <b>Available pre-defined icons and its styles</b>
 *  <br><pre>
 *      1	> (Arrow)
 *      2	? (QuestionMark)
 *      3	+ (Plus)
 *      4	X (Cross)
 *      5	- (Minus)</pre>
 *      <br>
 *   <b>For Example to use Arrow with Circle and Square shape, we need to include below piece of code on layout resource : </b><br>
 *      <br>
 *       <pre>&lt;com.philips.cdp.uikit.CustomButton.PhilipsActionButton
 style="@style/Philips.ActionButton.Circle.Arrow"</b>/&gt

 &lt;com.philips.cdp.uikit.CustomButton.PhilipsActionButton
 style="@style/Philips.ActionButton.Square.Arrow"</b>/&gt</pre>

 <br>
 <b>Users can either define their own styles or layout xml’s with ActionButton Class with following custom attributes</b><br>
 <pre>
 1	<b>actionButtonShape</b> = Square / Circle of type String
 2	<b>actionButtonBgColor</b> for Background Color of type Integer
 3	<b>actionButtonBgColorPressed</b> for Pressed Background Color of type Integer
 4	<b>actionButtonImageDrawable</b> for Icon Background of type Drawable.</pre>
 <br>

 <b>Example to use in Layout</b>
 <br>
 <pre>&lt;com.philips.cdp.uikit.CustomButton.ActionButton
 android:layout_width="wrap_content"
 android:layout_height="wrap_content"
 custom:actionButtonShape="square"
 custom:actionButtonBgColor="#ffffff"
 custom:actionButtonBgColorPressed="#bbbbbbb"
 custom:actionButtonImageDrawable ="@drawable/plus"</b>/&gt</pre>

 <b>Example to use in Styles</b>
 <pre>&lt;style name="Philips.ActionButton.Circle"&gt
 &ltitem name="actionButtonShape"&gtcircle&lt/item&gt
 &ltitem name="android:minWidth"&gt@dimen/philips_action_button_radius&lt/item&gt
 &ltitem name="android:minHeight"&gt@dimen/philips_action_button_radius&lt/item&gt
 &ltitem name="actionButtonShape"&gtsquare&lt/item&gt
 &ltitem name="actionButtonImageDrawable"&gt@drawable/arrow&lt/item&gt
 &ltitem name=" actionButtonBgColor "&gt#ffffff&lt/item&gt
 &ltitem name=" actionButtonBgColorPressed "&gt#bbbbbbb&lt/item&gt
 &lt/style&gt</pre>

 <b>To Support Shadow find the below code for reference</b>
 <br>
 <br>
 <b> Example to use in Layout</b>
 <pre>&lt;com.philips.cdp.uikit.CustomButton.ActionButton
 android:layout_width="wrap_content"
 android:layout_height="wrap_content"
 custom:actionButtonShape="square"
 custom:actionButtonBgColor="#ffffff"
 custom:actionButtonBgColorPressed="#bbbbbbb"
 custom:actionButtonImageDrawable ="@drawable/plus"
 custom:actionButtonShadow= true /&gt;</pre>

 <b>Example to use in Styles</b>
 <pre>&ltstyle name="Philips.ActionButton.Circle"&gt
 &ltitem name="actionButtonShape"&gtcircle&lt/item&gt
 &ltitem name="android:minWidth"&gt@dimen/philips_action_button_radius&lt/item&gt
 &ltitem name="android:minHeight"&gt@dimen/philips_action_button_radius&lt/item&gt
 &ltitem name="actionButtonShape"&gtsquare&lt/item&gt
 &ltitem name="actionButtonImageDrawable"&gt@drawable/arrow&lt/item&gt
 &ltitem name=" actionButtonBgColor "&gt#ffffff&lt/item&gt
 &ltitem name=" actionButtonBgColorPressed "&gt#bbbbbbb&lt/item&gt
 &ltitem name=" actionButtonShadow"&gt true &lt/item&gt
 &lt/style&gt</pre>

 <b> To Support Action Icons kindly use the below code</b>
 <pre>&lt;com.philips.cdp.uikit.CustomButton.PhilipsActionButton
 android:layout_width="wrap_content"
 android:layout_height="wrap_content"
 style="@style/Philips.ActionButton.Circle.Float.Pencil"
 </b>/&gt</pre>


 *

 */
public class ActionButton extends ImageButton {

    private static final int SQUARE = 0;
    private static final int CIRCLE = 1;

    public ActionButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.uikit_actionButtonStyleRef);
    }

    public ActionButton(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context, attrs, defStyle);
    }

    private void initializeView(final Context context, final AttributeSet attrs, final int defStyle) {
        Resources resources = context.getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ActionButton, defStyle,
                R.style.Philips_ActionButton);

        int resID = getResID(typedArray);
        if (resID > 0)
            setImageDrawable(VectorDrawable.create(context, resID));

        addStates(getNormalStateDrawable(typedArray, resources), getPressedStateDrawable(typedArray, resources));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setShadowing(resources, typedArray);

        typedArray.recycle();

        setScaleType(ScaleType.CENTER);
    }

    private void setShadowing(Resources resources, TypedArray typedArray) {
        float shadowValue = 0;

        if (typedArray.getBoolean(R.styleable.ActionButton_uikit_actionButtonShadow, false))
            shadowValue = resources.getDimension(R.dimen.uikit_action_button_shadow_radius);

        setShadow(shadowValue);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setShadow(float value) {
        setElevation(value);
    }

    private int getResID(final TypedArray typedArray) {
        String resPath = typedArray.getString(R.styleable.ActionButton_uikit_actionButtonImageDrawable);
        if (resPath != null)
            return UikitUtils.getResourceID(resPath.substring(resPath.lastIndexOf("/") + 1, resPath.lastIndexOf(".")), R.drawable.class);
        else
            return 0;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private GradientDrawable getShapeDrawable(final TypedArray typedArray, final Resources resources) {
        GradientDrawable gradientDrawable;
        int shapeValue = typedArray.getInt(R.styleable.ActionButton_uikit_actionButtonShape, SQUARE);
        switch (shapeValue) {
            case SQUARE:
                gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_square);
                break;
            case CIRCLE:
                gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_circle);
                break;
            default:
                gradientDrawable = (GradientDrawable) resources.getDrawable(R.drawable.uikit_square);
                break;
        }
        return gradientDrawable;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to resources.getColor(): sticking with deprecated API for now
    private GradientDrawable getNormalStateDrawable(final TypedArray typedArray, final Resources resources) {
        GradientDrawable gradientDrawable = getShapeDrawable(typedArray, resources);
        int color = typedArray.getColor(R.styleable.ActionButton_uikit_actionButtonBgColor, resources.getColor(R.color.uikit_philips_bright_blue));
        gradientDrawable.setColor(color);
        gradientDrawable.mutate();
        return gradientDrawable;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to resources.getColor(): sticking with deprecated API for now
    private GradientDrawable getPressedStateDrawable(final TypedArray typedArray, final Resources resources) {
        GradientDrawable gradientDrawable = getShapeDrawable(typedArray, resources);
        int color = typedArray.getColor(R.styleable.ActionButton_uikit_actionButtonBgColorPressed, resources.getColor(R.color.uikit_philips_dark_blue));
        gradientDrawable.setColor(color);
        gradientDrawable.mutate();
        return gradientDrawable;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private void addStates(final GradientDrawable normal, final GradientDrawable pressed) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                pressed);
        states.addState(new int[]{},
                normal);
        setBackgroundDrawable(states);
    }
}