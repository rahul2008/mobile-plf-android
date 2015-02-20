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

public class RepeatAdapter extends ArrayAdapter<String> {
	private Context context;
	private String[] weekDays;
	private boolean[] daysSelected;
	private SchedulerRepeatListener listener;

	public RepeatAdapter(Context context, int resource, String[] weekDays,
			boolean[] daysSelected, SchedulerRepeatListener listener) {
		super(context, resource, weekDays);
		this.context = context;
		this.weekDays = weekDays;
		this.daysSelected = daysSelected;
		this.listener = listener;
		ALog.i(ALog.SCHEDULER,
				"RepeatFragment-DaysAdapter () method constructor enter size: "
						+ weekDays.length);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.repeat_scheduler_item, null);
		final FontTextView day = (FontTextView) view.findViewById(R.id.rs_daytxt);
		final LinearLayout dayLayout = (LinearLayout) view.findViewById(R.id.rs_main_lyt);
		final ImageView tickImageView = (ImageView) view.findViewById(R.id.rs_day_tick_iv);
		tickImageView.setVisibility(View.GONE);
		day.setText(weekDays[position]);

		final int tempPosition = position;
		if (daysSelected[tempPosition]) {
			tickImageView.setVisibility(View.VISIBLE);
		}

		dayLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				daysSelected[tempPosition] = !daysSelected[tempPosition];
				if (daysSelected[tempPosition]) {
					tickImageView.setVisibility(View.VISIBLE);
				} else {
					tickImageView.setVisibility(View.GONE);
				}
				listener.onItemClick(daysSelected);
			}
		});

		return view;
	}
}