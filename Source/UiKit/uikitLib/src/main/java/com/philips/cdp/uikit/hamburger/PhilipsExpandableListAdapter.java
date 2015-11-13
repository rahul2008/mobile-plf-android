package com.philips.cdp.uikit.hamburger;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.HashMap;
import java.util.List;

public class PhilipsExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<HamburgerItem>> listDataChild;
    private int totalCount = 0;

    public PhilipsExpandableListAdapter(Context context, List<String> listDataHeader,
                                        HashMap<String, List<HamburgerItem>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        calculateCount();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final HamburgerItem hamburgerItem = (HamburgerItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.uikit_drawer_list_item, parent, false);
            addStates(convertView);
        }

        TextView hamburgerItemText = (TextView) convertView
                .findViewById(R.id.hamburger_item_text);

        VectorDrawableImageView imageView = (VectorDrawableImageView) convertView
                .findViewById(R.id.hamburger_list_icon);

        TextView hamburgerItemCounter = (TextView) convertView
                .findViewById(R.id.list_counter);

        setValuesToViews(hamburgerItem, imageView, hamburgerItemText, hamburgerItemCounter);
        addStatesToText(hamburgerItemText);
        return convertView;
    }

    private void setValuesToViews(final HamburgerItem hamburgerItem, final VectorDrawableImageView imgIcon, final TextView txtTitle, final TextView txtCount) {
        int icon = hamburgerItem.getIcon();
        setImageView(imgIcon, icon, txtTitle);
        txtTitle.setText(hamburgerItem.getTitle());
        int count = hamburgerItem.getCount();
        setTextView(txtCount, count);
    }

    private void setImageView(final VectorDrawableImageView imgIcon, final int icon, TextView txtTitle) {
        if (icon > 0) {
            imgIcon.setImageDrawable(VectorDrawable.create(context, icon));
        } else {
            imgIcon.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) txtTitle.getLayoutParams();
            layoutParams.leftMargin = (int) context.getResources().getDimension(R.dimen.uikit_hamburger_item_title_left_margin);
            txtTitle.setLayoutParams(layoutParams);
        }
    }

    private void setTextView(final TextView txtCount, final int count) {
        if (count > 0) {
            txtCount.setText(String.valueOf(count));
            totalCount += count;
        } else {
            txtCount.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.uikit_hamburger_list_group, parent, false);
            setGroupLayoutAlpha(convertView);
        }
        TextView hamburgerHeaderTitle = (TextView) convertView
                .findViewById(R.id.hamburger_header);
        hamburgerHeaderTitle.setText(headerTitle);
        ExpandableListView expandableListView = (ExpandableListView) parent;
        expandableListView.expandGroup(groupPosition);
        return convertView;
    }

    private void setGroupLayoutAlpha(View convertView) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor});
        int colorPrimary = typedArray.getColor(0, 0);
        int newColor = Color.argb(context.getResources().getInteger(R.integer.uikit_hamburger_menu_group_layout_alpha), Color.red(colorPrimary), Color.green(colorPrimary), Color.blue(colorPrimary));
        typedArray.recycle();
        convertView.setBackgroundColor(newColor);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getDrawable(): sticking with deprecated API for now
    private void addStates(View convertView) {
        StateListDrawable states = new StateListDrawable();
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.brightColor});
        states.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(typedArray.getColor(0, -1)));
        convertView.setBackgroundDrawable(states);
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to context.getColor(): sticking with deprecated API for now
    private void addStatesToText(TextView txtTitle) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{R.attr.veryLightColor});
        int[][] states = new int[][]{new int[]{android.R.attr.state_activated}, new int[]{android.R.attr.state_pressed}, new int[]{-android.R.attr.state_activated}};
        int[] colors = new int[]{context.getResources().getColor(android.R.color.white), context.getResources().getColor(android.R.color.white), typedArray.getColor(0, -1)};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        txtTitle.setTextColor(colorStateList);
    }

    public String calculateCount() {
        for (String header : listDataHeader) {
            List<HamburgerItem> hamburgerItems = listDataChild.get(header);
            for (HamburgerItem hamburgerItem : hamburgerItems) {
                totalCount += hamburgerItem.getCount();
            }
        }
        return String.valueOf(totalCount);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        totalCount = 0;
        calculateCount();
    }

    public int getCounterValue() {
        return totalCount;
    }
}