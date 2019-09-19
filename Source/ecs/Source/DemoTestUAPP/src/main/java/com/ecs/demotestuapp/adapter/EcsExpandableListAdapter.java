package com.ecs.demotestuapp.adapter;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.activity.EcsDemoResultActivity;
import com.ecs.demotestuapp.activity.FetchProductsInputActivity;
import com.ecs.demotestuapp.activity.FetchSummariesInputActivity;
import com.ecs.demotestuapp.activity.SpinnerInputActivity;
import com.ecs.demotestuapp.model.ButtonConfig;
import com.ecs.demotestuapp.model.PropertyItem;

public class EcsExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private LinkedList<String> _listDataHeader;

    private HashMap<String, List<PropertyItem>> hashMap;


    public EcsExpandableListAdapter(Context context,LinkedList<String> _listDataHeader,HashMap<String, List<PropertyItem>> hashMap) {
        this._context = context;
        this.hashMap = hashMap;
        this._listDataHeader = _listDataHeader;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return hashMap.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final PropertyItem propertyItem = (PropertyItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_child, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.tv_child);

        txtListChild.setText(propertyItem.name);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return hashMap.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return hashMap.size();
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_parent, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.tv_parent);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    public void gotoResultScreen(PropertyItem propertyItem) {

        Intent intent = new Intent(_context, EcsDemoResultActivity.class);
        if(propertyItem.needInput){
            intent = getInputScreenIntent(propertyItem);
        }


        Bundle bundle = new Bundle();
        bundle.putSerializable("property",propertyItem);

        intent.putExtras(bundle);
        _context.startActivity(intent);
    }

    private Intent getInputScreenIntent(PropertyItem propertyItem) {

        switch (propertyItem.apiNumber){

            case 4:
                return new Intent(_context, FetchProductsInputActivity.class);

            case 5:
                return new Intent(_context, FetchSummariesInputActivity.class);

            case 6:
                return new Intent(_context, FetchSummariesInputActivity.class);

            case 15:
                return new Intent(_context, SpinnerInputActivity.class);


        }
        return new Intent(_context, EcsDemoResultActivity.class);
    }

    public PropertyItem getPropertyItemFromButtonHeaderName(ButtonConfig buttonConfig, String headerTitle) {

        List<PropertyItem> property = buttonConfig.property;

        for(PropertyItem propertyItem:property){
            if(propertyItem.name.equalsIgnoreCase(headerTitle)){
                return propertyItem;
            }
        }
        return null;
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
