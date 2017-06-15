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

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class SearchBox extends LinearLayout {

    private ImageView backButton;
    private ImageView closeButton;
    private ImageView searchIconHolder;
    private AppCompatAutoCompleteTextView searchTextView;
    private View searchBoxView;
    private View searchClearLayout;
    private View searchExpandedLayout;
    private View searchBoxViewLayout;
    private ImageView searchBoxViewIcon;
    private Label searchBoxViewText;

    private boolean isSearchIconified;
    private boolean isSearchCollapsed = true;
    private int maxWidth;

    private ExpandListener expandListener;
    private FilterQueryChangedListener filterQueryChangedListener;
    private QuerySubmitListener querySubmitListener;
    private Filter searchFilter;

    private static final String FONT_PATH_CENTRALESANS_MEDIUM = "fonts/centralesansmedium.ttf";

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

    public View getSearchBoxViewLayout() {
        return searchBoxViewLayout;
    }

    public ImageView getSearchBoxViewIcon() {
        return searchBoxViewIcon;
    }

    public Label getSearchBoxViewText() {
        return searchBoxViewText;
    }

    public ImageView getBackButton() {
        return backButton;
    }

    public AppCompatAutoCompleteTextView getSearchTextView() {
        return searchTextView;
    }

    public View getSearchClearLayout() {
        return searchClearLayout;
    }

    private void initializeSearch(final Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.uid_search_box, this);
        searchExpandedLayout = findViewById(R.id.uid_search_box_layout);
        searchBoxViewLayout = findViewById(R.id.uid_search_box_view_layout);
        searchBoxViewIcon = (ImageView) findViewById(R.id.uid_search_box_view_icon);
        searchBoxViewText = (Label) findViewById(R.id.uid_search_box_view_text);
        initBackButton();
        initSearchIconHolder();
        initSearchBoxView();
        initClearIcon();
        initSearchTextView(context);
        updateViews();
        setSaveEnabled(true);
    }

    private void initClearIcon() {
        searchClearLayout = findViewById(R.id.uid_search_clear_layout);
        closeButton = (ImageView) findViewById(R.id.uid_search_close_btn);
        closeButton.setImageResource(R.drawable.uid_texteditbox_clear_icon);
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
        backButton = (ImageView) findViewById(R.id.uid_search_back_button);
        backButton.setImageResource(R.drawable.uid_back_icon);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchCollapsed(true);
                searchTextView.setText("");
                searchTextView.clearFocus();
            }
        });
    }

    private void initSearchIconHolder() {
        searchIconHolder = (ImageView) findViewById(R.id.uid_search_icon_holder);
        searchIconHolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchCollapsed(false);
                searchTextView.requestFocus();
            }
        });
    }

    private void initSearchBoxView() {
        searchBoxView = findViewById(R.id.uid_search_box_view);
        searchBoxView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchCollapsed(false);
                searchTextView.requestFocus();
            }
        });
    }

    private void initSearchTextView(Context context) {
        searchTextView = (AppCompatAutoCompleteTextView) findViewById(R.id.uid_search_src_text);
        searchTextView.addTextChangedListener(new SearchTextWatcher());
        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (querySubmitListener != null) {
                        querySubmitListener.onQuerySubmit(searchTextView.getText());
                    }
                    return true;
                }
                return false;
            }
        });
        searchTextView.setTypeface(TypefaceUtils.load(context.getAssets(), FONT_PATH_CENTRALESANS_MEDIUM));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = Math.min(getPreferredHeight(), height);
                break;
        }
        heightMode = MeasureSpec.EXACTLY;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                if (maxWidth > 0) {
                    width = Math.min(maxWidth, width);
                }
                break;
            case MeasureSpec.EXACTLY:
                if (maxWidth > 0) {
                    width = Math.min(maxWidth, width);
                }
                break;
        }
        widthMode = MeasureSpec.EXACTLY;

        //Match the height in case iconified, avoid screen flickering in toggling iconified
        if (isSearchCollapsed && isSearchIconified) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, heightMode));
            return;
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode),
                MeasureSpec.makeMeasureSpec(height, heightMode));
    }

    public void setSearchCollapsed(boolean shouldCollapse) {
        handleStateChange(shouldCollapse);
        setImeVisibility(!shouldCollapse);
        isSearchCollapsed = shouldCollapse;
        updateViews();
    }

    public void setSearchIconified(boolean searchIconified) {
        this.isSearchIconified = searchIconified;
        updateViews();
    }

    /**
     * <p>Changes the list of data used for search. </p>
     * <p>
     * The adapter is used only for filtering the results and calling proper filterable callbacks.
     * The list refresh responsibility still holds with Adapter.
     *
     * @param adapter the adapter holding the list data
     * @see android.widget.Filterable§
     * @see android.widget.Adapter
     */
    @SuppressWarnings("unused")
    public <T extends Adapter & Filterable & FilterQueryChangedListener> void setAdapter(T adapter) {
        if (adapter != null) {
            searchFilter = adapter.getFilter();
            Editable text = searchTextView.getText();
            filterQueryChangedListener = adapter;
            if (!TextUtils.isEmpty(text)) {
                searchFilter.filter(text);
                filterQueryChangedListener.onQueryTextChanged(text);
            }
        }
    }

    private void handleStateChange(boolean shouldCollapse) {
        if (!isSearchCollapsed && shouldCollapse) {
            callCollapseListener();
        } else if (isSearchCollapsed && !shouldCollapse) {
            callExpandListener();
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

    private void updateViews() {

        int collapsedVisibility = isSearchCollapsed ? View.VISIBLE : View.GONE;

        if (isSearchIconified) {
            searchIconHolder.setVisibility(collapsedVisibility);
            searchBoxView.setVisibility(View.GONE);
        } else {
            searchBoxView.setVisibility(collapsedVisibility);
            searchIconHolder.setVisibility(View.GONE);
        }

        int visibility = isSearchCollapsed ? View.GONE : View.VISIBLE;
        searchExpandedLayout.setVisibility(visibility);
        searchClearLayout.setVisibility(visibility);
        backButton.setVisibility(visibility);
        updateCloseButton();
        searchTextView.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            searchTextView.requestFocus();
        }
        searchTextView.requestFocus();
        searchTextView.setText("");
        requestLayout();
    }

    public boolean isSearchCollapsed() {
        return isSearchCollapsed;
    }

    public void setQuery(CharSequence query) {
        searchTextView.setText(query);
    }

    public CharSequence getQuery() {
        return searchTextView.getText();
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    private int getPreferredHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.uid_searchbox_height);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isSearchCollapsed = isSearchCollapsed();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setSearchCollapsed(ss.isSearchCollapsed);
        updateViews();
        requestLayout();
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
        closeButton.setVisibility(showClose ? VISIBLE : GONE);
    }

    static class SavedState extends BaseSavedState {
        boolean isSearchCollapsed;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.isSearchCollapsed = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(this.isSearchCollapsed);
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