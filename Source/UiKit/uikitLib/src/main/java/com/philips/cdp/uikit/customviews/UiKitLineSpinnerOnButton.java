/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;

/**
 * Created by 310240027 on 5/5/2016.
 */
public class UiKitLineSpinnerOnButton extends FrameLayout implements View.OnClickListener {

    View view;
    ProgressBar progressBar;
    Button button;
    private OnClickListener listener;

    public UiKitLineSpinnerOnButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.uikit_line_spinner_button, this);
        progressBar = (ProgressBar) view.findViewById(R.id.lineProgressBarPB);
        progressBar.setVisibility(ProgressBar.GONE);
        button = (Button) view.findViewById(R.id.buttonLinePB);
        button.setOnClickListener(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (listener != null)
                listener.onClick(this);
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
            if (listener != null)
                listener.onClick(this);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            enableProgress();
        }
    }

    public void setButtonText(String text){
        button.setText(text);
    }

    public void setDrawable(Drawable drawable,String position){
        if(position.equalsIgnoreCase("left")){
            button.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
        }
        else if(position.equalsIgnoreCase("right")){
            button.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        }else if(position.equalsIgnoreCase("top")){
            button.setCompoundDrawablesWithIntrinsicBounds(null,drawable,null,null);
        }
        else if(position.equalsIgnoreCase("bottom")){
            button.setCompoundDrawablesWithIntrinsicBounds(null,null,null,drawable);
        }
        else{
            button.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        }

    }

    public void enableProgress() {
        button.setText("");
        button.setEnabled(false);
        button.setClickable(false);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    public void disableProgress() {
        button.setEnabled(true);
        button.setClickable(true);
        progressBar.setVisibility(ProgressBar.GONE);

    }

}
