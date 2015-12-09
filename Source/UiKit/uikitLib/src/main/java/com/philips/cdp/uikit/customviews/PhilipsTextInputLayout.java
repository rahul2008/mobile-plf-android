package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;

import android.graphics.Rect;
import android.os.Parcelable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    View errorView;
    boolean isFocused = false;
    //int dividercolor;

    public PhilipsTextInputLayout(final Context context) {
        super(context);
    }

    public PhilipsTextInputLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        initErrorView();
        //dividercolor = getinitialColor();
    }

    /*@Override
    protected void onFocusChanged(final boolean gainFocus, final int direction, final Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

    }*/

    private int getinitialColor(){
        TypedArray array = getContext().obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int dividercolor = array.getColor(R.styleable.PhilipsUIKit_veryLightColor, 0);
        array.recycle();
        if(isFocused){
            return dividercolor;
        }else{
            return getResources().getColor(color.uikit_enricher4);
        }
    }

    private int getBaseColor(){

        TypedArray array = getContext().obtainStyledAttributes(R.styleable.PhilipsUIKit);
        int basecolor = array.getColor(R.styleable.PhilipsUIKit_baseColor, 0);
        array.recycle();
        return basecolor;
    }

    private void initErrorView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        errorView = inflater.inflate(R.layout.uikit_input_text_inline_error, null, false);
        errorView.setId(R.id.errorlayout);

                errorView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        highLightLines((LinearLayout) errorView.getParent(), getinitialColor(), "base");
                        EditText text;
                        text = (EditText) ((LinearLayout) ((LinearLayout) errorView.getParent()).getChildAt(0)).getChildAt(0);
                        highLightTextFeilds(text, getBaseColor(), "base");
                        setErrorMessageVisibilty(View.GONE);
                    }
                });
    }

    void setErrorMessageVisibilty(int visibilty){
            errorView.setVisibility(visibilty);
            isErrorShown = false;
    }



    public void showError(EditText edittext) {
        LinearLayout parent = ((LinearLayout) ((LinearLayout) edittext.getParent()).getParent());
        if (!isErrorShown) {
            removeLine(parent.getChildAt(1));
            parent.addView(errorView, 1);
            drawLine();
            setErrorMessageVisibilty(View.VISIBLE);
            /*
                If error is shown please don't show again
             */
            isErrorShown = true;
        }

        setNumberofchild(parent);

        int themecolor = getThemeColor();
        if(themecolor == getResources().getColor(color.uikit_philips_bright_orange)){
            highLightLines(parent, color.uikit_enricher_red, "Orange");
            highLightTextFeilds(edittext, color.uikit_enricher_red, "Orange");
        }else {

            highLightLines(parent, color.uikit_philips_bright_orange, "Orange");
            highLightTextFeilds(edittext, color.uikit_philips_bright_orange, "Orange");
        }
    }

    private void highLightTextFeilds(EditText editText, int color, String isBase){
        LinearLayout parent = (LinearLayout)editText.getParent();
        if(isBase.equalsIgnoreCase("Orange")) {
            ((EditText) (parent.getChildAt(0))).setTextColor(getResources().getColor(color));
        }else{
            ((EditText) (parent.getChildAt(0))).setTextColor(color);
        }

        if(isBase.equalsIgnoreCase("Orange")) {
            ((EditText) (parent.getChildAt(1))).setTextColor(getResources().getColor(color));
        }else{
            ((EditText) (parent.getChildAt(1))).setTextColor(color);
        }
        //((EditText)(parent.getChildAt(1))).setTextColor(getResources().getColor(color));

    }

    private void highLightLines(LinearLayout parent, int color, String isBase){
        View highlightchild = parent.getChildAt(2);
        int i = parent.indexOfChild(highlightchild);

        if(isBase.equalsIgnoreCase("Orange")) {
            highlightchild.setBackgroundColor(getResources().getColor(color));
        }else{
            highlightchild.setBackgroundColor(color);
        }

        LinearLayout parentsparent = (LinearLayout)parent.getParent();
        LinearLayout parentbefore = (LinearLayout)parentsparent.getChildAt(i-1);

        int count = parentbefore.getChildCount();
        int beforelineindex;

        if (count == 3)
            beforelineindex = 2;
        else
            beforelineindex = 1;

        View highlightbefore = parentbefore.getChildAt(beforelineindex);
        if(isBase.equalsIgnoreCase("Orange")) {
            highlightbefore.setBackgroundColor(getResources().getColor(color));
        }else{
            highlightbefore.setBackgroundColor(color);
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
        view.setBackgroundColor(getinitialColor());
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

    private void removeError(){
        errorView.setVisibility(View.GONE);
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
        drawLine();
        setDisabledTextFeild(child);
    }

    private void setDisabledTextFeild(View child){
        EditText editText1 = (EditText)((LinearLayout)child).getChildAt(0);
        EditText editText2 = (EditText)((LinearLayout)child).getChildAt(1);
        boolean isEnabled = editText1.isEnabled();
        if(isEnabled == false){
            LinearLayout parent = ((LinearLayout)((LinearLayout)child).getParent());
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
