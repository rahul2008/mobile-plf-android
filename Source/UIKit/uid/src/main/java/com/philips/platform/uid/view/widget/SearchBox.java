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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.uid.R;


public class SearchBox extends LinearLayout {

    public ImageView mBackButton;
    public ImageView mCloseButton;
    public ImageView mSearchIconHolder;
    public AppCompatAutoCompleteTextView autoCompleteTextView;

    private boolean isIconified;
    private View searchClearLayout;


    public SearchBox(Context context) {
        this(context, null);
    }

    public SearchBox(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.HORIZONTAL);

        initializeSearch(context);
    }

    private void initializeSearch(final Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.uid_search_box, this);
        initBackButton();
        mCloseButton = (ImageView) findViewById(R.id.search_close_btn);
        initCloseIconHolder();
        searchClearLayout = findViewById(R.id.uid_search_clear_layout);
        autoCompleteTextView = (AppCompatAutoCompleteTextView) findViewById(R.id.search_src_text);

        mBackButton.setImageResource(R.drawable.uid_back_icon);
        mCloseButton.setImageResource(R.drawable.uid_texteditbox_clear_icon);
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(context, autoCompleteTextView.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void initBackButton() {
        mBackButton = (ImageView) findViewById(R.id.search_button);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setIconified(true);
            }
        });
    }

    private void initCloseIconHolder() {
        mSearchIconHolder = (ImageView) findViewById(R.id.uid_search_icon_holder);
        mSearchIconHolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setIconified(false);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isIconified) {
             super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    public void setIconified(boolean iconified) {
        isIconified = iconified;
        updateViews();
    }

    private void updateViews() {
        int iconHolderVisisblity = isIconified ? View.VISIBLE: View.GONE;
        mSearchIconHolder.setVisibility(iconHolderVisisblity);

        int visibility = isIconified ? View.GONE: View.VISIBLE;
        searchClearLayout.setVisibility(visibility);
        mBackButton.setVisibility(visibility);
        mCloseButton.setVisibility(visibility);
        autoCompleteTextView.setVisibility(visibility);
        requestLayout();
    }
}