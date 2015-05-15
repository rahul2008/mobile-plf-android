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
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class RepeatFragment extends BaseFragment implements SchedulerRepeatListener {
	
	private ListView lstDays;
	private String[] days;
	private boolean[] daysSelected = {false, false, false, false, false, false, false};
	private RepeatAdapter repeatAdapter;
	private StringBuilder sSelectedDays;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::onCreateView() method enter");
		View view = inflater.inflate(R.layout.repeat_scheduler, null);
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
		headingTV.setText(getString(R.string.repeat_text));
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		lstDays = (ListView) getView().findViewById(R.id.repeat_scheduler);
		days = getResources().getStringArray(R.array.weekday_array);
		String selectedDays = getArguments().getString(SchedulerConstants.DAYS);
		daysSelected = SchedulerUtil.getSelectedDayList(days, selectedDays);
		repeatAdapter = new RepeatAdapter(getActivity(), R.layout.repeat_scheduler_item, days, daysSelected, this);
		lstDays.setAdapter(repeatAdapter);
		((SchedulerActivity)getActivity()).setDays(setDaysString());
		
		MetricsTracker.trackPage(TrackPageConstants.SCHEDULE_DAYS);
		
		ViewGroup container = (LinearLayout) getView().findViewById(R.id.containerLL);
		setBackground(container, R.drawable.ews_nav_bar_2x, Color.BLACK, .1F);
	}
	
	private String setDaysString() {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setDaysString() method enter");
		sSelectedDays = new StringBuilder();
		for (int i=0; i < daysSelected.length; i++) {
			if (daysSelected[i]) {
				sSelectedDays = sSelectedDays.append(i);
			}
		}
		return sSelectedDays.toString();
	}

	@Override
	public void onItemClick(boolean[] selectedItems) {
		daysSelected = selectedItems;
		((SchedulerActivity)getActivity()).setDays(setDaysString());
	}
}
