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
import android.support.v4.view.MarginLayoutParamsCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.R.color;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <b></b> InlineForms is UI Component providing real time Inline Form to the User</b>
 * <p/>
 * <b></b> Entry In each Inline Form is an Item in the Layout</b>
 * <p/>
 * <b></b> In Order to Enter an Entry in The Layout Use:</b>
 * <p/>
 * <pre>&lt;LinearLayout
 * android:id="@+id/firstnamelayouthorzontal"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:orientation="horizontal"/&gt;
 *
 * &lt;TextView
 * android:id="@+id/firstname"
 * style="@style/PhilipsTextInputLayoutTextViewStyle"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:layout_weight="0.5"
 * android:hint="Last Name"
 * android:text="Last Name"/&gt;
 *
 * &lt;EditText
 * android:id="@+id/firstnamevalue"
 * style="@style/PhilipsTextInputLayoutStyleEnabled"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:layout_weight="0.5"
 * android:hint="Jones"/&gt;
 *
 * &lt;LinearLayout/&gt;
 *
 *  </pre>
 * <p/>
 * <b></b> Please Note the Styles:</b>
 * <p/>
 * <b></b> For Disabled Edit Text Feilds: style="@style/PhilipsTextInputLayoutStyleDisabled"</b>
 * <p/>
 * <b></b> For Enabled Edit Text Feilds: style="@style/PhilipsTextInputLayoutStyleEnabled"</b>
 * <p/>
 * <b></b> For Text Feilds: style="@style/PhilipsTextInputLayoutTextViewStyle"</b>
 * <p/>
 * <b></b> Users Should give atleast one View withing the Form either TextView or EditText"</b>
 * <p/>
 * <b></b> In case the Feild has to be validated - Users Should set the Validator for that Layout"</b>
 * <pre>
 *
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
    public static final String ERROR_INDEX_MESSAGE_KEY = "ERROR_INDEX_MESSAGE_KEY";
    public static final String INSTANCE_STATE_KEY = "INSTANCE_STATE_KEY";

    public interface Validator {
        void validate(View EditText, boolean hasfocus);
    }

    private static final String VIEW_STATE_MAPPER_KEY = "VIEW_STATE_MAPPER_KEY";
    boolean isFocused = false;
    private int mThemeBaseColor;
    private int mFocusedColor;
    private int mOrangeColor;
    private int mEnricher4;
    private int mEnricher7;
    private int mEnricher_red;
    private int mDarkBlue;
    private Validator validator = null;
    private String errorText;
    private HashMap<Integer, String> mErrorLookUP = new HashMap<>();
    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(final View view, final boolean hasFocus) {
            isFocused = hasFocus;
            EditText editText = (EditText) view;
            LinearLayout rowItemParent = (LinearLayout) editText.getParent();//2
            int rowItemParentIndex = indexOfChild(rowItemParent);

            if (validator != null) {
                validator.validate(editText, hasFocus);
            }

        /*
            Handle Focus based color setting
        */
            if (hasFocus) {
                highLightErrorView(rowItemParentIndex, mFocusedColor);
                highLightTextField(rowItemParent, mDarkBlue);
            } else {
                highLightErrorView(rowItemParentIndex, mEnricher4);
                highLightTextField(rowItemParent, mEnricher4);
            }

            /*
                Error Color
             */
            retainErrorLayoutFocus();
        }
    };
    private OnClickListener mErrorLayoutClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            hideErrorViews((ViewGroup) v);

            //Row item is one layout above the error frame layout
            int rowItemLayoutIndex = indexOfChild((View) v.getParent()) - 1;
            mErrorLookUP.remove(rowItemLayoutIndex);
            resetColor(rowItemLayoutIndex);

            retainErrorLayoutFocus();
        }
    };
    private HashMap<Integer, Boolean> viewStateMap = new HashMap<>();

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
        bundle.putParcelable(INSTANCE_STATE_KEY, super.onSaveInstanceState());
        bundle.putSerializable(ERROR_INDEX_MESSAGE_KEY, mErrorLookUP);
        bundle.putSerializable(VIEW_STATE_MAPPER_KEY, viewStateMap);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            state = bundle.getParcelable(INSTANCE_STATE_KEY);
            final Map<Integer, Boolean> stateMapper = (Map) bundle.getSerializable(VIEW_STATE_MAPPER_KEY);

            retainRowEnabledState(stateMapper);

            retainErrorTexts(bundle);
        }
        super.onRestoreInstanceState(state);
    }

    private void retainErrorTexts(final Bundle bundle) {
        HashMap<Integer, String> errorMap = (HashMap<Integer, String>) bundle.getSerializable(ERROR_INDEX_MESSAGE_KEY);
        if (!errorMap.isEmpty()) {
            Iterator<Integer> errorIndexIterator = errorMap.keySet().iterator();
            while (errorIndexIterator.hasNext()) {
                int rowItemLayoutIndex = errorIndexIterator.next();
                errorText = errorMap.get(rowItemLayoutIndex);
                showErrorViews(rowItemLayoutIndex);
            }
        }
    }

    private void retainRowEnabledState(final Map<Integer, Boolean> stateMapper) {
        for (Integer viewId : stateMapper.keySet()) {
            final Boolean isEnabled = stateMapper.get(viewId);
            View view = findViewById(viewId);
            if (isEnabled) {
                enableRow(view);
            } else {
                disableRow(view);
            }
        }
    }

    private void retainErrorLayoutFocus() {
        Iterator<Integer> errorLayoutIndices = mErrorLookUP.keySet().iterator();
        while (errorLayoutIndices.hasNext()) {
            int layoutIndex = errorLayoutIndices.next();
            LinearLayout parent = (LinearLayout) getChildAt(layoutIndex);
            int color = isOrangeTheme() ? mEnricher_red : mOrangeColor;
            highLightErrorView(layoutIndex, color);
            highLightTextField(parent, color);
        }
    }

    private int getFocusedColor() {
        TypedArray array = getContext().obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int dividercolor = array.getColor(R.styleable.PhilipsUIKit_uikit_veryLightColor, 0);
        array.recycle();
        return dividercolor;
    }

    public void removeError(View editText) {
        int rowItemLayoutIndex = indexOfChild((View) editText.getParent());
        int errorGroupIndex = rowItemLayoutIndex + 1;
        ViewGroup errorGroup = (ViewGroup) getChildAt(errorGroupIndex);

        hideErrorViews(errorGroup);

        mErrorLookUP.remove(rowItemLayoutIndex);

        resetColor(rowItemLayoutIndex);
        retainErrorLayoutFocus();
    }

    private void hideErrorViews(ViewGroup errorGroup) {
        View errorIconLayout = errorGroup.findViewById(R.id.error_image);
        errorIconLayout.setVisibility(View.GONE);

        TextView errorTextView = (TextView) ((ViewGroup) errorIconLayout.getParent()).findViewById(R.id.inline_error_text);
        errorTextView.setVisibility(View.GONE);
    }

    private void resetColor(int indexofParent) {
        LinearLayout parent = (LinearLayout) getChildAt(indexofParent);
        final int errorTextColor = parent.hasFocus() ? mFocusedColor : mEnricher4;
        highLightErrorView(indexofParent, errorTextColor);
        highLightTextField(parent, parent.hasFocus() ? mDarkBlue : mEnricher4);

        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof EditText) {
                view.requestFocus();
            }
        }
    }

    public void showError(View edittext) {
        int indexofParent = indexOfChild((View) edittext.getParent());
        showErrorViews(indexofParent);
    }

    private void showErrorViews(int rowItemLayoutIndex) {
        ViewGroup errorView = (ViewGroup) getChildAt(rowItemLayoutIndex + 1);

        TextView errorTextView = (TextView) errorView.findViewById(R.id.inline_error_text);
        errorTextView.setText(errorText);
        errorTextView.setVisibility(View.VISIBLE);

        View errorLayout = errorView.findViewById(R.id.error_image);
        errorLayout.setVisibility(View.VISIBLE);
        errorLayout.setOnClickListener(mErrorLayoutClickListener);

        mErrorLookUP.put(rowItemLayoutIndex, errorText);
    }

    public void setErrorMessage(String text) {
        errorText = text;
    }

    private void highLightErrorView(int rowItemLayoutIndex, int color) {

        View aboveLine = getDividerView(getChildAt(rowItemLayoutIndex - 1));
        View belowLine = getDividerView(getChildAt(rowItemLayoutIndex + 1));

        int themeDependentColor = color;
        if (mErrorLookUP.containsKey(rowItemLayoutIndex) && isOrangeTheme()) {
            themeDependentColor = mOrangeColor;
        }

        if (aboveLine != null) {
            aboveLine.setBackgroundColor(themeDependentColor);
        }

        if (belowLine != null) {
            belowLine.setBackgroundColor(themeDependentColor);
        }
    }

    private boolean isOrangeTheme() {
        return mThemeBaseColor == mOrangeColor;
    }

    private void highLightTextField(LinearLayout parent, int color) {
        TextView titleText = null;
        EditText valueEditText = null;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);

            if (view instanceof EditText) {
                valueEditText = (EditText) view;
            } else if (view instanceof TextView) {
                titleText = (TextView) view;
            }
        }
        int decoratedColor = calculateTextColor(parent, valueEditText, color);
        if (titleText != null) {
            titleText.setTextColor(decoratedColor);
        }

        if (valueEditText != null) {
            valueEditText.setTextColor(decoratedColor);
        }
    }

    private int calculateTextColor(LinearLayout parent, EditText valueEditText, int color) {
        int decoratedColor = color;
        boolean containError = mErrorLookUP.containsKey(indexOfChild(parent));
        if (containError && isOrangeTheme()) {
            decoratedColor = mEnricher_red;
        } else if (containError) {
            decoratedColor = mOrangeColor;
        } else if (valueEditText != null && !TextUtils.isEmpty(valueEditText.getText())) {
            decoratedColor = mDarkBlue;
        }
        return decoratedColor;
    }

    private void addGenericErrorLayout(int startMargin) {
        ViewGroup view = (ViewGroup) View.inflate(getContext(), R.layout.uikit_input_text_inline_error, null);

        RelativeLayout.LayoutParams crossMarkParams = (RelativeLayout.LayoutParams) view.getChildAt(0).getLayoutParams();
        crossMarkParams.setMarginStart(startMargin);
        View dividerView = view.findViewById(R.id.inlineForm_divider);
        dividerView.setBackgroundColor(isFocused ? mFocusedColor : mEnricher4);

        addView(view);
    }

    private int getThemeColor() {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_baseColor});
        int themeBaseColor = a.getColor(0, ContextCompat.getColor(getContext(), color.uikit_philips_blue));
        a.recycle();
        return themeBaseColor;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void addView(final View child, final ViewGroup.LayoutParams params) {
        super.addView(child, params);
        int startMargin = 0;

        if (child instanceof ViewGroup) {
            MarginLayoutParams textLayoutParams = (MarginLayoutParams) ((ViewGroup) child).getChildAt(0).getLayoutParams();
            if (textLayoutParams != null) {
                startMargin = MarginLayoutParamsCompat.getMarginStart(textLayoutParams);
            }
        }

        EditText editText;
        for (int i = 0; i < ((ViewGroup) child).getChildCount(); i++) {
            View view = ((ViewGroup) child).getChildAt(i);
            if (view instanceof EditText) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.setOnFocusChangeListener(onFocusChangeListener);
            }
        }

        addGenericErrorLayout(startMargin);
    }

    /**
     * This API can be used to disable the row
     *
     * @param rowLayout
     */
    public void disableRow(View rowLayout) {

        if (!(rowLayout instanceof ViewGroup)) {
            return;
        }
        viewStateMap.put(rowLayout.getId(), false);
        rowLayout.setFocusable(false);
        rowLayout.setBackgroundColor(mEnricher7);
        for (int i = 0; i < ((ViewGroup) rowLayout).getChildCount(); i++) {
            View view = ((ViewGroup) rowLayout).getChildAt(i);
            if (view instanceof ViewGroup) {
                disableRow(view);
            }
            view.setBackgroundColor(mEnricher7);
            view.setClickable(false);
            view.setEnabled(false);
            view.setFocusable(false);
            if (view instanceof TextView) {
                final TextView textView = (TextView) view;
                textView.setTextColor(mEnricher4);
                textView.setHintTextColor(mEnricher4);
            }
        }
    }

    /**
     * This api can be used to enable a row in inline form, if you wish to enable individual components inside it you can do so by calling this api.
     *
     * @param rowLayout
     */
    public void enableRow(View rowLayout) {
        viewStateMap.put(rowLayout.getId(), true);

        rowLayout.setFocusable(true);
        rowLayout.setBackground(getBackground());
        for (int i = 0; i < ((ViewGroup) rowLayout).getChildCount(); i++) {
            View view = ((ViewGroup) rowLayout).getChildAt(i);
            if (view instanceof ViewGroup) {
                enableRow(view);
            }
            view.setBackground(getBackground());
            view.setEnabled(true);
            view.setClickable(true);
            view.setFocusable(true);
        }
    }

    private View getDividerView(View view) {
        return view == null ? view : view.findViewById(R.id.inlineForm_divider);
    }
}