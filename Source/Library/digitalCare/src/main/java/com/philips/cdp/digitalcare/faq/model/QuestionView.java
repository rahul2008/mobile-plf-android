package com.philips.cdp.digitalcare.faq.model;

import android.view.View;

/**
 * Created by naveen@philips.com on 15-Apr-16.
 */
public class QuestionView {


    private View mParentView = null;
    private View mChildView = null;

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
