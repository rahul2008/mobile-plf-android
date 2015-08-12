package com.catalog.ui.cdp.philips.navigationbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CustomViewInflation extends LinearLayout {

    private View view;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private RelativeLayout parentLayout;

    public CustomViewInflation(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public CustomViewInflation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomViewInflation(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        view = getView();
        addView(view);
        declareViews();
    }

    private void declareViews() {
        parentLayout = (RelativeLayout) view.findViewById(R.id.parent);

        textView1 = (TextView) view.findViewById(R.id.text1);

        textView2 = (TextView) view.findViewById(R.id.text2);

        textView3 = (TextView) view.findViewById(R.id.text3);
    }

    private View getView() {
        View view;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.inflate_check, this, false);
        return view;
    }


    public TextView getTextView1() {
        return textView1;
    }

    public TextView getTextView2() {
        return textView2;
    }

    public TextView getTextView3() {
        return textView3;
    }

    public RelativeLayout getParentLayout() {
        return parentLayout;
    }

}
