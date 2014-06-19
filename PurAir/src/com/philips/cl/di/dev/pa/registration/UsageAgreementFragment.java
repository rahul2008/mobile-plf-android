package com.philips.cl.di.dev.pa.registration;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.FontLoader;

public class UsageAgreementFragment extends BaseFragment {

	/**
	 * strings for group elements
	 */
	private static final int ARR_GROUP_ELEMENTS[] ={
		R.string.privacy_policy,
		R.string.terms_and_conditions,
		R.string.eula
	};

	/**
	 * strings for child elements
	 */
	private static final int ARR_CHILD_ELEMENTS[][] =
		{
		{
			R.string.privacy_policy_text
		},
		{
			R.string.terms_and_conditions_text
		},
		{
			R.string.eula_text
		}
		};

	private ExpandableListView mExpListView;
	private BaseExpandableListAdapter mListAdapter;
	private boolean[] expandCollapse = {true, true, true};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.air_registration_usage_agreement_fragment, container, false);
		mExpListView = (ExpandableListView) layout.findViewById(R.id.elv_usage_agreement); 
		
		mListAdapter = new CustomExpandableListAdapter(getActivity(), ARR_GROUP_ELEMENTS, ARR_CHILD_ELEMENTS);
		mExpListView.setAdapter(mListAdapter);
		
		// Expand all items by default
		for (int i=0; i < ARR_GROUP_ELEMENTS.length; i++) {
			mExpListView.expandGroup(i);
		}
		
		// Remove default group indicator because we use our own
		mExpListView.setGroupIndicator(null);
		return layout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mExpListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			
			@Override
			public void onGroupCollapse(int groupPosition) {
				expandCollapse[groupPosition] = false;
				mListAdapter.notifyDataSetChanged();
			}
		});
		
		mExpListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				expandCollapse[groupPosition] = true;
				mListAdapter.notifyDataSetChanged();
			}
		});
		
	}

	private class CustomExpandableListAdapter extends BaseExpandableListAdapter {

		private Context mContext;
		private int[] mListHeader; // header titles
	    // child data in format of header title, child title
	    private int[][] mListChildData;
		
		private CustomExpandableListAdapter(Context context, int[] listHeaderData,
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
			TextView childText= new TextView(mContext);
	    	childText.setPadding(0, 20, 0, 20);
	    	FontLoader.getInstance().setTypeface(childText, "fonts/gillsans.ttf");
	    	childText.setTextColor(mContext.getResources().getColor(R.color.gray));
	    	childText.setText(mListChildData[groupPosition][childPosition]);
	    	childText.setLineSpacing(5, 1);
	        return childText;
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
	        	imgView.setImageResource(R.drawable.minus_blue_reg);
	        } else {
	        	imgView.setImageResource(R.drawable.plus_blue_reg);
	        }
	        
	        TextView lblListHeader = (TextView) convertView.findViewById(R.id.tv_agreement_description);
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
}
