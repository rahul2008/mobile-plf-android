package com.philips.cl.di.dev.pa.scheduler;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cdp.dicommclient.port.common.ScheduleListPortInfo;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerOverViewAdapter extends ArrayAdapter<ScheduleListPortInfo> {
	private Context context;
	private List<ScheduleListPortInfo> schedulers;
	private HashMap<Integer, Boolean> selectedItems;
	private boolean edit;
	private SchedulerEditListener listener;
	
	public SchedulerOverViewAdapter(Context context, int resource, List<ScheduleListPortInfo> schedulers,
			HashMap<Integer, Boolean> selectedItems, boolean edit, SchedulerEditListener listener) {
		super(context, resource, schedulers);
		this.context = context;
		this.schedulers = schedulers;
		this.selectedItems = selectedItems;
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
		final ImageView editImageView = (ImageView) view.findViewById(R.id.list_item_delete);
		editImageView.setClickable(false);
		editImageView.setFocusable(false);
		final ImageView arrowImageView = (ImageView) view.findViewById(R.id.list_item_right_arrow);
		arrowImageView.setImageResource(R.drawable.right_arrow_list_item);
		final FontTextView deleteTV = (FontTextView) view.findViewById(R.id.list_item_right_text);
		
		final int tempPosition = position;
		
		if (schedulers.get(position).getName() != null) {
			name.setText(schedulers.get(position).getName());
		}
		
		final int scheduleNum = schedulers.get(position).getScheduleNumber();
		
		if (edit) {
			editImageView.setVisibility(View.VISIBLE);
			arrowImageView.setVisibility(View.INVISIBLE);
			editImageView.setImageResource(R.drawable.blue_cross);
			deleteTV.setVisibility(View.INVISIBLE);
			if (selectedItems.containsKey(scheduleNum) && selectedItems.get(scheduleNum)) {
				editImageView.setImageResource(R.drawable.red_cross);
				deleteTV.setVisibility(View.VISIBLE);
			} 
			
		} else {
			editImageView.setVisibility(View.GONE);
			arrowImageView.setVisibility(View.VISIBLE);
			deleteTV.setVisibility(View.INVISIBLE);
		}
		
		deleteTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ALog.i(ALog.SCHEDULER, "selectedItems2"+ selectedItems);
				if (selectedItems.containsKey(scheduleNum)) {
					selectedItems.remove(scheduleNum);
				}
				listener.onDeleteSchedule(tempPosition, selectedItems);
			}
		});
		
		mainLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit) {
					addSelectedItemInMap(scheduleNum);
					
					if (!selectedItems.get(scheduleNum)) {
						editImageView.setImageResource(R.drawable.blue_cross);
						deleteTV.setVisibility(View.INVISIBLE);
					} else {
						editImageView.setImageResource(R.drawable.red_cross);
						deleteTV.setVisibility(View.VISIBLE);
					}
				} else {
					listener.onEditSchedule(tempPosition);
				}
			}
		});
		return view;
	}
	
	private void addSelectedItemInMap(int scheduleNum) {
		if (selectedItems.containsKey(scheduleNum)) {
			selectedItems.put(scheduleNum, !selectedItems.get(scheduleNum));
		} else {
			selectedItems.put(scheduleNum, true);
		}
	}
	
}

