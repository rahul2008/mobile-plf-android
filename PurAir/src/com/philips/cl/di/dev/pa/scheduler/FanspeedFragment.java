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

public class FanspeedFragment extends BaseFragment {
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
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.FAN_SPEED);
		addValueToArray();
		String fanSpeedTemp = getArguments().getString(SchedulerConstants.SPEED);
		selectItemPosition = SchedulerUtil.getFanspeedItemPosition(fanModes, fanSpeedTemp);
		fanSpeedAdapter = new FanSpeedAdapter(getActivity(), R.layout.fanspeed_scheduler, fanModes);
		listView.setAdapter(fanSpeedAdapter);
	}
	
	private void initViews(View view) {
		listView = (ListView) view.findViewById(R.id.repeat_scheduler);
		selectItemPosition = -1;
	}
	
	private void addValueToArray() {
		fanModes = new String[6];
		fanModes[0] = getString(R.string.auto);
		fanModes[1] = getString(R.string.silent);
		fanModes[2] = getString(R.string.one);
		fanModes[3] = getString(R.string.two);
		fanModes[4] = getString(R.string.three);
		fanModes[5] = getString(R.string.turbo);
	}
	
	
	
	private class FanSpeedAdapter extends ArrayAdapter<String> {
		public FanSpeedAdapter(Context context, int resource, String[] objects) {
			super(context, resource, objects);
			ALog.i(ALog.SCHEDULER, "RepeatFragment-DaysAdapter () method constructor enter size: " + objects.length);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View view = inflater.inflate(R.layout.fanspeed_scheduler, null);
			final FontTextView fanSpeed = (FontTextView) view.findViewById(R.id.fanspeed_item);
			final RelativeLayout fanSpeedLayout = (RelativeLayout) view.findViewById(R.id.fanspeed_lyt);
			final int tempPosition = position;
			fanSpeed.setText(fanModes[tempPosition]);
			if (tempPosition == selectItemPosition) {
				fanSpeedLayout.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
				fanSpeed.setTextColor(Color.WHITE);
			}
			
			fanSpeedLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					selectItemPosition = tempPosition;
					fanSpeedAdapter.notifyDataSetChanged();
					((SchedulerActivity)getActivity()).dispatchInformations2(getFanspeed(tempPosition));
				}
			});
			
			return view;
		}
	}
	
	private String getFanspeed(int position) {
		switch (position) {
		case 0:
			return "a" ;
		case 1:
			return "s" ;
		case 2:
			return "1";
		case 3:
			return "2";
		case 4:
			return "3" ;
		case 5:
			return "t";
		default:
			return "" ;

		}
	}
	
}
