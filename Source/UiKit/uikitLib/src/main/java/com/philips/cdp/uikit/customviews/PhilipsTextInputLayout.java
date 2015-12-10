package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.R.color;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsTextInputLayout extends LinearLayout{
    View view ;
    private Validator validator=null;
    private boolean isErrorShown = false;
    private boolean isErrorFocusFlag = false;
    boolean isFocused = false;


    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(final View view, final boolean hasFocus) {
            isFocused = hasFocus;
            EditText editText = (EditText)view;
            String textToBeValidated = editText.getText().toString();
            LinearLayout parent = (LinearLayout)editText.getParent();//2
            int indexofParent = indexOfChild(parent);

            if(validator != null )
            {
                validator.validate(editText, hasFocus);
            }

        /*
            Handle Focus based color setting
        */
            if(hasFocus){
                highLightErrorView(indexofParent, getFocusedColor(),"base");
                highLightTextFeilds(parent,R.color.uikit_philips_very_dark_blue);
            }else{
                highLightErrorView(indexofParent, R.color.uikit_enricher4,"Orange");
                highLightTextFeilds(parent, R.color.uikit_enricher4);
            }
        }
    };

    public PhilipsTextInputLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    private int getFocusedColor(){
        TypedArray array = getContext().obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int dividercolor = array.getColor(R.styleable.PhilipsUIKit_veryLightColor, 0);
        array.recycle();
        if(isFocused){
            return dividercolor;
        }else{
            return getResources().getColor(R.color.uikit_enricher4);
        }
    }

    private int getBaseColor(){

        TypedArray array = getContext().obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int basecolor = array.getColor(R.styleable.PhilipsUIKit_baseColor, 0);
        array.recycle();
        return basecolor;
    }

    private View createNewErrorView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View errorView = inflater.inflate(R.layout.uikit_input_text_inline_error, null, false);

                errorView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        indexOfChild(errorView);
                        errorView.setVisibility(View.GONE);
                        isErrorShown = false;
                    }
                });
        return errorView;
    }


    public void showError(EditText edittext) {
        LinearLayout parent = (LinearLayout)edittext.getParent();
        int indexofParent = indexOfChild(parent);

        if (!isErrorShown) {
            addView(createNewErrorView(), indexofParent +1);

            //If error is shown please don't show again
            isErrorShown = true;
            isErrorFocusFlag = true;
        }

        int themecolor = getThemeColor();
        if(themecolor == getResources().getColor(color.uikit_philips_bright_orange)){
            highLightErrorView(indexofParent, R.color.uikit_enricher_red, "Orange");
            highLightTextFeilds(parent, R.color.uikit_enricher_red);
        }else {

            highLightErrorView(indexofParent, R.color.uikit_philips_bright_orange, "Orange");
            highLightTextFeilds(parent, R.color.uikit_philips_bright_orange);
        }
    }

    private void highLightErrorView(int indexofParent, int color, String isBase){

        /*
            Line HighLighting
         */
        try {
            View belowLine;
            View aboveLine = getChildAt(indexofParent - 1);

            belowLine = getChildAt(indexofParent + 1);
            if(belowLine instanceof RelativeLayout){
                belowLine = getChildAt(indexofParent + 2);
            }

            if (isBase.equalsIgnoreCase("Orange")) {
                aboveLine.setBackgroundColor(getResources().getColor(color));
            } else {
                aboveLine.setBackgroundColor(color);
            }

            if (isBase.equalsIgnoreCase("Orange")) {
                belowLine.setBackgroundColor(getResources().getColor(color));
            } else {
                belowLine.setBackgroundColor(color);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void highLightTextFeilds(LinearLayout parent, int color){

        EditText editText1 = (EditText) parent.getChildAt(0);
        EditText editText2 = (EditText) parent.getChildAt(1);

       // if(isBase.equalsIgnoreCase("Orange")) {
            editText1.setTextColor(getResources().getColor(color));
        //}else{
          //  editText1.setTextColor(color);
        //}

//        if(isBase.equalsIgnoreCase("Orange")) {
            editText2.setTextColor(getResources().getColor(color));
  //      }else{
    //        editText2.setTextColor(color);
      //  }

    }


    private void setNumberofchild(LinearLayout parent){
        int count = parent.getChildCount();
        if(count>3){
            for(int i=count;i>=3;i--){
                removeView(parent.getChildAt(i));
            }
        }
    }

    private void drawLine(){
       // Toast.makeText(getContext(),"Draw line",Toast.LENGTH_SHORT).show();

        view = new View(getContext());
        LayoutParams layoutparams = new LayoutParams(LayoutParams.MATCH_PARENT,1);
        view.setLayoutParams(layoutparams);
        view.setBackgroundColor(getFocusedColor());
        addView(view);
    }

    private void removeLine(View view){
        //Toast.makeText(getContext(),"Remove Line",Toast.LENGTH_SHORT).show();
        removeView(view);
    }

    private int getThemeColor(){
        TypedArray a = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor});
        int themeBaseColor = a.getColor(0, getResources().getColor(R.color.uikit_philips_blue));
        a.recycle();
        return themeBaseColor;
    }


    public interface Validator {
        void validate(View EditText, boolean hasfocus);
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

//below is called
    @Override
    public void addView(final View child, final ViewGroup.LayoutParams params) {
        super.addView(child, params);

        EditText editText;
        for (int i = 0; i < ((ViewGroup)child).getChildCount(); i++) {
            View view= ((ViewGroup) child).getChildAt(i);
            if(view instanceof EditText) {
                editText = (EditText)view;
                editText.setFocusable(true);
                editText.setFocusableInTouchMode(true);
                editText.setOnFocusChangeListener(onFocusChangeListener);
                editText.setTextColor(getResources().getColor(R.color.uikit_enricher4));
            }
        }
       // child.setOnFocusChangeListener(onFocusChangeListener);
        drawLine();
        setDisabledTextFeild(child);
    }


    private void setDisabledTextFeild(View child) {
        EditText editText1 = (EditText) ((LinearLayout) child).getChildAt(0);
        EditText editText2 = (EditText) ((LinearLayout) child).getChildAt(1);
        boolean isEnabled = editText1.isEnabled();
        if (isEnabled == false) {
            LinearLayout parent = ((LinearLayout) ((LinearLayout) child).getParent());
            View line = parent.getChildAt(1);
            line.setBackgroundColor(getResources().getColor(R.color.uikit_enricher4));
            child.setBackgroundColor(getResources().getColor(R.color.uikit_enricher7));
            editText1.setTextColor(getResources().getColor(R.color.uikit_enricher4));
            editText2.setTextColor(getResources().getColor(R.color.uikit_enricher4));
            editText1.setHintTextColor(getResources().getColor(R.color.uikit_enricher4));
            editText2.setHintTextColor(getResources().getColor(R.color.uikit_enricher4));
            editText1.setFocusable(false);
            editText2.setFocusable(false);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();

    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
