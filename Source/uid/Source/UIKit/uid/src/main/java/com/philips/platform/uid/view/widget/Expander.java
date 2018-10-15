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


    private UIDExpanderDelegate uidExpanderDelegate;
    private RelativeLayout ExpanderViewTitle;
    private Label ExpanderViewTitleDefaultLabel;


    private Label ExpanderViewTitleDefaultIcon;
    private RelativeLayout ExpanderViewContent;
    private Label chevronLabel;
    private Context mContext;
    private View titleBottomSeperator;
    private View contentBottomSeperator;


    private boolean seperatorVisibility = true;


    public Expander(Context context, @Nullable AttributeSet attrs) {
        super(context);
        mContext = context;
        initializeViews(context, attrs);
    }

    public Expander(@NonNull final Context context) {
        this(context, null);

    }

    private void initializeViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(UIDHelper.getPopupThemedContext(context));
        ViewGroup expanderLayout = (ViewGroup) inflater.inflate(R.layout.uid_expander, this);
        ExpanderViewTitle = (RelativeLayout) expanderLayout.findViewById(R.id.uid_expander_view_title);
        ExpanderViewTitle.setOnClickListener(this);
        ExpanderViewContent = (RelativeLayout) expanderLayout.findViewById(R.id.uid_expander_view_content);

        titleBottomSeperator = (View) expanderLayout.findViewById(R.id.uid_expander_title_bottom_divider);
        contentBottomSeperator = (View) expanderLayout.findViewById(R.id.uid_expander_content_bottom_divider);

        setLayout(R.layout.uid_expander_title_layout_default, ExpanderViewTitle); // set default expander panel title
        ExpanderViewTitleDefaultLabel = (Label) expanderLayout.findViewById(R.id.uid_expander_title_text);
        ExpanderViewTitleDefaultIcon = (Label) expanderLayout.findViewById(R.id.uid_expander_title_image);
        chevronLabel = (Label) expanderLayout.findViewById(R.id.uid_expander_title_chevron);

    }


    public void setExpanderCustomPanelView(int resourceLayoutId) {
        uidExpanderDelegate = null; // callback delegate is removed when custom panel is set
        setLayout(resourceLayoutId, ExpanderViewTitle);

    }

    public void setExpanderCustomPanelView(View resourceView) {
        uidExpanderDelegate = null; // callback delegate is removed when custom panel is set
        setLayout(resourceView, ExpanderViewTitle);
    }


    // content layout
    public void setExpanderContentView(int resourceLayoutId) {
        setLayout(resourceLayoutId, ExpanderViewContent);

    }

    public void setExpanderContentView(View resourceView) {
        setLayout(resourceView, ExpanderViewContent);
    }

    public void setExpanderTitle(String title) {
        if (isDefaultPanel() && null != ExpanderViewTitleDefaultLabel) {
            ExpanderViewTitleDefaultLabel.setText(title);
        }
    }

    public void setExpanderPanelIcon(String fontIcon) {
        if (isDefaultPanel() && null != ExpanderViewTitleDefaultIcon) {
            ExpanderViewTitleDefaultIcon.setText(fontIcon);
            ExpanderViewTitleDefaultIcon.setVisibility(VISIBLE);
        }
    }

    public void setExpanderTheme() {

    }


    public boolean isExpanded() {
        return ExpanderViewContent.isShown();
    }

    public void expand(boolean aBoolean) {
        if (aBoolean) {
            showContentView();
        } else {
            hideContentView();
        }

    }


    public void setSeparatorVisible(boolean aBoolean) {
        seperatorVisibility = aBoolean;
        // update separator visibility instantly
        if (aBoolean) {
            if (null != ExpanderViewContent && ExpanderViewContent.isShown()) {
                contentBottomSeperator.setVisibility(VISIBLE);
            } else {
                titleBottomSeperator.setVisibility(VISIBLE);
            }
        } else {
            titleBottomSeperator.setVisibility(GONE);
            contentBottomSeperator.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        if (viewId == R.id.uid_expander_view_title) {
            expandOrCollapse();
        }
    }


    public void setExpanderDelegate(UIDExpanderDelegate uidExpanderDelegate) {
        this.uidExpanderDelegate = uidExpanderDelegate;
    }

    // Helper methods



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
            if (null != ExpanderViewContent && null != ExpanderViewContent.getChildAt(0)) { // expand only if content view is present
                showContentView();
            }
        }
    }

    private void showContentView() {
        if (null != uidExpanderDelegate) { // call delegate should be called only for default expander panel
            uidExpanderDelegate.expanderPanelWillExpand(); // expand start callback
        }
        titleBottomSeperator.setVisibility(GONE);
        ExpanderViewContent.setVisibility(View.VISIBLE);
        if (isSeperatorVisibile()) {
            contentBottomSeperator.setVisibility(View.VISIBLE);
        }
        chevronLabel.setText(mContext.getResources().getString(R.string.dls_navigationup));
        if (null != uidExpanderDelegate) { // call delegate should be called only for default expander panel
            uidExpanderDelegate.expanderPanelDidExpand(); // expand end callback
        }
    }

    private void hideContentView() {
        if (null != uidExpanderDelegate) { // call delegate should be called only for default expander panel
            uidExpanderDelegate.expanderPanelWillCollapse(); // collapse start callback
        }
        ExpanderViewContent.setVisibility(View.GONE);
        contentBottomSeperator.setVisibility(GONE);
        if (isSeperatorVisibile()) {
            titleBottomSeperator.setVisibility(VISIBLE);
        }
        chevronLabel.setText(mContext.getResources().getString(R.string.dls_navigationdown));
        if (null != uidExpanderDelegate) { // call delegate should be called only for default expander panel
            uidExpanderDelegate.expanderPanelDidCollapse(); // collapse start callback
        }
    }

    // is Expander panel is having the default title panel
    private boolean isDefaultPanel() {
        return (ExpanderViewTitle.getChildAt(0).getId() == R.id.uid_expander_title_layout_default);
    }


    // to get current visibility of seperator
    private boolean isSeperatorVisibile() {
        return seperatorVisibility;
    }
}
