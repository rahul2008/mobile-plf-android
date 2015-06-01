package com.philips.cl.di.dev.pa.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.registration.UsageAgreementFragment;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class TermsAndConditionsFragment extends BaseFragment{
	private ExpandableListView mExpListView;
	private BaseExpandableListAdapter mListAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.terms_and_conditions,null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	public void initView() { 
		ImageButton backButton = (ImageButton) getView().findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		FontTextView heading=(FontTextView)getView().findViewById(R.id.heading_name_tv);
		heading.setText(getResources().getString(R.string.terms_and_conditions));

		mExpListView = (ExpandableListView)getView().findViewById(R.id.elv_usage_agreement); 

		final MainActivity activity = ((MainActivity)getActivity());
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.onBackPressed();
			}
		});

		mListAdapter = new CustomExpandableListAdapter(activity, UsageAgreementFragment.ARR_GROUP_ELEMENTS, UsageAgreementFragment.ARR_CHILD_ELEMENTS);
		mExpListView.setAdapter(mListAdapter);

		// Remove default group indicator because we use our own
		mExpListView.setGroupIndicator(null);
	}








}
