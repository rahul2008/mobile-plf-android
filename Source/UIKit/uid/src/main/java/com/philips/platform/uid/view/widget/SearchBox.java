/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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


    private boolean isSearchIconified;
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
        mCloseButton = (ImageView) findViewById(R.id.uid_search_close_btn);
        initSearchIconHolder();
        searchClearLayout = findViewById(R.id.uid_search_clear_layout);
        autoCompleteTextView = (AppCompatAutoCompleteTextView) findViewById(R.id.uid_search_src_text);

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
        updateViews();
        setSaveEnabled(true);
    }

    private void initBackButton() {
        mBackButton = (ImageView) findViewById(R.id.uid_search_button);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchIconified(true);
                autoCompleteTextView.setText(null);
                setImeVisibility(false);
                autoCompleteTextView.clearFocus();
            }
        });
    }

    private void initSearchIconHolder() {
        mSearchIconHolder = (ImageView) findViewById(R.id.uid_search_icon_holder);
        mSearchIconHolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchIconified(false);
                autoCompleteTextView.requestFocus();
                setImeVisibility(true);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(isSearchIconified) {
             super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    public void setSearchIconified(boolean searchIconified) {
        isSearchIconified = searchIconified;
        updateViews();
    }

    private void updateViews() {
        int iconHolderVisisblity = isSearchIconified ? View.VISIBLE: View.GONE;
        mSearchIconHolder.setVisibility(iconHolderVisisblity);

        int visibility = isSearchIconified ? View.GONE: View.VISIBLE;
        searchClearLayout.setVisibility(visibility);
        mBackButton.setVisibility(visibility);
        mCloseButton.setVisibility(visibility);
        autoCompleteTextView.setVisibility(visibility);
        requestLayout();
    }

    public boolean isSearchIconified() {
        return isSearchIconified;
    }

    static class SavedState extends BaseSavedState {
        boolean isSearchIconified;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.isSearchIconified = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(this.isSearchIconified);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isSearchIconified = isSearchIconified();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        setSearchIconified(ss.isSearchIconified);
        updateViews();
        requestLayout();
        super.onRestoreInstanceState(ss.getSuperState());
    }

    private Runnable mShowImeRunnable = new Runnable() {
        public void run() {
            InputMethodManager imm = getContext().getSystemService(InputMethodManager.class);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    };

    private void setImeVisibility(final boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            InputMethodManager imm = getContext().getSystemService(InputMethodManager.class);

            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

}