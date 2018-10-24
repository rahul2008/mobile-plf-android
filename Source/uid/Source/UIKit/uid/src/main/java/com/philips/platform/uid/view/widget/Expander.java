/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.UIDHelper;


/**
 * An UIDExpander displays the DLS expander to user. It comes with a pre-bundled template of displaying
 * two icons to the left and right and a title at the middle of the expander.
 * This whole section can be customized according to user needs. Upon expansion, a custom view can be show to the user.
 * The expand/collapse operation is taken care by the Expander through an API and also user click.
 * In InterfaceBuilder it is possible to create a UIView and give it the class UIDExpander, the
 * styling will be done immediately.
 *
 * @Since: 1805.0.0
 */
public class Expander extends LinearLayout implements View.OnClickListener {


    private UIDExpanderListener uidExpanderListener;
    private RelativeLayout ExpanderViewTitle;
    private Label ExpanderViewTitleDefaultLabel;

    private Label ExpanderViewTitleDefaultIcon;
    private RelativeLayout ExpanderViewContent;
    private Label chevronLabel;
    private Context mContext;
    private View titleBottomSeparator;
    private View contentBottomSeparator;

    private boolean separatorVisibility = true;


    /**
     * Instantiates a new Expander.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public Expander(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initializeViews(context, attrs);
    }


    /**
     * Instantiates a new Expander.
     *
     * @param context the context
     */
    public Expander(@NonNull final Context context) {
        this(context, null);

    }


    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(UIDHelper.getPopupThemedContext(context));
        View expanderLayout = (View) inflater.inflate(R.layout.uid_expander, this);
        ExpanderViewTitle = (RelativeLayout) expanderLayout.findViewById(R.id.uid_expander_view_title);
        ExpanderViewTitle.setOnClickListener(this);
        ExpanderViewContent = (RelativeLayout) expanderLayout.findViewById(R.id.uid_expander_view_content);

        titleBottomSeparator = (View) expanderLayout.findViewById(R.id.uid_expander_title_bottom_divider);
        contentBottomSeparator = (View) expanderLayout.findViewById(R.id.uid_expander_content_bottom_divider);

        setLayout(R.layout.uid_expander_title_layout_default, ExpanderViewTitle); // set default expander panel title
        ExpanderViewTitleDefaultLabel = (Label) expanderLayout.findViewById(R.id.uid_expander_title_text);
        ExpanderViewTitleDefaultIcon = (Label) expanderLayout.findViewById(R.id.uid_expander_title_icon);
        chevronLabel = (Label) expanderLayout.findViewById(R.id.uid_expander_title_chevron);

    }


    /**
     * Sets expander custom title panel view.
     * Setting this will remove all default Expander Header Panel
     * components and their functionalities .
     *
     * @param resourceLayoutId the resource xml layout id
     * @Since: 1805.0.0
     */
    public void setExpanderCustomPanelView(int resourceLayoutId) {
        uidExpanderListener = null; // callback delegate is removed when custom panel is set
        setLayout(resourceLayoutId, ExpanderViewTitle);
    }


    /**
     * Sets expander custom title panel view.
     * Setting this will remove all default Expander Header Panel
     * components and their functionalities .
     *
     * @param resourceView the resource view
     * @Since: 1805.0.0
     */
    public void setExpanderCustomPanelView(View resourceView) {
        uidExpanderListener = null; // callback delegate is removed when custom panel is set
        setLayout(resourceView, ExpanderViewTitle);
    }


    /**
     * Sets expander content view.
     * This is the view which will display when the Expander is expanded.
     * This value should be set in order to do the expand operation
     *
     * @param resourceLayoutId the resource layout id
     * @Since: 1805.0.0
     */
// content layout
    public void setExpanderContentView(int resourceLayoutId) {
        setLayout(resourceLayoutId, ExpanderViewContent);
    }


    /**
     * Sets expander content view.
     * This is the view which will display when the Expander is expanded.
     * This value should be set in order to do the expand operation
     *
     * @param resourceView the resource view
     * @Since: 1805.0.0
     */
    public void setExpanderContentView(View resourceView) {
        setLayout(resourceView, ExpanderViewContent);
    }


    /**
     * Sets expander title.
     *
     * @param title the title
     *              Default: blank
     * @Since: 1805.0.0
     */
    public void setExpanderTitle(String title) {
        if (isDefaultPanel() && null != ExpanderViewTitleDefaultLabel) {
            ExpanderViewTitleDefaultLabel.setText(title);
        }
    }


    /**
     * Sets expander panel left icon.
     *
     * @param fontIcon the font icon
     *                 Default: null
     * @Since: 1805.0.0
     */
    public void setExpanderPanelIcon(String fontIcon) {
        if (isDefaultPanel() && null != ExpanderViewTitleDefaultIcon) {
            ExpanderViewTitleDefaultIcon.setText(fontIcon);
            if (null != fontIcon) {
                ExpanderViewTitleDefaultIcon.setVisibility(VISIBLE);
            } else {
                ExpanderViewTitleDefaultIcon.setVisibility(GONE);
            }
        }
    }


    /**
     * Sets expander theme.
     *
     * @Since: 1805.0.0
     */
    public void setExpanderTheme() {

    }


    /**
     * Get title label label.
     * This method is exposed so that user can customise this label attributes
     *
     * @return the label
     * @Since: 1805.0.0
     */
    public Label getTitleLabel() {
        Label label = null;
        if (isDefaultPanel() && null != ExpanderViewTitleDefaultLabel) {
            label = ExpanderViewTitleDefaultLabel;
        }
        return label;
    }


    /**
     * Is expander current state is expanded.
     * Note: If the setExpanderContentView API is not set then this API has no effect.
     * Default: false
     *
     * @return the boolean
     * @Since: 1805.0.0
     */
    public boolean isExpanded() {
        return ExpanderViewContent.isShown();
    }


    /**
     * To expand/collapse the Expander through code.
     *
     * @param aBoolean the a boolean
     * @Since: 1805.0.0
     */
    public void expand(boolean aBoolean) {
        if (aBoolean) {
            showContentView();
        } else {
            hideContentView();
        }
    }


    /**
     * set the visibility of the Expander's separator (show/hide)
     *
     * @param aBoolean the a boolean
     *                 Default: true
     * @Since: 1805.0.0
     */
    public void setSeparatorVisible(boolean aBoolean) {
        separatorVisibility = aBoolean;
        // update separator visibility instantly
        if (aBoolean) {
            if (null != ExpanderViewContent && ExpanderViewContent.isShown()) {
                contentBottomSeparator.setVisibility(VISIBLE);
            } else {
                titleBottomSeparator.setVisibility(VISIBLE);
            }
        } else {
            if (null != titleBottomSeparator) {
                titleBottomSeparator.setVisibility(GONE);
            }
            if (null != contentBottomSeparator) {
                contentBottomSeparator.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.uid_expander_view_title) {
            expandOrCollapse();
        }
    }


    /**
     * Sets expander delegate to get expand/collapse callbacks for ONLY default title view.
     * If this method is not called, expander will not get callbacks
     * Also if setExpanderCustomPanelView method is called, expander will not get callbacks
     *
     * @param uidExpanderListener the uid expander delegate
     *                            Default: null
     * @Since: 1805.0.0
     */
    public void setExpanderListener(UIDExpanderListener uidExpanderListener) {
        if (isDefaultPanel()) { // only default expander title view gets expand/collapse callback
            this.uidExpanderListener = uidExpanderListener;
        }
    }


    // private Helper methods

    private void setLayout(int resource, RelativeLayout expanderParentlayout) {
        expanderParentlayout.removeAllViews(); // remove old view if any
        LayoutInflater layoutInflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(resource, expanderParentlayout, false);
        expanderParentlayout.addView(contentView);
    }

    private void setLayout(View view, RelativeLayout expanderParentlayout) {
        expanderParentlayout.removeAllViews(); // remove old view if any
        expanderParentlayout.addView(view);
    }

    private void expandOrCollapse() {
        if (ExpanderViewContent.isShown()) {
            hideContentView();
        } else {
            showContentView();
        }
    }

    private void showContentView() {
        if (null != ExpanderViewContent && null != ExpanderViewContent.getChildAt(0)) { // expand only if content view is present
            titleBottomSeparator.setVisibility(GONE);
            ExpanderViewContent.setVisibility(View.VISIBLE);
            if (isSeperatorVisibile()) {
                contentBottomSeparator.setVisibility(View.VISIBLE);
            }
            chevronLabel.setText(mContext.getResources().getString(R.string.dls_navigationup));
            if (null != uidExpanderListener) { // call delegate should be called only for default expander panel
                uidExpanderListener.expanderPanelExpanded(); // expand end callback
            }
        }
    }

    private void hideContentView() {

        ExpanderViewContent.setVisibility(View.GONE);
        contentBottomSeparator.setVisibility(GONE);
        if (isSeperatorVisibile()) {
            titleBottomSeparator.setVisibility(VISIBLE);
        }
        chevronLabel.setText(mContext.getResources().getString(R.string.dls_navigationdown));
        if (null != uidExpanderListener) { // call delegate should be called only for default expander panel
            uidExpanderListener.expanderPanelCollapsed(); // collapse start callback
        }
    }

    // is Expander panel is having the default title panel
    private boolean isDefaultPanel() {
        boolean isDefault = false;
        if (null != ExpanderViewTitle && null != ExpanderViewTitle.getChildAt(0) && -1 != ExpanderViewTitle.getChildAt(0).getId()) {
            isDefault = (ExpanderViewTitle.getChildAt(0).getId() == R.id.uid_expander_title_layout_default) ? true : false;
        }
        return isDefault;
    }


    // to get current visibility of seperator
    private boolean isSeperatorVisibile() {
        return separatorVisibility;
    }
}
