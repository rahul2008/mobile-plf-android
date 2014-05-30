package com.philips.cl.di.dev.pa.scheduler;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class SchedulerOverviewFragment extends BaseFragment implements OnClickListener {
	
	private FontTextView edit;
	private ImageView add;
	private ListView lstView;
	private SchedulerOverViewAdapter schOverviewAdapter;
	private List<SchedulePortInfo> lstSchedulers;
	private boolean isEdit;
	private ArrayList<Boolean> isEditList;
	
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
		schOverviewAdapter = new SchedulerOverViewAdapter(getActivity(), R.layout.scheduler_overview_item, lstSchedulers);
		lstView.setAdapter(schOverviewAdapter);
	}
	
	
	private void initViews(View view) {
		edit = (FontTextView) view.findViewById(R.id.sch_edit);
		edit.setOnClickListener(this);
		add = (ImageView) view.findViewById(R.id.sch_add);
		add.setOnClickListener(this);
		lstView = (ListView) view.findViewById(R.id.event_scheduler_listview);
		lstSchedulers = new ArrayList<SchedulePortInfo>();
		isEditList = new ArrayList<Boolean>();
		ALog.i(ALog.SCHEDULER, "SchedulerOverview::initViews() method exit");
	}
	
	private class SchedulerOverViewAdapter extends ArrayAdapter<SchedulePortInfo> {
		public SchedulerOverViewAdapter(Context context, int resource, List<SchedulePortInfo> objects) {
			super(context, resource, objects);
			ALog.i(ALog.SCHEDULER, "RepeatFragment-DaysAdapter () method constructor enter " + objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View view = inflater.inflate(R.layout.scheduler_overview_item, null);
			final FontTextView name = (FontTextView) view.findViewById(R.id.sch_overview_name);
			final RelativeLayout mainLayout = (RelativeLayout) view.findViewById(R.id.ll_scheduler_overview);
			final ImageView selDelete = (ImageView) view.findViewById(R.id.sch_overview_delete);
			final ImageView rhtArr = (ImageView) view.findViewById(R.id.sch_overview_rightarr);
			final FontTextView rhtArrtxt = (FontTextView) view.findViewById(R.id.sch_overview_rhtarrtext);
			
			final int tempPosition = position;
			
			if (lstSchedulers.get(position).getName() != null) {
				name.setText(lstSchedulers.get(position).getName());
			}
			if (isEdit) {
				selDelete.setVisibility(View.VISIBLE);
				rhtArr.setVisibility(View.VISIBLE);
				if (isEditList.get(tempPosition)) {
					selDelete.setImageResource(R.drawable.delete_t2b);
					rhtArr.setImageResource(R.drawable.delete);
					rhtArrtxt.setVisibility(View.VISIBLE);
				}
			} else {
				selDelete.setVisibility(View.GONE);
				rhtArr.setImageResource(R.drawable.about_air_quality_goto);
			}
			
			selDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					isEditList.set(tempPosition, !isEditList.get(tempPosition));
					schOverviewAdapter.notifyDataSetChanged();
				}
			});
			
			rhtArr.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (rhtArrtxt.getVisibility() == View.VISIBLE) {
						lstSchedulers.remove(tempPosition);
						isEditList.remove(tempPosition);
						schOverviewAdapter.notifyDataSetChanged();
						((SchedulerActivity) getActivity()).deleteScheduler(tempPosition);
					} 
				}
			});
			
			mainLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					((SchedulerActivity)getActivity()).onEditScheduler(tempPosition);
				}
			});
			return view;
		}
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
			isEditList.add(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
			case R.id.sch_edit:
				
				if (edit.getText().equals(getString(R.string.edit))) {
					isEdit = true;
					edit.setText(getString(R.string.done));
				} else {
					isEdit = false;
					edit.setText(getString(R.string.edit));
				}
				if (schOverviewAdapter == null) return;
				schOverviewAdapter.notifyDataSetChanged();
				break;
			case R.id.sch_add:
				((SchedulerActivity)getActivity()).setSchedulerType(SCHEDULE_TYPE.ADD);
				((SchedulerActivity)getActivity()).initializeDayAndFanspeed();
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
				break;
			default:
				break;
		}
		
	}
}
