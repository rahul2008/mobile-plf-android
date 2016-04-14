package com.philips.cdp.digitalcare.faq.model;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by naveen@philips.com on 15-Apr-16.
 */
public class QuestionsGroupModel {


    private View mParentView = null;
    private View mChildView = null;
    private String mQuestionText = null;
    private ImageView mArrowImage = null;

    public ImageView getArrowImage() {
        return mArrowImage;
    }

    public void setArrowImage(ImageView mArrowImage) {
        this.mArrowImage = mArrowImage;
    }

    public String getQuestionText() {
        return mQuestionText;
    }

    public void setQuestionText(String mQuestionText) {
        this.mQuestionText = mQuestionText;
    }

    public View getParentView() {
        return mParentView;
    }

    public void setParentView(View mParentView) {
        this.mParentView = mParentView;
    }

    public View getChildView() {
        return mChildView;
    }

    public void setChildView(View mChildView) {
        this.mChildView = mChildView;
    }

}
