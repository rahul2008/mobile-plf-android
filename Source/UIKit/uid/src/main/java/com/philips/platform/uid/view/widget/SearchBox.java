/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.platform.uid.R;


public class SearchBox extends LinearLayout {

    public final ImageView mBackButton;
    public final ImageView mCloseButton;
    public AppCompatAutoCompleteTextView autoCompleteTextView;


    public SearchBox(Context context) {
        this(context, null);
    }

    public SearchBox(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.HORIZONTAL);


        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.uid_search_box, this);
        mBackButton = (ImageView) findViewById(android.support.v7.appcompat.R.id.search_button);
        mCloseButton = (ImageView) findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        autoCompleteTextView = (AppCompatAutoCompleteTextView) findViewById(android.support.v7.appcompat.R.id.search_src_text);


        mBackButton.setImageResource(R.drawable.uid_back_icon);
        mCloseButton.setImageResource(R.drawable.uid_texteditbox_clear_icon);
    }


}
