/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.R.color;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * <b></b> InlineForms is UI Component providing real time Inline Form to the User</b>
 * <p/>
 *  <b></b> Entry In each Inline Form is an Item in the Layout</b>
 *  <p/>
 *  <b></b> In Order to Enter an Entry in The Layout Use:</b>
 *
 * <pre>&lt;LinearLayout
 * android:id="@+id/firstnamelayouthorzontal"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:orientation="horizontal"/&gt;
 *
 *&lt;TextView
 * android:id="@+id/firstname"
 * style="@style/PhilipsTextInputLayoutTextViewStyle"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:layout_weight="0.5"
 * android:hint="Last Name"
 * android:text="Last Name"/&gt;
 *
 *&lt;EditText
 * android:id="@+id/firstnamevalue"
 * style="@style/PhilipsTextInputLayoutStyleEnabled"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:layout_weight="0.5"
 * android:hint="Jones"/&gt;
 *
 *&lt;LinearLayout/&gt;
 *
 *  </pre>
 *  <p/>
 *  <b></b> Please Note the Styles:</b>
 *  <p/>
 *  <b></b> For Disabled Edit Text Feilds: style="@style/PhilipsTextInputLayoutStyleDisabled"</b>
 *  <p/>
 *  <b></b> For Enabled Edit Text Feilds: style="@style/PhilipsTextInputLayoutStyleEnabled"</b>
 *  <p/>
 *  <b></b> For Text Feilds: style="@style/PhilipsTextInputLayoutTextViewStyle"</b>
 *  <p/>
 *  <b></b> Users Should give atleast one View withing the Form either TextView or EditText"</b>
 *  <p/>
 *  <b></b> In case the Feild has to be validated - Users Should set the Validator for that Layout"</b>
 *  <pre>
 *  <p/>
 * layout.setValidator(new InlineForms.Validator() {
 *      public void validate(View editText, boolean hasFocus) {
 *          if (editText.getId() == R.id.lastnamevalue && hasFocus == false) {
 *              boolean result = validateEmail(editText, hasFocus);
 *                  if (!result) {
 *                      layout.showError((EditText) editText);
 *                  }
 *          }
 *      }
 * });
 * </pre>
 */
public class InlineForms extends LinearLayout {
    final String TAG = InlineForms.class.getSimpleName();
    boolean isFocused = false;
    int mThemeBaseColor;
    int mFocusedColor;
    int mOrangeColor;
    int mEnricher4;
    int mEnricher7;
    int mEnricher_red;
    int mDarkBlue;
    private Validator validator = null;
    private String errorText =getResources().getString(R.string.invalid_email_format);
    private Set<Integer> set = new ConcurrentSkipListSet<Integer>();
    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(final View view, final boolean hasFocus) {
            isFocused = hasFocus;
            EditText editText = (EditText) view;
            LinearLayout parent = (LinearLayout) editText.getParent();//2
            int indexofParent = indexOfChild(parent);

            if (validator != null) {
                validator.validate(editText, hasFocus);
            }

        /*
            Handle Focus based color setting
        */
            if (hasFocus) {
                highLightErrorView(indexofParent, mFocusedColor);
                highLightTextField(parent, mDarkBlue);
            } else {
                highLightErrorView(indexofParent, mEnricher4);
                highLightTextField(parent, mEnricher4);
            }

            /*
                Error Color
             */
            retainErrorLayoutFocus();
        }
    };

    public InlineForms(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        initColors();
    }

    private void initColors() {
        mThemeBaseColor = getThemeColor();
        mFocusedColor = getFocusedColor();
        mOrangeColor = ContextCompat.getColor(getContext(), color.uikit_philips_bright_orange);
        mEnricher4 = ContextCompat.getColor(getContext(), color.uikit_enricher4);
        mEnricher7 = ContextCompat.getColor(getContext(), color.uikit_enricher7);
        mEnricher_red = ContextCompat.getColor(getContext(), color.uikit_enricher_red);
        mDarkBlue = ContextCompat.getColor(getContext(), color.uikit_philips_very_dark_blue);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcel = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        ArrayList<Integer> list = new ArrayList<>();
        list.addAll(set);
        bundle.putIntegerArrayList("Set", list);
        // ... save everything
        return bundle;
        //return  parcel;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            ArrayList<Integer> list = bundle.getIntegerArrayList("Set");
            // ... load everything
            state = bundle.getParcelable("instanceState");
            Set<Integer> restoreSet = new ConcurrentSkipListSet<Integer>(list);
            if (!restoreSet.isEmpty()) {
                Log.i(TAG, " In OnRestoreInstanceState = " + restoreSet.toString());
                for (int i : restoreSet
                        ) {
                    View layout = getChildAt(i);
                    if (layout instanceof LinearLayout) {
                        LinearLayout linearLayout = (LinearLayout) layout;
                        for (int j = 0; j < linearLayout.getChildCount(); j++) {
                            View view = linearLayout.getChildAt(j);
                            if (view instanceof EditText) {
                                showError((EditText) view);
                            }
                        }
                    }
                }
            }

        }

        for (int i = 0; i < this.getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) view;
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    if ((linearLayout.getChildAt(j) instanceof EditText)) {
                        EditText editText = (EditText) ((ViewGroup) view).getChildAt(j);
                        if (TextUtils.isEmpty(editText.getText())) {
                            highLightTextField(linearLayout, mDarkBlue);
                        }
                    }
                }
            }
        }

        super.onRestoreInstanceState(state);
    }

    private void retainErrorLayoutFocus() {
        try {
            for (Integer layout : set) {
                LinearLayout parent = (LinearLayout) getChildAt(layout);
                if (mThemeBaseColor == mOrangeColor) {
                    highLightErrorView(layout, mEnricher_red);
                    highLightTextField(parent, mEnricher_red);
                } else {
                    highLightErrorView(layout, mOrangeColor);
                    highLightTextField(parent, mOrangeColor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getFocusedColor() {
        TypedArray array = getContext().obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int dividercolor = array.getColor(R.styleable.PhilipsUIKit_veryLightColor, 0);
        array.recycle();
        return dividercolor;
    }

    private View createNewErrorView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View errorView = inflater.inflate(R.layout.uikit_input_text_inline_error, null, false);
        TextView tv= (TextView)errorView.findViewById(R.id.error_text);
        tv.setText(errorText);
        FrameLayout imageview = (FrameLayout) errorView.findViewById(R.id.error_image);

        imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                Log.i(TAG, "In On Click Listener the Set = " + set.toString());
                RelativeLayout parent = (RelativeLayout) view.getParent();
                int index = indexOfChild(parent);
                removeView(parent);
                set.remove(index - 1);
                resetcolor(index - 1);

                for (Object i : set
                        ) {
                    Integer indexOfParenthavingError = (Integer) i;
                    if (index <= indexOfParenthavingError) {
                        set.remove(indexOfParenthavingError);
                        set.add(indexOfParenthavingError - 1);
                    }
                }
                Log.i(TAG, "After In On Click Listener the Set = " + set.toString());
                retainErrorLayoutFocus();
            }
        });
        return errorView;
    }

    public void removeError(View editText) {
        LinearLayout parent = (LinearLayout) editText.getParent();
        int indexofparent = indexOfChild(parent);
        View errorview = getChildAt(indexofparent + 1);

        if (errorview instanceof RelativeLayout) {
            int index = indexOfChild(errorview);
            removeView(errorview);
            boolean isremoved = set.remove(index - 1);
            resetcolor(index - 1);

            for (Object i : set
                    ) {
                Integer indexOfParenthavingError = (Integer) i;
                if (index <= indexOfParenthavingError) {
                    set.remove(indexOfParenthavingError);
                    set.add(indexOfParenthavingError - 1);
                }
            }
            Log.i(TAG, "After In On Click Listener the Set = " + set.toString());
            retainErrorLayoutFocus();
        }
    }

    private void resetcolor(int indexofParent) {
        LinearLayout parent = (LinearLayout) getChildAt(indexofParent);
        if (parent.hasFocus()) {
            highLightErrorView(indexofParent, mFocusedColor);
            highLightTextField(parent, mDarkBlue);
        } else {
            highLightErrorView(indexofParent, mEnricher4);
            highLightTextField(parent, mEnricher4);
        }

        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof EditText) {
                view.requestFocus();
            }
        }
        /*
        View view = getFocusedChild();
        try {
            LinearLayout layout = null;
            int index = -1;
            if (view instanceof LinearLayout) {
                layout = (LinearLayout) view;
                index = indexOfChild(layout);
                highLightErrorView(index, mFocusedColor);
                highLightTextFeilds(layout, mDarkBlue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public void showError(EditText edittext) {
        Log.i(TAG, "In Show Error the Set = " + set.toString());

        LinearLayout parent = (LinearLayout) edittext.getParent();
        int indexofParent = indexOfChild(parent);

        if (!(set.contains(indexofParent))) {
            View errorView = createNewErrorView();

            addView(errorView, indexofParent + 1);
            set.add(indexofParent);
            int errorIndex = indexOfChild(errorView);
            for (Object i : set) {
                Integer indexofErrorParent = (Integer) i;
                if (errorIndex <= indexofErrorParent) {
                    set.remove(indexofErrorParent);
                    set.add(indexofErrorParent + 1);
                }
            }
            Log.i(TAG, "After In Show Error the Set = " + set.toString());
        }
    }
public void setErrorMessage(String text)
{
    errorText=text;

}
    private void highLightErrorView(int indexofParent, int color) {

        /*
            Line HighLighting
         */
        try {
            View belowLine;
            View aboveLine = getChildAt(indexofParent - 1);
            belowLine = getChildAt(indexofParent + 1);

            if (belowLine instanceof RelativeLayout) {
                belowLine = getChildAt(indexofParent + 2);
            }

            aboveLine.setBackgroundColor(color);
            belowLine.setBackgroundColor(color);

            if (set.contains(indexofParent)) {
                if (mThemeBaseColor == mOrangeColor) {
                    aboveLine.setBackgroundColor(mEnricher_red);
                    belowLine.setBackgroundColor(mEnricher_red);
                } else {
                    aboveLine.setBackgroundColor(mOrangeColor);
                    belowLine.setBackgroundColor(mOrangeColor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void highLightTextField(LinearLayout parent, int color) {
        TextView editText1 = null;
        EditText editText2 = null;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);

            if (view instanceof EditText) {
                editText2 = (EditText) parent.getChildAt(1);
            } else if (view instanceof TextView) {
                editText1 = (TextView) parent.getChildAt(0);
            }
        }

        try {

            if (TextUtils.isEmpty(editText2.getText())) {
                editText1.setTextColor(color);
                editText2.setTextColor(color);
            } else {
                editText1.setTextColor(mDarkBlue);
                editText2.setTextColor(mDarkBlue);

                if (set.contains(indexOfChild(parent))) {
                    if (mThemeBaseColor == mOrangeColor) {
                        editText1.setTextColor(mEnricher_red);
                        editText2.setTextColor(mEnricher_red);
                    } else {
                        editText1.setTextColor(mOrangeColor);
                        editText2.setTextColor(mOrangeColor);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawLine() {
        View view = new View(getContext());
        int height = (int) getContext().getResources().getDimension(R.dimen.uikit_view_height);

        LayoutParams layoutparams = new LayoutParams(LayoutParams.MATCH_PARENT, height);
        view.setLayoutParams(layoutparams);
        if (isFocused)
            view.setBackgroundColor(mFocusedColor);
        else
            view.setBackgroundColor(mEnricher4);
        addView(view);
    }

    private int getThemeColor() {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor});
        int themeBaseColor = a.getColor(0, ContextCompat.getColor(getContext(), R.color.uikit_philips_blue));
        a.recycle();
        return themeBaseColor;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void addView(final View child, final ViewGroup.LayoutParams params) {
        super.addView(child, params);

        EditText editText;
        LinearLayout layout = (LinearLayout) child;

        for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
            View view = ((ViewGroup) child).getChildAt(i);
            if (view instanceof EditText) {
                editText = (EditText) view;
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.setOnFocusChangeListener(onFocusChangeListener);
                editText.setTextColor(mEnricher4);
            }
        }

        drawLine();
        setDisabledTextFeild(child);
    }

    private void setDisabledTextFeild(View child) {
        TextView editText1 = null;
        EditText editText2 = null;

        for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
            View view = ((ViewGroup) child).getChildAt(i);

            if (view instanceof EditText) {
                editText2 = (EditText) ((LinearLayout) child).getChildAt(1);
            } else if (view instanceof TextView) {
                editText1 = (TextView) ((LinearLayout) child).getChildAt(0);
            }
        }
        try {
            boolean isEnabled = editText2.isEnabled();
            if (isEnabled == false) {
                LinearLayout parent = ((LinearLayout) child.getParent());
                View line = parent.getChildAt(1);
                line.setBackgroundColor(mEnricher4);
                child.setBackgroundColor(mEnricher7);
                editText1.setTextColor(mEnricher4);
                editText2.setTextColor(mEnricher4);
                editText1.setHintTextColor(mEnricher4);
                editText2.setHintTextColor(mEnricher4);
                editText1.setFocusable(false);
                editText2.setFocusable(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Validator {
        void validate(View EditText, boolean hasfocus);
    }
}
