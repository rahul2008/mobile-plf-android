package com.philips.cl.di.dev.pa.fragment;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter{

	private boolean[] expandCollapse = {true, true, true};

	private Context mContext;
	private int[] mListHeader; // header titles
    // child data in format of header title, child title
    private int[][] mListChildData;
	
	public CustomExpandableListAdapter(Context context, int[] listHeaderData,
            int [][] listChildData) {
		mContext = context;
		mListHeader = listHeaderData;
		mListChildData = listChildData;
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
	public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	LayoutInflater infalInflater = 
    			(LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = infalInflater.inflate(R.layout.expandable_list_child,	null);
		
		FontTextView childTV = (FontTextView) view.findViewById(R.id.expandable_list_child_tv);
		String text = mContext.getString(mListChildData[groupPosition][childPosition]);
		childTV.setText(Html.fromHtml(text));
		return view;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mListChildData[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return mListHeader.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.air_registration_agreement_item, null);
        }
 
        ImageView imgView =  (ImageView) convertView.findViewById(R.id.iv_agreement_icon);
        
        if (expandCollapse[groupPosition]) {
        	imgView.setImageResource(R.drawable.up_arrow_list_item);
        } else {
        	imgView.setImageResource(R.drawable.down_arrow_list_item);
        }
        
        FontTextView lblListHeader = (FontTextView) convertView.findViewById(R.id.tv_agreement_description);
        lblListHeader.setText(mListHeader[groupPosition]);
        
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
