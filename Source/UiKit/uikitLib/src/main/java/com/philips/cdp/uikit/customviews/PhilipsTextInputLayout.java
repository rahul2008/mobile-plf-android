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
import android.widget.Toast;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.R.color;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsTextInputLayout extends LinearLayout{
    View view ;
    private Validator validator;
    private boolean isErrorShown = false;
    boolean isFocused = false;

    OnFocusChangeListener listener = new OnFocusChangeListener() {

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                Toast.makeText(getContext(),"On Focuss", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(),"No Focus", Toast.LENGTH_SHORT).show();
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
            return getResources().getColor(color.uikit_enricher4);
        }
    }

   /* private int getBaseColor(){

        TypedArray array = getContext().obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int basecolor = array.getColor(R.styleable.PhilipsUIKit_baseColor, 0);
        array.recycle();
        return basecolor;
    }*/

    private View createNewErrorView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View errorView = inflater.inflate(R.layout.uikit_input_text_inline_error, null, false);

                errorView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                       /* highLightErrorView(indexOfChild((LinearLayout) errorView.getParent()), getFocusedColor(), "base");
                        EditText text;
                        text = (EditText) ((LinearLayout) ((LinearLayout) errorView.getParent()).getChildAt(0)).getChildAt(0);
                        highLightTextFeilds(text, getBaseColor(), "base");*/
                        indexOfChild(errorView);
                        errorView.setVisibility(View.GONE);
                        isErrorShown = false;
                      //  removeView(errorView);
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
        }

        int themecolor = getThemeColor();

        if(themecolor == getResources().getColor(color.uikit_philips_bright_orange)){
            highLightErrorView(indexofParent, color.uikit_enricher_red, "Orange");
            highLightTextFeilds(parent, color.uikit_enricher_red, "Orange");
        }else {
            highLightErrorView(indexofParent, color.uikit_philips_bright_orange, "Orange");
            highLightTextFeilds(parent, color.uikit_philips_bright_orange, "Orange");
        }
    }

    private void highLightErrorView(int indexofParent, int color, String isBase){

        /*
            Line HighLighting
         */
        View aboveLine = getChildAt(indexofParent-1);
        View belowLine = getChildAt(indexofParent+2);

        if(isBase.equalsIgnoreCase("Orange")) {
            aboveLine.setBackgroundColor(getResources().getColor(color));
        }else{
            aboveLine.setBackgroundColor(color);
        }

        if(isBase.equalsIgnoreCase("Orange")) {
            belowLine.setBackgroundColor(getResources().getColor(color));
        }else {
            belowLine.setBackgroundColor(color);
        }
    }

    private void highLightTextFeilds(LinearLayout parent, int color, String isBase){

        EditText editText1 = (EditText) parent.getChildAt(0);
        EditText editText2 = (EditText) parent.getChildAt(1);

        if(isBase.equalsIgnoreCase("Orange")) {
            editText1.setTextColor(getResources().getColor(color));
        }else{
            editText1.setTextColor(color);
        }

        if(isBase.equalsIgnoreCase("Orange")) {
            editText2.setTextColor(getResources().getColor(color));
        }else{
            editText2.setTextColor(color);
        }

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
        boolean validate(String inputToBeValidated);
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }
//below is called
    @Override
    public void addView(final View child, final ViewGroup.LayoutParams params) {
        super.addView(child, params);

        child.setFocusable(true);
        child.setFocusableInTouchMode(true);
        child.setOnFocusChangeListener(new OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(getContext(), "On Focuss " +v.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "No Focus", Toast.LENGTH_SHORT).show();
                }
            }
        });
        child.setClickable(true);
        child.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                Toast.makeText(getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        child.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                Toast.makeText(getContext(), "On touch", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

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
            editText1.setTextColor(getResources().getColor(color.uikit_enricher4));
            editText2.setTextColor(getResources().getColor(color.uikit_enricher4));
            editText1.setHintTextColor(getResources().getColor(color.uikit_enricher4));
            editText2.setHintTextColor(getResources().getColor(color.uikit_enricher4));
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
