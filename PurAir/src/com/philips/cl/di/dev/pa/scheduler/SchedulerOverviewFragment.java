package com.philips.cl.di.dev.pa.scheduler;
import java.util.ArrayList;
import java.util.List;

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
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerOverviewFragment extends BaseFragment implements OnClickListener, SchedulerEditListener {
	
	private FontTextView editTxt;
	private ImageView add;
	private ListView lstView;
	private SchedulerOverViewAdapter schOverviewAdapter;
	private List<SchedulePortInfo> lstSchedulers;
	private boolean edit;
	private ArrayList<Boolean> editList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::onCreateView() method enter");
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.OVERVIEW_EVENT);
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
		addSelectedEdit();
		schOverviewAdapter = new SchedulerOverViewAdapter(getActivity(),
				R.layout.simple_list_item, lstSchedulers, editList, edit, this);
		lstView.setAdapter(schOverviewAdapter);
	}
	
	
	private void initViews(View view) {
		editTxt = (FontTextView) view.findViewById(R.id.sch_edit);
		editTxt.setOnClickListener(this);
		add = (ImageView) view.findViewById(R.id.sch_add);
		add.setOnClickListener(this);
		lstView = (ListView) view.findViewById(R.id.event_scheduler_listview);
		lstSchedulers = new ArrayList<SchedulePortInfo>();
		editList = new ArrayList<Boolean>();
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::initViews() method exit");
	}
	
	public void updateList() {
		if (getActivity() == null || getView() == null) return;
		clearAddList();
		addSelectedEdit();
		if (schOverviewAdapter == null) return;
		schOverviewAdapter.notifyDataSetChanged();
	}
	
	private void clearAddList() {
		if (!lstSchedulers.isEmpty()) {
			lstSchedulers.clear();
		}
		
		lstSchedulers.addAll(((SchedulerActivity) getActivity()).getSchedulerList());
	}
	
	private void addSelectedEdit() {
		for (int i = 0; i < lstSchedulers.size(); i++) {
			editList.add(false);
		}
	}

	@Override
	public void onClick(View v) {
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
				schOverviewAdapter = new SchedulerOverViewAdapter(getActivity(), 
						R.layout.simple_list_item, lstSchedulers, editList, edit, this);
				lstView.setAdapter(schOverviewAdapter);
//				schOverviewAdapter.notifyDataSetChanged();
				break;
			case R.id.sch_add:
				((SchedulerActivity)getActivity()).setSchedulerType(SCHEDULE_TYPE.ADD);
				((SchedulerActivity)getActivity()).initializeDayAndFanspeed();
				try {
					DialogFragment newFragment = new TimePickerFragment();
					newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}	
				break;
			default:
				break;
		}
	}

	@Override
	public void onDeleteSchedule(int position) {
		((SchedulerActivity) getActivity()).deleteScheduler(position);
	}

	@Override
	public void onEditSchedule(int position) {
		((SchedulerActivity)getActivity()).onEditScheduler(position);
	}
}
