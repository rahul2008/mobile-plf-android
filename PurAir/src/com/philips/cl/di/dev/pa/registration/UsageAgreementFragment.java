package com.philips.cl.di.dev.pa.registration;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver.ConnectionState;
import com.philips.cl.di.dev.pa.view.FontButton;
import com.philips.cl.di.dev.pa.view.FontTextView;
import com.philips.cl.di.reg.errormapping.Error;

public class UsageAgreementFragment extends BaseFragment implements OnClickListener {

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
		View view = inflater.inflate(R.layout.air_registration_usage_agreement_fragment, container, false);
		mExpListView = (ExpandableListView) view.findViewById(R.id.elv_usage_agreement); 
		FontButton declineBtn = (FontButton) view.findViewById(R.id.user_agreement_decline_btn);
		FontButton acceptBtn = (FontButton) view.findViewById(R.id.user_agreement_accept_btn);
		declineBtn.setOnClickListener(this);
		acceptBtn.setOnClickListener(this);
		
		mListAdapter = new CustomExpandableListAdapter(getActivity(), ARR_GROUP_ELEMENTS, ARR_CHILD_ELEMENTS);
		mExpListView.setAdapter(mListAdapter);
		
		// Expand all items by default
		for (int i=0; i < ARR_GROUP_ELEMENTS.length; i++) {
			mExpListView.expandGroup(i);
		}
		
		// Remove default group indicator because we use our own
		mExpListView.setGroupIndicator(null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.USAGE_AGREEMENT);
		
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
	    	LayoutInflater infalInflater = 
	    			(LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = infalInflater.inflate(R.layout.expandable_list_child,	null);
			
			FontTextView childTV = (FontTextView) view.findViewById(R.id.expandable_list_child_tv);
			childTV.setText(mListChildData[groupPosition][childPosition]);
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

	@Override
	public void onClick(View view) {
		UserRegistrationActivity activity = (UserRegistrationActivity) getActivity();
		if (activity == null) return;
		switch (view.getId()) {
		case R.id.user_agreement_decline_btn:
			activity.onBackPressed();
			break;
		case R.id.user_agreement_accept_btn:
			if (ConnectionState.DISCONNECTED == NetworkReceiver.getInstance().getLastKnownNetworkState()) {
				activity.showErrorDialog(Error.NO_NETWORK_CONNECTION);
				break;
			}
			activity.showFragment(new CreateAccountFragment());
			break;
		default:
			break;
		}
		
	}
}
