package com.philips.cl.di.dev.pa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.buyonline.AppUtils;
import com.philips.cl.di.dev.pa.buyonline.FeedbackActivity;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class RateAndFeedbackFragment extends BaseFragment implements OnClickListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.rate_and_feedback, container, false);
		initViews(view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage("Rate And Feedback");
	}

	private void initViews(View view) {
		ImageButton backButton = (ImageButton) view.findViewById(R.id.heading_back_imgbtn);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);
		FontTextView heading=(FontTextView) view.findViewById(R.id.heading_name_tv);
		heading.setText(getString(R.string.rate_and_feedback));
		
		FontTextView rateThisApp = (FontTextView) view.findViewById(R.id.rate_this_app);
		rateThisApp.setOnClickListener(this);
		
		FontTextView shareFeedback = (FontTextView) view.findViewById(R.id.share_feedback);
		shareFeedback.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		MainActivity activity = (MainActivity) getActivity();
		switch (v.getId()) {
		case R.id.rate_this_app:
			MetricsTracker.trackActionRateThisApp();
			AppUtils.startMarketCommend(getActivity(), getActivity().getPackageName());
			break;
		case R.id.share_feedback:
			Intent intent=new Intent(getActivity(), FeedbackActivity.class);
			activity.startActivity(intent);
			break;
		case R.id.heading_back_imgbtn:
			activity.onBackPressed();
			break;
		}
	}
}
