/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<FaqBeanPojo>> listDataChild;
    private THSFaqFragment mTHSFaqFragment;

    public ExpandableListAdapter(THSFaqFragment thsFaqFragment, HashMap<String, List<FaqBeanPojo>> map) {
        this.mTHSFaqFragment = thsFaqFragment;
        this.context = mTHSFaqFragment.getContext();

        final Set set = map.keySet();
        this.listDataHeader = new ArrayList<>(set);
        this.listDataChild = map;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        final FaqBeanPojo faqBeanPojo = this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
        return faqBeanPojo;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final FaqBeanPojo childObject = (FaqBeanPojo) getChild(groupPosition, childPosition);
        final String childText = childObject.getQuestion();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.ths_faqs_expandable_list_group_item, null);
        }

        Label txtListChild = (Label) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(THSConstants.THS_FAQ_HEADER, listDataHeader.get(groupPosition).toString());
                bundle.putSerializable(THSConstants.THS_FAQ_ITEM, childObject);
                THSFaqAnswerFragment thsFaqAnswerFragment = new THSFaqAnswerFragment();
                mTHSFaqFragment.addFragment(thsFaqAnswerFragment, THSFaqAnswerFragment.TAG, bundle, false);
            }
        });

        return convertView;
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
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.ths_faqs_expandable_list_group, null);
        }

        Label lblListHeader = convertView
                .findViewById(R.id.lblListHeader);
        Label lblListHeaderShowAllorShowLess =  convertView
                .findViewById(R.id.lblshowAll);
        if(isExpanded){
            lblListHeaderShowAllorShowLess.setText(context.getString(R.string.ths_show_less));
        }else{
            lblListHeaderShowAllorShowLess.setText(context.getString(R.string.ths_show_all));
        }
        lblListHeader.setText(headerTitle);

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

