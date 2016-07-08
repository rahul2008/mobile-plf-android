/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.hamburger;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.utils.UikitUtils;

import java.util.ArrayList;

/**
 * <b> Hamburger adaptor is used to set view for Hamburger list
 */

public class HamburgerAdapter extends BaseAdapter {

    public static final int HEADER = 0;
    public static final int CHILD = 1;
    private final LayoutInflater mInflater;
    private Context context;
    private ArrayList<HamburgerItem> hamburgerItems;
    private int disabledColor;
    private int brightColor;
    private int selectedIndex;
    private int baseColor;
    private int groupAlpha = 0;
    private TextView mHamburgerTotalCountView;

    private boolean isFullScreen;
    private boolean isPreLollipop = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;

    /**
     * Provides auto update of total count
     * when total count text view is provided as input. If used from uikit, the resource id is {@code R.id.hamburger_count}
     * <br>
     * @param context            {@link Context} Context
     * @param hamburgerItems     {@link HamburgerItem} List of items to be shown in Hamburger menu
     * @param totalCountTextView {@link TextView} representing total count of all the items in the hamburger menu.
     *                                           Pass {@code null}, if no total count view is required.
     * @param isFullScreen       if {@code {@link Boolean#TRUE}}, it removes the top offset for drawer view. The offset accounts to match the status bar height.
     *                           If app running in fullscreen/immersive mode true can be applied.
     */
    public HamburgerAdapter(@NonNull Context context, @NonNull ArrayList<HamburgerItem> hamburgerItems, TextView totalCountTextView, boolean isFullScreen) {
        this.context = context;
        this.hamburgerItems = hamburgerItems;
        mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        setColors();
        groupAlpha = adjustAlpha(baseColor, 0.5f);
        mHamburgerTotalCountView = totalCountTextView;
        this.isFullScreen = isFullScreen;
        setCounterView(totalCountTextView, calculateCount());
    }


    /**
     * Preferred only when total badge count is not required, otherwise use {@link HamburgerAdapter#HamburgerAdapter(Context, ArrayList, TextView, boolean)}
     *
     * @param context            {@link Context} Context
     * @param hamburgerItems     {@link HamburgerItem} List of items to be shown in Hamburger menu
     */
    public HamburgerAdapter(Context context, ArrayList<HamburgerItem> hamburgerItems) {
        this(context, hamburgerItems, null, false);
    }

    /**
     * API to set row as selected
     *
     * @param index - index of row to be selected
     */
    public void setSelectedIndex(int index) {
        selectedIndex = index;
        notifyDataSetChanged();
    }

    /**
     * @return Returns the count of adapter
     */
    @Override
    public int getCount() {
        return hamburgerItems.size();
    }

    /**
     * @param position - index of row
     * @return - Returns Object of required index
     */
    @Override
    public Object getItem(int position) {
        return hamburgerItems.get(position);
    }

    /**
     * @param position
     * @return Returns id of position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolderItem;
        switch (getItemViewType(position)) {

            case HEADER:
                View parentView = convertView;
                if (parentView == null) {
                    parentView = mInflater.inflate(R.layout.uikit_hamburger_list_group, parent, false);
                    viewHolderItem = new ViewHolderItem();
                    initializeHeaderViews(parentView, viewHolderItem);
                    parentView.setSelected(false);
                    setGroupLayoutAlpha(viewHolderItem.parentView);
                    parentView.setTag(viewHolderItem);
                } else {
                    viewHolderItem = (ViewHolderItem) parentView.getTag();
                }
                addHeaderMargin(position, viewHolderItem.transparentView);
                viewHolderItem.txtTitle.setText(hamburgerItems.get(position).getTitle());
                return parentView;
            case CHILD:
                View childView = convertView;
                if (childView == null) {
                    childView = mInflater.inflate(R.layout.uikit_drawer_list_item, parent, false);
                    viewHolderItem = new ViewHolderItem();
                    initializeChildViews(childView, viewHolderItem);
                    childView.setSelected(false);
                    childView.setTag(viewHolderItem);
                } else {
                    viewHolderItem = (ViewHolderItem) childView.getTag();
                }
                validateBottomDivider(hamburgerItems.get(position), viewHolderItem.bottomDivider);
                addHeaderMargin(position, viewHolderItem.transparentView);
                setValuesToViews(position, viewHolderItem.imgIcon, viewHolderItem.txtTitle, viewHolderItem.txtCount, hamburgerItems.get(position), viewHolderItem.parentView);
                return childView;
        }
        return null;
    }

    private void initializeHeaderViews(View convertView, ViewHolderItem viewHolder) {
        viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.hamburger_item_text);
        viewHolder.transparentView = convertView.findViewById(R.id.transparentView);
        viewHolder.parentView = (RelativeLayout) convertView.findViewById(R.id.hamburger_parent);
    }

    private void addHeaderMargin(int position, View transparentView) {
        if (position == 0 && !(isFullScreen || isPreLollipop))
            transparentView.setVisibility(View.VISIBLE);
    }

    private void initializeChildViews(View convertView, ViewHolderItem viewHolder) {
        viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.hamburger_item_text);
        viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.hamburger_list_icon);
        viewHolder.txtCount = (TextView) convertView.findViewById(R.id.list_counter);
        viewHolder.bottomDivider = convertView.findViewById(R.id.divider_bottom);
        viewHolder.transparentView = convertView.findViewById(R.id.transparentView);
        viewHolder.parentView = (RelativeLayout) convertView.findViewById(R.id.hamburger_parent);
    }

    private void setColors() {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.uikit_veryLightColor, R.attr.uikit_brightColor, R.attr.uikit_baseColor});
        disabledColor = typedArray.getColor(0, -1);
        brightColor = typedArray.getColor(1, -1);
        baseColor = typedArray.getColor(2, -1);
        typedArray.recycle();
    }

    private void validateBottomDivider(HamburgerItem hamburgerItem, View bottomDivider) {
        if (hamburgerItem.isLastChild())
            bottomDivider.setVisibility(View.INVISIBLE);
    }


    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to setBackgroundDrawable(): sticking with deprecated API for now
    private void setGroupLayoutAlpha(View convertView) {
        convertView.setBackgroundColor(groupAlpha);
    }


    private int adjustAlpha(int color, float factor) {
        return UikitUtils.adjustAlpha(color, factor);
    }

    private void setValuesToViews(final int position, final ImageView imgIcon, TextView txtTitle, final TextView txtCount, final HamburgerItem hamburgerItem, final View convertView) {
        Drawable icon = hamburgerItems.get(position).getIcon();
        int count = hamburgerItems.get(position).getCount();
        setCounterView(txtCount, count);
        handleSelector(position, imgIcon, txtTitle, convertView, icon);
        txtTitle.setText(hamburgerItems.get(position).getTitle());
    }

    private void handleSelector(int position, ImageView imgIcon, TextView txtTitle, View convertView, Drawable icon) {
        if (selectedIndex != -1 && position == selectedIndex) {
            setTextColor(txtTitle, Color.WHITE);
            setImageView(imgIcon, icon, txtTitle, Color.WHITE);
            convertView.setBackgroundColor(brightColor);
        } else {
            setTextColor(txtTitle, disabledColor);
            setImageView(imgIcon, icon, txtTitle, disabledColor);
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    private void setCounterView(final TextView txtCount, final int count) {
        if (txtCount == null) {
            return;
        }
        setCountViewVisibility(txtCount, count);
        if (count > 0) {
            txtCount.setText(String.valueOf(count));
        }
    }

    private void setCountViewVisibility(TextView countView, int count) {
        int visibility = count > 0 ? View.VISIBLE : View.GONE;
        countView.setVisibility(visibility);
    }

    private void setImageView(final ImageView imgIcon, final Drawable icon, TextView txtTitle, int color) {
        if (icon != null) {
            imgIcon.setImageDrawable(icon);
            imgIcon.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        } else {
            imgIcon.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) txtTitle.getLayoutParams();
            layoutParams.leftMargin = (int) context.getResources().getDimension(R.dimen.uikit_hamburger_item_icon_left_margin);
            txtTitle.setLayoutParams(layoutParams);
        }
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void setTextColor(TextView txtTitle, int color) {
        txtTitle.setTextColor(color);
    }

    /**
     * @return Returns the badge count
     */
    public int getCounterValue() {
        return calculateCount();
    }

    /**
     * API to be called when Date set is updated
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        setCounterView(mHamburgerTotalCountView, calculateCount());
    }

    /**
     * API to be called to calculate total badge count
     */
    public int calculateCount() {
        int totalCount = 0;
        for (HamburgerItem hamburgerItem : hamburgerItems) {
            totalCount += hamburgerItem.getCount();
        }
        return totalCount;
    }

    @Override
    public int getItemViewType(int position) {
        HamburgerItem hamburgerItem = hamburgerItems.get(position);
        if (hamburgerItem.isParent())
            return HEADER;
        else
            return CHILD;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class ViewHolderItem {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtCount;
        View bottomDivider;
        View transparentView;
        RelativeLayout parentView;
    }
}
