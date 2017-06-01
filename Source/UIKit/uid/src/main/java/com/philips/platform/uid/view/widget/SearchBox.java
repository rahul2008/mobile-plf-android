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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.platform.uid.R;


public class SearchBox extends LinearLayout {

    public ImageView mBackButton;
    public ImageView mCloseButton;
    public ImageView mSearchIconHolder;
    public AppCompatAutoCompleteTextView searchTextView;


    private boolean isSearchIconified = true;
    private View searchClearLayout;
    private ExpandListener expandListener;
    private FilterQueryChangedListener filterQueryChangedListener;
    private Filter searchFilter;
    private QuerySubmitListener querySubmitListener;

    public interface ExpandListener {
        void onSearchExpanded();

        void onSearchCollapsed();
    }

    public interface FilterQueryChangedListener {
        void onQueryTextChanged(CharSequence newQuery);
    }

    public interface QuerySubmitListener {
        void onQuerySubmit(CharSequence query);
    }

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
        initClearIcon();
        searchTextView = (AppCompatAutoCompleteTextView) findViewById(R.id.uid_search_src_text);
        searchTextView.addTextChangedListener(new SearchTextWatcher());

        mBackButton.setImageResource(R.drawable.uid_back_icon);
        mCloseButton.setImageResource(R.drawable.uid_texteditbox_clear_icon);
        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(querySubmitListener!=null){
                        querySubmitListener.onQuerySubmit(searchTextView.getText());
                    }
                    return true;
                }
                return false;
            }
        });
        updateViews();
        setSaveEnabled(true);
    }

    private void initClearIcon() {
        searchClearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTextView.setText("");
                searchTextView.requestFocus();
                setImeVisibility(true);
            }
        });
    }

    private void initBackButton() {
        mBackButton = (ImageView) findViewById(R.id.uid_search_back_button);
        mBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callCollapseListener();
                setSearchIconified(true);
                searchTextView.setText("");
                setImeVisibility(false);
                searchTextView.clearFocus();
            }
        });
    }

    private void initSearchIconHolder() {
        mSearchIconHolder = (ImageView) findViewById(R.id.uid_search_icon_holder);
        mSearchIconHolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callExpandListener();
                setSearchIconified(false);
                searchTextView.requestFocus();
                setImeVisibility(true);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isSearchIconified) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    public void setSearchIconified(boolean searchIconified) {
        handleSearchExpansion(searchIconified);
        isSearchIconified = searchIconified;
        updateViews();
    }

    /**
     * <p>Changes the list of data used for search. </p>
     * <p>
     * The adapter is used only for filtering the results and calling proper filterable callbacks.
     * The list refresh responsibility still holds with Adapter.
     *
     * @param adapter the adapter holding the list data
     * @see android.widget.Filterable
     * @see android.widget.Adapter
     */
    @SuppressWarnings("unused")
    public <T extends Adapter & Filterable & FilterQueryChangedListener> void setAdapter(T adapter) {
        if (adapter != null) {
            searchFilter = adapter.getFilter();
            Editable text = searchTextView.getText();
            filterQueryChangedListener = adapter;
            if(!TextUtils.isEmpty(text)) {
                searchFilter.filter(text);
                filterQueryChangedListener.onQueryTextChanged(text);
            }
        }
    }

    private void handleSearchExpansion(boolean searchIconified) {
        if (shouldExpand(searchIconified)) {
            callExpandListener();
        } else if (shouldCollapse(searchIconified)) {
            callCollapseListener();
        }
    }

    private void callCollapseListener() {
        if (expandListener != null) {
            expandListener.onSearchCollapsed();
        }
    }

    private void callExpandListener() {
        if (expandListener != null) {
            expandListener.onSearchExpanded();
        }
    }

    private boolean shouldCollapse(boolean searchIconified) {
        return !isSearchIconified && searchIconified;
    }

    private boolean shouldExpand(boolean searchIconified) {
        return isSearchIconified && !searchIconified;
    }

    private void updateViews() {
        int iconHolderVisisblity = isSearchIconified ? View.VISIBLE : View.GONE;
        mSearchIconHolder.setVisibility(iconHolderVisisblity);

        int visibility = isSearchIconified ? View.GONE : View.VISIBLE;
        searchClearLayout.setVisibility(visibility);
        mBackButton.setVisibility(visibility);
        updateCloseButton();
        searchTextView.setVisibility(visibility);
        if(visibility == View.VISIBLE) {
            searchTextView.requestFocus();
        }
        searchTextView.requestFocus();
        requestLayout();
    }

    public boolean isSearchIconified() {
        return isSearchIconified;
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
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        setSearchIconified(ss.isSearchIconified);
        updateViews();
        requestLayout();
        super.onRestoreInstanceState(ss.getSuperState());
    }

    public void setExpandListener(ExpandListener listener) {
        expandListener = listener;
    }

    public void setQuerySubmitListener(QuerySubmitListener listener) {
        querySubmitListener = listener;
    }

    private Runnable mShowImeRunnable = new Runnable() {
        public void run() {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    };

    private void setImeVisibility(final boolean visible) {
        if (visible) {
            post(mShowImeRunnable);
        } else {
            removeCallbacks(mShowImeRunnable);
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    private void performFiltering(CharSequence s) {
        if (searchFilter != null) {
            searchFilter.filter(s);
        }
        if (filterQueryChangedListener != null) {
            filterQueryChangedListener.onQueryTextChanged(s);
        }
    }

    private class SearchTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            SearchBox.this.performFiltering(s);
            SearchBox.this.updateCloseButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void updateCloseButton() {
        final boolean showClose = !TextUtils.isEmpty(searchTextView.getText());
        mCloseButton.setVisibility(showClose ? VISIBLE : GONE);
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
}