package com.philips.cl.di.dev.pa.scheduler;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FanspeedFragment extends BaseFragment implements SchedulerFanspeedListener {
	private ListView listView;
	private int selectItemPosition;
	private String[] fanModes;
	private FanSpeedAdapter fanSpeedAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ALog.i("Scheduler", "FanspeedFragment::onCreateView method beginning is called ");
		View view = inflater.inflate(R.layout.repeat_scheduler, null);
		initViews(view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		MetricsTracker.trackPage(TrackPageConstants.SCHEDULE_FAN_SPEED);
		
		String fanSpeedTemp = getArguments().getString(SchedulerConstants.SPEED);
		
		fanModes = getResources().getStringArray(R.array.fanspeed_array);
		selectItemPosition = SchedulerUtil.getFanspeedItemPosition(fanSpeedTemp);
		fanSpeedAdapter = new FanSpeedAdapter(getActivity(), 
				R.layout.repeat_scheduler_item, fanModes, selectItemPosition, this);
		listView.setAdapter(fanSpeedAdapter);
		((SchedulerActivity)getActivity()).setFanSpeed(getFanspeed(selectItemPosition));
		
		ViewGroup container = (LinearLayout) getView().findViewById(R.id.containerLL);
		setBackground(container, R.drawable.ews_nav_bar_2x, Color.BLACK, .1F);
	}
	
	private void initViews(View view) {
		listView = (ListView) view.findViewById(R.id.repeat_scheduler);
		selectItemPosition = 0;
		((ImageView) view.findViewById(R.id.scheduler_back_img)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SchedulerActivity activity = (SchedulerActivity) getActivity();
				if (activity != null) {
					activity.onBackPressed();
				}
			}
		});
		FontTextView headingTV = (FontTextView) view.findViewById(R.id.scheduler_heading_tv);
		headingTV.setText(getString(R.string.fanspeed));
	}
	
	private String getFanspeed(int position) {
		switch (position) {
		case 0:
			return AppConstants.FAN_SPEED_AUTO ;
		case 1:
			return AppConstants.FAN_SPEED_SILENT ;
		case 2:
			return AppConstants.FAN_SPEED_ONE;
		case 3:
			return AppConstants.FAN_SPEED_TWO;
		case 4:
			return AppConstants.FAN_SPEED_THREE ;
		case 5:
			return AppConstants.FAN_SPEED_TURBO;
		case 6:
			return AppConstants.FAN_SPEED_OFF;
		default:
			return "" ;

		}
	}

	@Override
	public void itemClick(int position) {
		((SchedulerActivity)getActivity()).setFanSpeed(getFanspeed(position));
	}
}
