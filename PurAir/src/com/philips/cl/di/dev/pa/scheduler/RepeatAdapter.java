package com.philips.cl.di.dev.pa.scheduler;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.GraphConst;
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
		final FontTextView day = (FontTextView) view
				.findViewById(R.id.rs_daytxt);
		final RelativeLayout dayLayout = (RelativeLayout) view
				.findViewById(R.id.rs_main_lyt);
		day.setText(weekDays[position]);

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
				listener.onItemClick(daysSelected);
			}
		});

		return view;
	}
}