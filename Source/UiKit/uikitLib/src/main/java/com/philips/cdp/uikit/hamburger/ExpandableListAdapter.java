package com.philips.cdp.uikit.hamburger;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<HamburgerItem>> listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<HamburgerItem>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
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
        }

        TextView hamburgerItemText = (TextView) convertView
                .findViewById(R.id.hamburger_item_text);

        VectorDrawableImageView imageView = (VectorDrawableImageView) convertView
                .findViewById(R.id.hamburger_list_icon);

        TextView hamburgerItemCounter = (TextView) convertView
                .findViewById(R.id.list_counter);

        setValuesToViews(hamburgerItem, imageView, hamburgerItemText, hamburgerItemCounter);
        return convertView;
    }

    private void setValuesToViews(final HamburgerItem hamburgerItem, final VectorDrawableImageView imgIcon, final TextView txtTitle, final TextView txtCount) {
        int icon = hamburgerItem.getIcon();
        setImageView(imgIcon, icon);
        txtTitle.setText(hamburgerItem.getTitle());
        String count = hamburgerItem.getCount();
        setTextView(txtCount, count);
    }

    private void setImageView(final VectorDrawableImageView imgIcon, final int icon) {
        if (icon > 0) {
            imgIcon.setImageDrawable(VectorDrawable.create(context, icon));
        } else {
            imgIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void setTextView(final TextView txtCount, final String count) {
        if (count != null && !count.equals("0")) {
            txtCount.setText(count);
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
        }

        TextView hamburgerHeaderTitle = (TextView) convertView
                .findViewById(R.id.hamburger_header);

        hamburgerHeaderTitle.setText(headerTitle);
        ExpandableListView expandableListView = (ExpandableListView) parent;
        expandableListView.expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}