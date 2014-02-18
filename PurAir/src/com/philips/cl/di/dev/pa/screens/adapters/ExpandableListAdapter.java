package com.philips.cl.di.dev.pa.screens.adapters;

import java.util.HashMap;
import java.util.List;

import com.philips.cl.di.dev.pa.R;
 
import com.philips.cl.di.dev.pa.utils.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
 
public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context context;
    private int[] colorListHeader; // header titles
    // child data in format of header title, child title
    private int[][] colorListChildData;
    private int[] colorList;
    private String[] colorListLbl;
 
    public ExpandableListAdapter(Context context, int[] listDataHeader,
            int [][] listChildData, int[] colorList, String[] colorListLbl) {
        this.context = context;
        this.colorListHeader = listDataHeader;
        this.colorListChildData = listChildData;
        this.colorList=colorList;
        this.colorListLbl=colorListLbl;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosition) {
     return null;
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) { 
       
    	TextView childText= new TextView(context);
    	childText.setPadding(0, 20, 0, 20);
    	childText.setTypeface(Fonts.getGillsans(context));
    	childText.setTextColor(context.getResources().getColor(R.color.gray));
    	childText.setText(colorListChildData[groupPosition][childPosition]);
        return childText;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
     return colorListChildData[groupPosition].length;
    }
   
    @Override
    public Object getGroup(int groupPosition) {
     return null;
    }
   
    @Override
    public int getGroupCount() {
     return colorListHeader.length;
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
            convertView = infalInflater.inflate(R.layout.air_color_group_item, null);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.color_indication_text);
        lblListHeader.setTypeface(Fonts.getGillsans(context));
        lblListHeader.setText(colorListHeader[groupPosition]);
        
        TextView lblColor = (TextView) convertView.findViewById(R.id.color_lbl); 
        lblColor.setBackgroundResource(colorList[groupPosition]);
        lblColor.setText(colorListLbl[groupPosition]);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}