/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.uikit.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.philips.cdp.uikit.R;

/**
 * Created by 310240027 on 5/5/2016.
 */
public class UiKitSpinnerButton extends FrameLayout implements View.OnClickListener {
    View view;
    ProgressBar progressBar;
    UIKitButton button;

    public UiKitSpinnerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        view = LayoutInflater.from(context).inflate(R.layout.uikit_spinner_button,this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarPB);
        progressBar.setVisibility(ProgressBar.GONE);
        button = (UIKitButton) view.findViewById(R.id.buttonPB);
        button.setOnClickListener(this);
    }

    public void setProgress(int progress){
        if(progress > 0) {
            progressBar.setProgress(progress);
        }
    }


    @Override
    public void onClick(View v) {
            if(v instanceof  UIKitButton){
                enableProgress();
            }
    }

    public void enableProgress(){
        button.setText("");
        button.setEnabled(false);
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    public void disableProgress(String text){
        button.setText(text);
        button.setEnabled(true);
        progressBar.setVisibility(ProgressBar.GONE);

    }
}
