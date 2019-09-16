package com.ecs.demotestuapp.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.model.ButtonConfig;
import com.ecs.demotestuapp.model.PropertyItem;

public class EcsExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private LinkedList<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;


    private HashMap<String, List<PropertyItem>> hashMap;

    public EcsExpandableListAdapter(Context context, ButtonConfig buttonConfig) {
        this._context = context;
        hashMap = new HashMap<>();

        for(PropertyItem propertyItem:buttonConfig.property){
            hashMap.put(propertyItem.name,propertyItem.getProperty());
        }
        this._listDataHeader = new LinkedList<>(hashMap.keySet());
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return hashMap.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_child, null);
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

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
