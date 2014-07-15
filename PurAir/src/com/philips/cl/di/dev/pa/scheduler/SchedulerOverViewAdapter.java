package com.philips.cl.di.dev.pa.scheduler;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerOverViewAdapter extends ArrayAdapter<SchedulePortInfo> {
	private Context context;
	private List<SchedulePortInfo> schedulers;
	private ArrayList<Boolean> editList;
	private boolean edit;
	private SchedulerEditListener listener;
	
	public SchedulerOverViewAdapter(Context context, int resource, List<SchedulePortInfo> schedulers,
			ArrayList<Boolean> editList, boolean edit, SchedulerEditListener listener) {
		super(context, resource, schedulers);
		this.context = context;
		this.schedulers = schedulers;
		this.editList = editList;
		this.edit = edit;
		this.listener = listener;
		ALog.i(ALog.SCHEDULER, "RepeatFragment-DaysAdapter () method constructor enter " + schedulers);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.simple_list_item, null);
		final FontTextView name = (FontTextView) view.findViewById(R.id.list_item_name);
		final RelativeLayout mainLayout = (RelativeLayout) view.findViewById(R.id.ll_list_item);
		final ImageView deleteImg = (ImageView) view.findViewById(R.id.list_item_delete);
		final ImageView rhtArrImg = (ImageView) view.findViewById(R.id.list_item_right_arrow);
		final FontTextView rhtArrtxt = (FontTextView) view.findViewById(R.id.list_item_right_text);
		
		final int tempPosition = position;
		
		if (schedulers.get(position).getName() != null) {
			name.setText(schedulers.get(position).getName());
		}
		if (edit) {
			deleteImg.setVisibility(View.VISIBLE);
			rhtArrImg.setVisibility(View.VISIBLE);
			if (editList.get(tempPosition)) {
				deleteImg.setImageResource(R.drawable.delete_t2b);
				rhtArrImg.setVisibility(View.INVISIBLE);
				rhtArrtxt.setVisibility(View.VISIBLE);
			} 
		} else {
			deleteImg.setVisibility(View.GONE);
			rhtArrImg.setImageResource(R.drawable.about_air_quality_goto);
		}
		
		deleteImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editList.set(tempPosition, !editList.get(tempPosition));
				notifyDataSetChanged();
			}
		});
		
		rhtArrtxt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				schedulers.remove(tempPosition);
				editList.remove(tempPosition);
				notifyDataSetChanged();
				listener.onDeleteSchedule(tempPosition);
			}
		});
		
		mainLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onEditSchedule(tempPosition);
			}
		});
		return view;
	}
}

