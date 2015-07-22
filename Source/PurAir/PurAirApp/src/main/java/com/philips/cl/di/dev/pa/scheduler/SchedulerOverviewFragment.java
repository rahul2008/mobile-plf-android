package com.philips.cl.di.dev.pa.scheduler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.philips.cdp.dicommclient.port.common.ScheduleListPortInfo;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontButton;
import com.philips.cl.di.dev.pa.view.FontTextView;

@SuppressLint("UseSparseArrays")
public class SchedulerOverviewFragment extends BaseFragment implements OnClickListener, SchedulerEditListener {

	private ListView lstView;
	private SchedulerOverViewAdapter schOverviewAdapter;
	private List<ScheduleListPortInfo> lstSchedulers;
	private boolean edit;
	private HashMap<Integer, Boolean> selectedItems;
	private FontButton editScheduleBtn;
	private FontTextView addScheduleTV;
	private ImageView addScheduleSeparator;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		selectedItems = new HashMap<Integer, Boolean>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::onCreateView() method enter");
		View view = inflater.inflate(R.layout.scheduler_overview, null);
		initViews(view);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (((SchedulerActivity) getActivity()).getSchedulerList() != null) {
			lstSchedulers.addAll(((SchedulerActivity) getActivity()).getSchedulerList());
		}
		setListData();
		MetricsTracker.trackPage(TrackPageConstants.SCHEDULE_OVERVIEW);
		
	}
	
	@Override
	public void onResume() {
		setVisibilityAddSchedule();
		super.onResume();
	}
	
	private void setListData() {
		schOverviewAdapter = new SchedulerOverViewAdapter(getActivity(),
				R.layout.simple_list_item, lstSchedulers, selectedItems, edit, this);
		lstView.setAdapter(schOverviewAdapter);
	}
	
	private void initViews(View view) {
		((ImageView) view.findViewById(R.id.scheduler_back_img)).setOnClickListener(this);
		FontTextView headingTV = (FontTextView) view.findViewById(R.id.scheduler_heading_tv);
		headingTV.setText(getString(R.string.scheduler));
		editScheduleBtn = (FontButton) view.findViewById(R.id.scheduler_save_btn);
		setEditButtonProperty();
		addScheduleTV = (FontTextView) view.findViewById(R.id.schedule_add_tv);
		addScheduleSeparator = (ImageView) view.findViewById(R.id.event_scheduler_listview_top_separator);
		addScheduleTV.setOnClickListener(this);
		lstView = (ListView) view.findViewById(R.id.event_scheduler_listview);
		lstSchedulers = new ArrayList<ScheduleListPortInfo>();
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::initViews() method exit");
	}
	
	private void setEditButtonProperty() {
		editScheduleBtn.setText(getString(R.string.edit));
		editScheduleBtn.setTextColor(getResources().getColor(R.color.blue_title));
		editScheduleBtn.setBackgroundResource(R.drawable.list_item_selector);
		editScheduleBtn.setVisibility(View.VISIBLE);
		editScheduleBtn.setOnClickListener(this);
	}
	
	public void updateList() {
		if (getActivity() == null || getView() == null) return;
		clearAddList();
		if (schOverviewAdapter == null) return;
		schOverviewAdapter.notifyDataSetChanged();
	}
	
	private void clearAddList() {
		if (!lstSchedulers.isEmpty()) {
			lstSchedulers.clear();
		}
		
		lstSchedulers.addAll(((SchedulerActivity) getActivity()).getSchedulerList());
	}
	
	private void setVisibilityAddSchedule() {
		if (getString(R.string.edit).equals(editScheduleBtn.getText().toString())) {
			addScheduleTV.setVisibility(View.GONE);
			addScheduleSeparator.setVisibility(View.GONE);
		} else {
			addScheduleTV.setVisibility(View.VISIBLE);
			addScheduleSeparator.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		SchedulerActivity activity = (SchedulerActivity)getActivity();
		if (activity == null) return;
		switch(v.getId())
		{
			case R.id.schedule_add_tv:
				activity.setSchedulerType(SCHEDULE_TYPE.ADD);
				activity.initializeDayAndFanspeedTime();
				activity.showAddSchedulerFragment();
				selectedItems.clear();
				break;
			case R.id.scheduler_back_img:
				activity.finish();
				break;
			case R.id.scheduler_save_btn:
				if (getString(R.string.edit).equals(editScheduleBtn.getText().toString())) {
					edit = true;
					editScheduleBtn.setText(getString(R.string.done));
					addScheduleTV.setVisibility(View.VISIBLE);
					addScheduleSeparator.setVisibility(View.VISIBLE);
				} else {
					edit = false;
					editScheduleBtn.setText(getString(R.string.edit));
					addScheduleTV.setVisibility(View.GONE);
					addScheduleSeparator.setVisibility(View.GONE);
				}
				selectedItems.clear();
				if (schOverviewAdapter != null) setListData();;
				break;
			default:
				break;
		}
	}

	@Override
	public void onDeleteSchedule(int position, HashMap<Integer, Boolean> selectedItems) {
		this.selectedItems = selectedItems;
		((SchedulerActivity) getActivity()).deleteScheduler(position);
	}

	@Override
	public void onEditSchedule(int position) {
		((SchedulerActivity)getActivity()).onEditScheduler(position);
	}
}
