package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
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

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsTextInputLayout extends LinearLayout {
    final String TAG = PhilipsTextInputLayout.class.getSimpleName();
    private Validator validator = null;
    boolean isFocused = false;
    private Set<Integer> set = new ConcurrentSkipListSet<Integer>();
    int mThemeBaseColor;
    int mFocusedColor;
    int mOrangeColor;
    int mEnricher4;
    int mEnricher7;
    int mEnricher_red;
    int mDarkBlue;

    public interface Validator {
        void validate(View EditText, boolean hasfocus);
    }

    public PhilipsTextInputLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        initColors();
    }
    
    private void  initColors(){
        mThemeBaseColor = getThemeColor();
        mFocusedColor = getFocusedColor();
        mOrangeColor = ContextCompat.getColor(getContext(), color.uikit_philips_bright_orange);
        mEnricher4 = ContextCompat.getColor(getContext(), color.uikit_enricher4);
        mEnricher7 = ContextCompat.getColor(getContext(), color.uikit_enricher7);
        mEnricher_red = ContextCompat.getColor(getContext(), color.uikit_enricher_red);
        mDarkBlue = ContextCompat.getColor(getContext(), color.uikit_philips_very_dark_blue);
    }

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
                highLightTextFeilds(parent, mDarkBlue);
            } else {
                highLightErrorView(indexofParent, mEnricher4);
                highLightTextFeilds(parent, mEnricher4);
            }

            /*
                Error Color
             */
            retainErrorLayoutFocus();
        }
    };

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        super.onRestoreInstanceState(state);
        for(int i=0;i<this.getChildCount();i++){
            View view = getChildAt(i);
            if(view instanceof LinearLayout){
                LinearLayout linearLayout = (LinearLayout)view;
                for(int j=0; j< linearLayout.getChildCount();j++){
                    if(((View)linearLayout.getChildAt(j) instanceof EditText)){
                        EditText editText = (EditText)((ViewGroup) view).getChildAt(j);
                        if(TextUtils.isEmpty(editText.getText())){
                            highLightTextFeilds(linearLayout,mDarkBlue);
                        }
                    }
                }
            }
        }
    }

    private void retainErrorLayoutFocus() {
        try {
            for (Integer layout : set) {
                LinearLayout parent = (LinearLayout) getChildAt(layout);
                if (mThemeBaseColor == mOrangeColor) {
                    highLightErrorView(layout, mEnricher_red);
                    highLightTextFeilds(parent, mEnricher_red);
                } else {
                    highLightErrorView(layout, mOrangeColor);
                    highLightTextFeilds(parent, mOrangeColor);
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
            highLightTextFeilds(parent, mDarkBlue);
        } else {
            highLightErrorView(indexofParent, mEnricher4);
            highLightTextFeilds(parent, mEnricher4);
        }

        for(int i=0;i<parent.getChildCount();i++){
            View view = parent.getChildAt(i);
            if(view instanceof EditText){
                ((EditText)view).requestFocus();
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

    private void highLightTextFeilds(LinearLayout parent, int color) {
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
                LinearLayout parent = ((LinearLayout) ((LinearLayout) child).getParent());
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
}
