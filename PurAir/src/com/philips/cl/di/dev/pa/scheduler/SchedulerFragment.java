package com.philips.cl.di.dev.pa.scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.util.ALog;

public class SchedulerFragment extends BaseFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "SchedulerFragment::onCreateView() method enter");
		View view = inflater.inflate(R.layout.scheduler_container, container, false);
		initViews();
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.OVERVIEW_EVENT);
		ALog.i(ALog.SCHEDULER, "SchedulerFragment::onCreateView() method exit");
		return view;
	}

	private void initViews() {
		ALog.i(ALog.SCHEDULER, "SchedulerFragment::initViews() method called");
	}
}

