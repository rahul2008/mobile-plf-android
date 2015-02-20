package com.philips.cl.di.dev.pa.scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FanSpeedAdapter extends ArrayAdapter<String> {
	private Context context;
	private String[] fanModes;
	private int selectedItemPosition;
	private SchedulerFanspeedListener listener;
	
	public FanSpeedAdapter(Context context, int resource, 
			String[] fanModes, int selectedPosition, SchedulerFanspeedListener listener) {
		super(context, resource, fanModes);
		this.context = context;
		this.fanModes = fanModes;
		this.selectedItemPosition = selectedPosition;
		this.listener = listener;
		ALog.i(ALog.SCHEDULER, "RepeatFragment-DaysAdapter () method constructor enter size: " + fanModes.length);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.repeat_scheduler_item, null);
		final FontTextView fanSpeed = (FontTextView) view.findViewById(R.id.rs_daytxt);
		final LinearLayout fanSpeedLayout = (LinearLayout) view.findViewById(R.id.rs_main_lyt);
		final ImageView tickImageView = (ImageView) view.findViewById(R.id.rs_day_tick_iv);
		tickImageView.setVisibility(View.GONE);
		
		final int tempPosition = position;
		fanSpeed.setText(fanModes[tempPosition]);
		if (tempPosition == selectedItemPosition) {
			tickImageView.setVisibility(View.VISIBLE);
		}
		
		fanSpeedLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				selectedItemPosition = tempPosition;
				notifyDataSetChanged();
				listener.itemClick(tempPosition);
			}
		});
		return view;
	}
}
