/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDInputTextUtils;


/**
 * UID SearchBox. Use app:uidSearchInContentArea="true" in xml if the searchbox is to be used in content area.
 * <p>
 * <P>UID SearchBox is custom search view as per DLS design. Searchbox has two modes.<br
 * <BR>Trigger mode
 * <BR>Input mode
 * <p>
 * <p>Trigger mode has two variants.
 * <BR>Iconified
 * <BR>Expanded Decoy
 * <p>
 * <p>
 * <P> For usage of Search box please refer to the DLS Catalog app or the confluence page below
 * <p>
 *
 * @see <a href="https://confluence.atlas.philips.com/display/MU/How+to+integrate+DLS+Search+Box">https://confluence.atlas.philips.com/display/MU/How+to+integrate+DLS+Search+Box</a>
 */

public class SearchBox extends LinearLayout {

    private ImageView collapseIcon;
    private ImageView clearIcon;
    private ImageView searchIconHolder;
    private AppCompatAutoCompleteTextView searchTextView;
    private View searchClearLayout;
    private View searchExpandedLayout;

    private ViewGroup decoySearchView;
    private ImageView decoySearchIcon;
    private Label decoyHintView;

    private boolean isSearchInContentArea;
    private boolean isSearchIconified;
    private boolean isSearchCollapsed = true;
    private int maxWidth;

    private ExpandListener expandListener;
    private FilterQueryChangedListener filterQueryChangedListener;
    private QuerySubmitListener querySubmitListener;
    private Filter searchFilter;

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

    ViewTreeObserver.OnGlobalFocusChangeListener globalFocusChangeListener = new ViewTreeObserver.OnGlobalFocusChangeListener() {
        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            //Should we really check for the empty text? May be need to change from app feedback
            if (oldFocus == searchTextView && TextUtils.isEmpty(searchTextView.getText())) {
                searchExpandedLayout.setVisibility(View.GONE);
                decoySearchView.setVisibility(View.VISIBLE);
            }
        }
    };

    public SearchBox(Context context) {
        this(context, null);
    }

    public SearchBox(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.HORIZONTAL);

        checkIfSearchInContentArea(context, attrs);
        initializeSearch(context, attrs, defStyleAttr);
    }

    private void checkIfSearchInContentArea(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, new int[]{R.attr.uidSearchInContentArea});
            if (array.hasValue(0)) {
                isSearchInContentArea = array.getBoolean(0, false);
            }
            array.recycle();
        }
    }

    private void initializeSearch(final Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.uid_search_box, this);
        searchExpandedLayout = findViewById(R.id.uid_search_box_layout);

        initViews(context, attrs, defStyleAttr);
        initHintTexts(context, attrs);

    }

    private void initViews(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        initSearchTextView();
        initDecoySearchView(context, attrs, defStyleAttr);
        initSearchInContentArea(context);
        initCollapseIcon();
        initSearchIconHolder();
        initClearIcon();
        updateViews();
        setSaveEnabled(true);
    }

    private void initDecoySearchView(final Context context, AttributeSet attrs, int defStyleAttr) {
        decoySearchView = (ViewGroup) findViewById(R.id.uid_search_decoy_view);
        decoySearchIcon = (ImageView) findViewById(R.id.uid_search_decoy_hint_icon);
        decoyHintView = (Label) findViewById(R.id.uid_search_decoy_hint_text);

        if (!isSearchInContentArea) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDTextEditBox, defStyleAttr, R.style.UIDEditTextBox);
            final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
            Drawable backgroundDrawable = UIDInputTextUtils.getLayeredBackgroundDrawable(context, typedArray);

            if (backgroundDrawable != null) {
                decoySearchView.setBackground(backgroundDrawable);
            }
            typedArray.recycle();
        }

        decoySearchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchCollapsed(false);
                searchTextView.requestFocus();
            }
        });
    }

    private void initSearchInContentArea(Context context) {
        if (isSearchInContentArea) {
            Drawable background = ContextCompat.getDrawable(context, R.drawable.uid_searchbox_contentarea_background);
            decoySearchView.setBackground(background);
            searchExpandedLayout.setBackground(background);
            int leftRightMargin = context.getResources().getDimensionPixelSize(R.dimen.uid_searchbox_layout_margin);
            MarginLayoutParamsCompat.setMarginEnd((MarginLayoutParams) searchExpandedLayout.getLayoutParams(), leftRightMargin);
            MarginLayoutParamsCompat.setMarginStart((MarginLayoutParams) searchExpandedLayout.getLayoutParams(), leftRightMargin);
        }
    }


    private void initClearIcon() {
        searchClearLayout = findViewById(R.id.uid_search_clear_layout);
        clearIcon = (ImageView) findViewById(R.id.uid_search_close_btn);
        clearIcon.setImageResource(R.drawable.uid_texteditbox_clear_icon);
        searchClearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTextView.setText("");
                searchTextView.requestFocus();
                setImeVisibility(true);
            }
        });
    }

    private void initCollapseIcon() {
        collapseIcon = (ImageView) findViewById(R.id.uid_search_back_button);
        collapseIcon.setImageResource(R.drawable.uid_back_icon);
        collapseIcon.setOnClickListener(new OnClickListener() {
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
        setDecoySearchIcon(ContextCompat.getDrawable(getContext(), R.drawable.uid_search_icon));
        searchIconHolder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchCollapsed(false);
                searchTextView.requestFocus();
            }
        });
    }

    private void initSearchTextView() {
        searchTextView = (AppCompatAutoCompleteTextView) findViewById(R.id.uid_search_src_text);
        searchTextView.addTextChangedListener(new SearchTextWatcher());
        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (querySubmitListener != null) {
                    querySubmitListener.onQuerySubmit(searchTextView.getText());
                }
                return true;
            }
        });
    }

    private void initHintTexts(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{R.attr.uidSearchInputHintText, R.attr.uidSearchDecoyHintText});

        CharSequence inputText = typedArray.getText(0);
        if (inputText != null) {
            searchTextView.setHint(inputText);
        }

        CharSequence decoyText = typedArray.getText(1);
        if (decoyText != null) {
            decoyHintView.setText(decoyText);
        }
        typedArray.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isSearchInContentArea) {
            getViewTreeObserver().addOnGlobalFocusChangeListener(globalFocusChangeListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isSearchInContentArea) {
            getViewTreeObserver().removeOnGlobalFocusChangeListener(globalFocusChangeListener);
        }
    }

    @SuppressLint("SwitchIntDef")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                height = height == 0 ? getPreferredHeight() : Math.min(getPreferredHeight(), height);
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
        if (isSearchCollapsed && isSearchIconified && !isSearchInContentArea) {
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, heightMode));
            return;
        }

        super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode),
                MeasureSpec.makeMeasureSpec(height, heightMode));
    }

    /**
     * This API will help you to set if SearchBox should be collapsed or expanded for input.
     *
     * @param shouldCollapse Boolean to set if the SearchBox should be collapsed or input mode
     */
    public void setSearchCollapsed(boolean shouldCollapse) {
        handleStateChange(shouldCollapse);
        setImeVisibility(!shouldCollapse);
        isSearchCollapsed = shouldCollapse;
        updateViews();
    }

    /**
     * This API will help you to set the Search Box collapsed form. Either as icon or expanded decoy
     *
     * @param searchIconified Boolean to set if the SearchBox collapsed form should appear as icon or expanded decoy
     */
    public void setSearchIconified(boolean searchIconified) {
        this.isSearchIconified = searchIconified;
        updateViews();
    }

    /**
     * This API will help you to set the exact string resource passed to Decoy Search View Hint. Alternatively you can use app:uidSearchDecoyHintText attribute in xml.
     *
     * @param resID String to be set to the Decoy Search View Hint
     */
    public void setDecoySearchViewHint(@StringRes int resID) {
        decoyHintView.setText(resID);
    }

    /**
     * This API will help you to set the exact string value passed to Decoy Search View Hint. Alternatively you can use app:uidSearchDecoyHintText attribute in xml.
     *
     * @param text String to be set to the Decoy Search View Hint
     */
    @SuppressWarnings("unused")
    public void setDecoySearchViewHint(String text) {
        decoyHintView.setText(text);
    }

    /**
     * This API will help you to set the exact string resource passed to Input Search View Hint. Alternatively you can use app:uidSearchInputHintText attribute in xml.
     *
     * @param resID String to be set to the Input Search View Hint
     */
    public void setSearchBoxHint(@StringRes int resID) {
        searchTextView.setHint(resID);
    }

    /**
     * This API will help you to set the exact string value passed to Input Search View Hint. Alternatively you can use app:uidSearchInputHintText attribute in xml
     *
     * @param text String to be set to the Input Search View Hint
     */
    @SuppressWarnings("unused")
    public void setSearchBoxHint(String text) {
        searchTextView.setHint(text);
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
            decoySearchView.setVisibility(View.GONE);
        } else {
            decoySearchView.setVisibility(collapsedVisibility);
            hideSearchIcon();
        }

        int visibility = isSearchCollapsed ? View.GONE : View.VISIBLE;
        searchExpandedLayout.setVisibility(visibility);
        searchClearLayout.setVisibility(visibility);
        setCollapseIconVisibility(visibility);
        updateCloseButton();
        searchTextView.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            searchTextView.requestFocus();
        }
        searchTextView.requestFocus();
        searchTextView.setText("");
        requestLayout();
    }

    private void setCollapseIconVisibility(int visibility) {
        int newVisibility = isSearchInContentArea ? View.GONE : visibility;
        collapseIcon.setVisibility(newVisibility);
    }

    /**
     * This API returns if the Search Box is in collapsed form or input form.
     */
    public boolean isSearchCollapsed() {
        return isSearchCollapsed;
    }

    /**
     * This API helps to set the query text for the search box input, for example in case of config change.
     *
     * @param query String to be set for search box input, useful
     */
    public void setQuery(CharSequence query) {
        searchTextView.setText(query);
    }

    /**
     * This API helps to get the query text from the search box input.
     */
    public CharSequence getQuery() {
        return searchTextView.getText();
    }

    /**
     * This API returns the Search Box decoy search layout
     */
    @SuppressWarnings("unused")
    public View getDecoySearchLayout() {
        return decoySearchView;
    }

    /**
     * This API returns the Search Box decoy search icon
     */
    @SuppressWarnings("unused")
    public ImageView getDecoySearchIconView() {
        return decoySearchIcon;
    }

    /**
     * This API returns the Search Box decoy hint text label
     */
    @SuppressWarnings("unused")
    public Label getDecoySearchHintView() {
        return decoyHintView;
    }

    /**
     * This API returns the Search Box collapse/back icon
     */
    public ImageView getCollapseView() {
        return collapseIcon;
    }

    /**
     * This API returns the Search Box input clear icon
     */
    @SuppressWarnings("unused")
    public ImageView getClearIconView() {
        return clearIcon;
    }

    /**
     * This API returns the Search Box input AppCompatAutoCompleteTextView
     */
    public AppCompatAutoCompleteTextView getSearchTextView() {
        return searchTextView;
    }

    /**
     * This API returns the Search Box input clear icon holder
     */
    public View getSearchClearLayout() {
        return searchClearLayout;
    }

    /**
     * This API helps you to set the max width for the searchbox
     *
     * @param maxWidth Max value in int that the searchbox should take
     */
    @SuppressWarnings("unused")
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        updateViews();
    }

    /**
     * Hides the search icon in decoy view
     */
    public void hideSearchIcon() {
        searchIconHolder.setVisibility(View.GONE);
    }

    /**
     * Sets the search icon in decoy search view.
     *
     * @param drawable for search.
     */
    @SuppressWarnings("unused")
    public void setDecoySearchIcon(Drawable drawable) {
        searchIconHolder.setImageDrawable(drawable);
    }

    private int getPreferredHeight() {
        int dimenID = isSearchInContentArea ? R.dimen.uid_searchbox_decoy_height : R.dimen.uid_searchbox_height;
        return getContext().getResources().getDimensionPixelSize(dimenID);
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

    /**
     * This API helps you to set the ExpandListener in case search box transforms from expanded to collapsed or vice a versa.
     */
    public void setExpandListener(ExpandListener listener) {
        expandListener = listener;
    }

    /**
     * This API helps you to set the QuerySubmitListener in case search box query is submit through the search icon on IME.
     */
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
        clearIcon.setVisibility(showClose ? VISIBLE : GONE);
    }

    private static class SavedState extends BaseSavedState {
        boolean isSearchCollapsed;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.isSearchCollapsed = (Boolean) in.readValue(getClass().getClassLoader());
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