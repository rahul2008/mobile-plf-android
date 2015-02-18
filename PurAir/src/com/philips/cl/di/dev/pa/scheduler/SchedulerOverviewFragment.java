package com.philips.cl.di.dev.pa.scheduler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.FontTextView;

@SuppressLint("UseSparseArrays")
public class SchedulerOverviewFragment extends BaseFragment implements OnClickListener, SchedulerEditListener {

    private FontTextView editTxt;
	private ImageView add;
	private ListView lstView;
	private SchedulerOverViewAdapter schOverviewAdapter;
	private List<SchedulePortInfo> lstSchedulers;
	private boolean edit;
	private HashMap<Integer, Boolean> selectedItems;
	
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
	
	private void setListData() {
		schOverviewAdapter = new SchedulerOverViewAdapter(getActivity(),
				R.layout.simple_list_item, lstSchedulers, selectedItems, edit, this);
		lstView.setAdapter(schOverviewAdapter);
	}
	
	private void initViews(View view) {
		((ImageView) view.findViewById(R.id.scheduler_back_img)).setOnClickListener(this);
		FontTextView headingTV = (FontTextView) view.findViewById(R.id.scheduler_heading_tv);
		headingTV.setText(getString(R.string.scheduler));
		editTxt = (FontTextView) view.findViewById(R.id.sch_edit);
		editTxt.setOnClickListener(this);
		add = (ImageView) view.findViewById(R.id.sch_add);
		add.setOnClickListener(this);
		lstView = (ListView) view.findViewById(R.id.event_scheduler_listview);
		lstSchedulers = new ArrayList<SchedulePortInfo>();
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::initViews() method exit");
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

	@Override
	public void onClick(View v) {
		SchedulerActivity activity = (SchedulerActivity)getActivity();
		if (activity == null) return;
		switch(v.getId())
		{
			case R.id.sch_edit:
				if (editTxt.getText().equals(getString(R.string.edit))) {
					edit = true;
					editTxt.setText(getString(R.string.done));
				} else {
					edit = false;
					editTxt.setText(getString(R.string.edit));
				}
				if (schOverviewAdapter == null) return;
				setListData();
				break;
			case R.id.sch_add:
				activity.setSchedulerType(SCHEDULE_TYPE.ADD);
				activity.initializeDayAndFanspeed();
				try {
					DialogFragment newFragment = new TimePickerFragment();
					newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}	
				break;
			case R.id.scheduler_back_img:
				activity.finish();
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
