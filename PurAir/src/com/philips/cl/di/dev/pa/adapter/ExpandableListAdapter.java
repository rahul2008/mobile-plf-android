package com.philips.cl.di.dev.pa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private int[] colorListHeader; // header titles
	// child data in format of header title, child title
	private int[][] colorListChildData;
	private int[] colorList;
	private String[] colorListLbl;

	public ExpandableListAdapter(Context context, int[] listDataHeader,
			int[][] listChildData, int[] colorList, String[] colorListLbl) {
		this.context = context;
		this.colorListHeader = listDataHeader;
		this.colorListChildData = listChildData;
		this.colorList = colorList;
		this.colorListLbl = colorListLbl;
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
		
		LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = infalInflater.inflate(R.layout.expandable_list_child,	null);
		
		FontTextView childTV = (FontTextView) view.findViewById(R.id.expandable_list_child_tv);
		childTV.setText(colorListChildData[groupPosition][childPosition]);
		return view;
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
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.air_color_group_item,	null);
		}

		FontTextView lblListHeader = (FontTextView) convertView.findViewById(R.id.color_indication_text);
		lblListHeader.setText(colorListHeader[groupPosition]);

		FontTextView lblColor = (FontTextView) convertView.findViewById(R.id.color_lbl);
		lblColor.setBackgroundResource(colorList[groupPosition]);
		lblColor.setText(colorListLbl[groupPosition]);
		
		ImageView indicator = (ImageView)convertView.findViewById(R.id.expandable_list_indicator);
		if (isExpanded) {
			indicator.setImageResource(R.drawable.up_arrow_list_item);
		} else {
			indicator.setImageResource(R.drawable.down_arrow_list_item);
		}

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