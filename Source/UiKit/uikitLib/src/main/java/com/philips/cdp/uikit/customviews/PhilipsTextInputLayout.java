package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsTextInputLayout extends LinearLayout{
    View view ;
    private Validator validator=null;

    boolean isFocused = false;
    Set<Integer> set = new ConcurrentSkipListSet<Integer>() ;
    int mThemeBaseColor;
    final String TAG = "Set";


    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(final View view, final boolean hasFocus) {
            isFocused = hasFocus;
            EditText editText = (EditText)view;
        //    String textToBeValidated = editText.getText().toString();
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

            /*
                Error Color
             */
            retainErrorLayoutFocus();


        }
    };

    private void retainErrorLayoutFocus(){
        try {
            Log.i(TAG, "In retainErrorLayoutFocus and set = " + set.toString());
            for (Integer layout:set
                 ) {
                LinearLayout parent = (LinearLayout)getChildAt(layout);

             //   if (array.contains(layout)) {
                    //In case true meaning error is there
                   // int themecolor = getThemeColor();
                    if (mThemeBaseColor == getResources().getColor(color.uikit_philips_bright_orange)) {
                        highLightErrorView(layout, R.color.uikit_enricher_red, "Orange");
                        highLightTextFeilds(parent, R.color.uikit_enricher_red);
                    } else {

                        highLightErrorView(layout, R.color.uikit_philips_bright_orange, "Orange");
                        highLightTextFeilds(parent, R.color.uikit_philips_bright_orange);
                    }
               // }
            }

        }catch (Exception e){
            Log.i("Set", set.toString());
            e.printStackTrace();
        }
    }
    public PhilipsTextInputLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        mThemeBaseColor = getThemeColor();
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


    private View createNewErrorView(){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View errorView = inflater.inflate(R.layout.uikit_input_text_inline_error, null, false);
        FrameLayout imageview = (FrameLayout)errorView.findViewById(R.id.error_image);

        imageview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {

                Log.i(TAG, "In On Click Listener the Set = " + set.toString());

                RelativeLayout parent = (RelativeLayout) view.getParent();

                int index = indexOfChild(parent);
                Log.i(TAG,"Index of RelativeLayout = " +index);

                removeView(parent);

                boolean isremoved = set.remove(index - 1);
                Log.i(TAG,"is removded = " + isremoved);

                Log.i(TAG, "Reset color called for" + index + "minus one");
                resetcolor(index - 1);


                for (Object i :set
                        ) {
                    Integer indexOfParenthavingError = (Integer)i;
                    if(index<=indexOfParenthavingError){
                        set.remove(indexOfParenthavingError);
                        set.add(indexOfParenthavingError-1);
                    }
                }
                Log.i(TAG, "After In On Click Listener the Set = " + set.toString());
                retainErrorLayoutFocus();
            }
        });
        return errorView;
    }

    public void removeError(View editText){
        //Toast.makeText(getContext(),"Remove Error", Toast.LENGTH_SHORT).show();
        LinearLayout parent = (LinearLayout)editText.getParent();
        int indexofparent = indexOfChild(parent);

        View errorview = getChildAt(indexofparent + 1);

        if(errorview instanceof RelativeLayout){
            int index = indexOfChild(errorview);
            removeView(errorview);
            boolean isremoved = set.remove(index - 1);
            Log.i(TAG,"is removded = " + isremoved);

            Log.i(TAG, "Reset color called for" + index + "minus one");
            resetcolor(index - 1);

            for (Object i :set
                    ) {
                Integer indexOfParenthavingError = (Integer)i;
                if(index<=indexOfParenthavingError){
                    set.remove(indexOfParenthavingError);
                    set.add(indexOfParenthavingError-1);
                }
            }
            Log.i(TAG, "After In On Click Listener the Set = " + set.toString());
            retainErrorLayoutFocus();
        }
    }

    private void resetcolor(int indexofParent){
         LinearLayout parent = (LinearLayout)getChildAt(indexofParent);
        if(parent.hasFocus()){
            highLightErrorView(indexofParent, getFocusedColor(),"base");
            highLightTextFeilds(parent,R.color.uikit_philips_very_dark_blue);
        }else{
            highLightErrorView(indexofParent, color.uikit_enricher4,"Orange");
            highLightTextFeilds(parent,R.color.uikit_enricher4);
        }
         View view = getFocusedChild();
        try{
            //ViewParent parentOfView = view.getParent();
            LinearLayout layout = null;
            int index = -1;
            if(view instanceof LinearLayout){
                 layout = (LinearLayout)view;
                index = indexOfChild(layout);
                highLightErrorView(index, getFocusedColor(),"base");
                highLightTextFeilds(layout,R.color.uikit_philips_very_dark_blue);
            }



        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void showError(EditText edittext) {
        Log.i(TAG, "In Show Error the Set = " + set.toString());

        LinearLayout parent = (LinearLayout)edittext.getParent();
        int indexofParent = indexOfChild(parent);
        Log.i(TAG,"In show error index of LinearLayout where error has to be added = " + indexofParent);

        if (!(set.contains(indexofParent))) {
            View errorView = createNewErrorView();

            addView(errorView, indexofParent +1);
            set.add(indexofParent);
            int errorIndex = indexOfChild(errorView);
            Log.i(TAG,"Error Layout containing error view = " + indexofParent);
            for (Object i: set
                    ) {
                Integer indexofErrorParent = (Integer)i;
                if(errorIndex <=indexofErrorParent){
                    Log.i(TAG, "In Show Error, error Index " + errorIndex + "<= error layout index in set " + indexofErrorParent);
                    boolean isremoved = set.remove(indexofErrorParent);
                    Log.i(TAG,"is removed  = " + isremoved + "and set = " + set.toString());
                    isremoved = set.add(indexofErrorParent+1);
                    Log.i(TAG,"is added  = " + isremoved + "and set = " + set.toString());
                }
            }
            Log.i(TAG, "After In Show Error the Set = " + set.toString());
        }
    }

    private void highLightErrorView(int indexofParent, int color, String isBase){

        /*
            Line HighLighting
         */
        try {
            View belowLine;
            View aboveLine = getChildAt(indexofParent - 1);
            Log.i(TAG, "above Line index = " + indexOfChild(aboveLine));

            belowLine = getChildAt(indexofParent + 1);

            if(belowLine instanceof RelativeLayout){
                belowLine = getChildAt(indexofParent + 2);
            }

            Log.i(TAG, "Below Line index = " + indexOfChild(belowLine));

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

            if(set.contains(indexofParent)) {
                if (mThemeBaseColor == getResources().getColor(R.color.uikit_philips_bright_orange)) {
                    aboveLine.setBackgroundColor(getResources().getColor(R.color.uikit_enricher_red));
                    belowLine.setBackgroundColor(getResources().getColor(R.color.uikit_enricher_red));
                }else{
                    aboveLine.setBackgroundColor(getResources().getColor(R.color.uikit_philips_bright_orange));
                    belowLine.setBackgroundColor(getResources().getColor(R.color.uikit_philips_bright_orange));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void highLightTextFeilds(LinearLayout parent, int color){

        TextView editText1 = (TextView) parent.getChildAt(0);
        EditText editText2 = (EditText) parent.getChildAt(1);
            if(editText2.getText().toString().trim().length() == 0) {
                editText1.setTextColor(getResources().getColor(color));
                editText2.setTextColor(getResources().getColor(color));
            }else{
                editText1.setTextColor(getResources().getColor(R.color.uikit_philips_dark_blue));
                editText2.setTextColor(getResources().getColor(R.color.uikit_philips_dark_blue));

                if(set.contains(indexOfChild(parent))) {
                    if (mThemeBaseColor == getResources().getColor(R.color.uikit_philips_bright_orange)) {
                        editText1.setTextColor(getResources().getColor(R.color.uikit_enricher_red));
                        editText2.setTextColor(getResources().getColor(R.color.uikit_enricher_red));
                    }else{
                        editText1.setTextColor(getResources().getColor(R.color.uikit_philips_bright_orange));
                        editText2.setTextColor(getResources().getColor(R.color.uikit_philips_bright_orange));
                    }
                }
            }



    }


    private void drawLine(){
        view = new View(getContext());
        int height = (int) getContext().getResources().getDimension(R.dimen.uikit_view_height);

        LayoutParams layoutparams = new LayoutParams(LayoutParams.MATCH_PARENT,height);
        view.setLayoutParams(layoutparams);
        view.setBackgroundColor(getFocusedColor());
        addView(view);
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


    @Override
    public void addView(final View child, final ViewGroup.LayoutParams params) {
        super.addView(child, params);

        EditText editText;
        LinearLayout layout = (LinearLayout)child;

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

        drawLine();
        setDisabledTextFeild(child);


    }


    private void setDisabledTextFeild(View child) {
        TextView editText1 = (TextView) ((LinearLayout) child).getChildAt(0);
        EditText editText2 = (EditText) ((LinearLayout) child).getChildAt(1);

        boolean isEnabled = editText2.isEnabled();
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
