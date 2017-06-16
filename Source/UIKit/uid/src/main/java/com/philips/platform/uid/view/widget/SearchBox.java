/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.StrokeCompat;
import com.philips.platform.uid.thememanager.ThemeUtils;


/**
 * UID SearchBox.
 * <P>
 * <P>UID SearchBox is custom search view as per DLS design. Searchbox has two modes.
 * <BR>Trigger mode
 * <BR>Input mode
 * <P>
 * <p>Trigger mode has two variants.
 * <BR>Iconified
 * <BR>Expanded Decoy
 * <P>
 * <P> For usage of Search box please refer to the DLS Catalog app or the confluence page below
 * <P>
 * @see <a href="https://confluence.atlas.philips.com/display/MU/DLS-Android%3A+Integrate+Search+Box">https://confluence.atlas.philips.com/display/MU/DLS-Android%3A+Integrate+Search+Box</a>
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

    private boolean isSearchIconified;
    private boolean isSearchCollapsed = true;
    private int maxWidth;

    private ExpandListener expandListener;
    private FilterQueryChangedListener filterQueryChangedListener;
    private QuerySubmitListener querySubmitListener;
    private Filter searchFilter;

    private final static int DRAWABLE_FILL_INDEX = 0;
    private final static int DRAWABLE_STROKE_INDEX = 1;

    private ColorStateList strokeColorStateList;
    private ColorStateList fillColorStateList;

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

        initializeSearch(context, attrs, defStyleAttr);
    }

    /**
     * This API will return the Search Box decoy search layout
     *
     */
    @SuppressWarnings("unused")
    public View getDecoySearchLayout() {
        return decoySearchView;
    }

    /**
     * This API will return the Search Box decoy search icon
     *
     */
    @SuppressWarnings("unused")
    public ImageView getDecoySearchIconView() {
        return decoySearchIcon;
    }

    /**
     * This API will return the Search Box decoy hint text label
     *
     */
    @SuppressWarnings("unused")
    public Label getDecoySearchHintView() {
        return decoyHintView;
    }

    /**
     * This API will return the Search Box collapse/back icon
     *
     */
    public ImageView getCollapseView() {
        return collapseIcon;
    }

    /**
     * This API will return the Search Box input clear icon
     *
     */
    @SuppressWarnings("unused")
    public ImageView getClearIconView() {
        return clearIcon;
    }

    /**
     * This API will return the Search Box input AppCompatAutoCompleteTextView
     *
     */
    public AppCompatAutoCompleteTextView getSearchTextView() {
        return searchTextView;
    }

    /**
     * This API will return the Search Box input clear icon holder
     *
     */
    public View getSearchClearLayout() {
        return searchClearLayout;
    }

    private void initializeSearch(final Context context, @NonNull AttributeSet attrs, @NonNull int defStyleAttr) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.uid_search_box, this);
        searchExpandedLayout = findViewById(R.id.uid_search_box_layout);
        initCollapseIcon();
        initSearchIconHolder();
        initDecoySearchView(context, attrs, defStyleAttr);
        initClearIcon();
        initSearchTextView();
        updateViews();
        setSaveEnabled(true);
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
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (querySubmitListener != null) {
                        querySubmitListener.onQuerySubmit(searchTextView.getText());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void initDecoySearchView(final Context context, @NonNull AttributeSet attrs, @NonNull int defStyleAttr) {
        decoySearchView = (ViewGroup) findViewById(R.id.uid_search_decoy_view);
        decoySearchIcon = (ImageView) findViewById(R.id.uid_search_decoy_hint_icon);
        decoyHintView = (Label) findViewById(R.id.uid_search_decoy_hint_text);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UIDTextEditBox, defStyleAttr, R.style.UIDEditTextBox);
        final Resources.Theme theme = ThemeUtils.getTheme(context, attrs);
        Drawable backgroundDrawable = getLayeredBackgroundDrawable(typedArray, theme);
        if (backgroundDrawable != null) {
            decoySearchView.setBackground(backgroundDrawable);
        }

        decoySearchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchCollapsed(false);
                searchTextView.requestFocus();
            }
        });
    }

    private Drawable getLayeredBackgroundDrawable(@NonNull TypedArray typedArray, @NonNull final Resources.Theme theme) {
        Drawable fillDrawable = getFillBackgroundDrawable(typedArray, theme);
        Drawable borderDrawable = getBorderBackground(typedArray, theme);
        Drawable backgroundDrawable = fillDrawable;
        if (fillDrawable != null && borderDrawable != null) {
            backgroundDrawable = new LayerDrawable(new Drawable[]{fillDrawable, borderDrawable});
            ((LayerDrawable) backgroundDrawable).setId(DRAWABLE_FILL_INDEX, R.id.uid_texteditbox_fill_drawable);
            ((LayerDrawable) backgroundDrawable).setId(DRAWABLE_STROKE_INDEX, R.id.uid_texteditbox_stroke_drawable);
        }
        return backgroundDrawable;
    }

    private Drawable getBorderBackground(final @NonNull TypedArray typedArray,
                                         final Resources.Theme theme) {
        int borderDrawableID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextBorderBackground, -1);
        Drawable strokeDrawable = null;
        if (borderDrawableID != -1) {
            strokeDrawable = AppCompatResources.getDrawable(getContext(), borderDrawableID);
            int borderColorStateListID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextBorderBackgroundColorList, -1);
            int borderWidth = (int) typedArray.getDimension(R.styleable.UIDTextEditBox_uidInputTextBorderWidth, 0f);
            if (borderColorStateListID != -1) {
                strokeColorStateList = ThemeUtils.buildColorStateList(getContext().getResources(), theme, borderColorStateListID);
                strokeDrawable = StrokeCompat.setStroke(strokeDrawable, borderWidth, strokeColorStateList);
            }
        }
        return strokeDrawable;
    }

    private Drawable getFillBackgroundDrawable(final @NonNull TypedArray typedArray,
                                               final Resources.Theme theme) {
        int fillDrawableID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextFillBackground, -1);
        Drawable fillDrawable = null;
        if (fillDrawableID != -1) {
            fillDrawable = DrawableCompat.wrap(AppCompatResources.getDrawable(getContext(), fillDrawableID));
            int fillColorStateListID = typedArray.getResourceId(R.styleable.UIDTextEditBox_uidInputTextFillBackgroundColorList, -1);
            if (fillColorStateListID != -1) {
                fillColorStateList = ThemeUtils.buildColorStateList(getContext().getResources(), theme, fillColorStateListID);
                DrawableCompat.setTintList(fillDrawable, fillColorStateList);
            }
        }
        return fillDrawable;
    }


    @SuppressLint("SwitchIntDef")
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
     * This API will help you to set the exact string resource passed to Decoy Search View Hint.
     *
     * @param resID String to be set to the Decoy Search View Hint
     */
    public void setDecoySearchViewHint(@StringRes int resID){
        decoyHintView.setText(resID);
    }

    /**
     * This API will help you to set the exact string value passed to Decoy Search View Hint.
     *
     * @param text String to be set to the Decoy Search View Hint
     */
    public void setDecoySearchViewHint(String text){
        decoyHintView.setText(text);
    }

    /**
     * This API will help you to set the exact string resource passed to Input Search View Hint.
     *
     * @param resID String to be set to the Input Search View Hint
     */
    public void setSearchBoxHint(@StringRes int resID){
        searchTextView.setHint(resID);
    }

    /**
     * This API will help you to set the exact string value passed to Input Search View Hint.
     *
     * @param text String to be set to the Input Search View Hint
     */
    public void setSearchBoxHint(String text){
        searchTextView.setHint(text);
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
            decoySearchView.setVisibility(View.GONE);
        } else {
            decoySearchView.setVisibility(collapsedVisibility);
            searchIconHolder.setVisibility(View.GONE);
        }

        int visibility = isSearchCollapsed ? View.GONE : View.VISIBLE;
        searchExpandedLayout.setVisibility(visibility);
        searchClearLayout.setVisibility(visibility);
        collapseIcon.setVisibility(visibility);
        updateCloseButton();
        searchTextView.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            searchTextView.requestFocus();
        }
        searchTextView.requestFocus();
        searchTextView.setText("");
        requestLayout();
    }

    /**
     * This API will return if the Search Box is in collapsed form or input form.
     *
     */
    public boolean isSearchCollapsed() {
        return isSearchCollapsed;
    }

    /**
     * This API will help you to set the query text for the search box input, for example in case of config change.
     *
     * @param query String to be set for search box input, useful
     */
    public void setQuery(CharSequence query) {
        searchTextView.setText(query);
    }

    /**
     * This API will help you to get the query text from the search box input.
     *
     */
    public CharSequence getQuery() {
        return searchTextView.getText();
    }

    /**
     * This API will help you to set the max width for the searchbox
     *
     * @param maxWidth Max value in int that the searchbox should take
     */
    @SuppressWarnings("unused")
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

    /**
     * This API will help you to set the ExpandListener in case search box transforms from expanded to collapsed or vice a versa.
     *
     */
    public void setExpandListener(ExpandListener listener) {
        expandListener = listener;
    }

    /**
     * This API will help you to set the QuerySubmitListener in case search box query is submit through the search icon on IME.
     *
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