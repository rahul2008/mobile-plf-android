package com.philips.cl.di.dev.pa.registration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.CustomExpandableListAdapter;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver;
import com.philips.cl.di.dev.pa.util.networkutils.NetworkReceiver.ConnectionState;
import com.philips.cl.di.dev.pa.view.FontButton;
import com.philips.cl.di.reg.errormapping.Error;

public class UsageAgreementFragment extends BaseFragment implements OnClickListener {
 
	/**
	 * strings for group elements
	 */
	public static final int ARR_GROUP_ELEMENTS[] ={
		R.string.privacy_policy,
		R.string.terms_and_conditions,
		R.string.eula
	};

	/**
	 * strings for child elements
	 */
	public static final int ARR_CHILD_ELEMENTS[][] =
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
		
		// Remove default group indicator because we use our own
		mExpListView.setGroupIndicator(null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.USAGE_AGREEMENT);
		
		
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
 