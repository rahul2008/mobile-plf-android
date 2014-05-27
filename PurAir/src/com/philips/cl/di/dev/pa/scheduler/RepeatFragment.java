package com.philips.cl.di.dev.pa.scheduler;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class RepeatFragment extends BaseFragment {
	
	private ListView lstDays;
	private String[] days;
	private boolean[] daysSelected = {false, false, false, false, false, false, false};
	private DaysAdapter daysAdapter;
	private StringBuilder sSelectedDays;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::onCreateView() method enter");
		View view = inflater.inflate(R.layout.repeat_scheduler, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.REPEAT);
		lstDays = (ListView) getView().findViewById(R.id.repeat_scheduler);
		addValues();
		daysAdapter = new DaysAdapter(getActivity(), R.layout.repeat_scheduler_item, days);
		lstDays.setAdapter(daysAdapter);
	}
	
	private void addValues() {
		days = new String[7];
		days[0] = getString(R.string.sunday);
		days[1] = getString(R.string.monday);
		days[2] = getString(R.string.tuesday);
		days[3] = getString(R.string.wednesday);
		days[4] = getString(R.string.thursday);
		days[5] = getString(R.string.friday);
		days[6] = getString(R.string.saturday);
	}
	
	private String setDaysString() {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setDaysString() method enter");
		sSelectedDays = new StringBuilder();
		for (int i=0; i<daysSelected.length; i++) {
			if (daysSelected[i]) {
				sSelectedDays = sSelectedDays.append(i);
			}
		}
		
		return sSelectedDays.toString();
	}
	
	private class DaysAdapter extends ArrayAdapter<String> {
		public DaysAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
			ALog.i(ALog.SCHEDULER, "RepeatFragment-DaysAdapter () method constructor enter size: " + objects.length);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View view = inflater.inflate(R.layout.repeat_scheduler_item, null);
			final FontTextView day = (FontTextView) view.findViewById(R.id.rs_daytxt);
			final RelativeLayout dayLayout = (RelativeLayout) view.findViewById(R.id.rs_main_lyt);
			day.setText(days[position]);
			
			final int tempPosition = position;
			if (daysSelected[tempPosition]) {
				dayLayout.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			}
			
			dayLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					daysSelected[tempPosition] = !daysSelected[tempPosition];
					if (daysSelected[tempPosition]) {
						dayLayout.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
						day.setTextColor(Color.WHITE);
					} else {
						dayLayout.setBackgroundColor(Color.WHITE);
						day.setTextColor(Color.GRAY);
					}
					((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
					
				}
			});
			
			return view;
		}
	}
}
