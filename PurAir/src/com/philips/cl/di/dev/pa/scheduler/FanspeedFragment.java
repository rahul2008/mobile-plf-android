package com.philips.cl.di.dev.pa.scheduler;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class FanspeedFragment extends BaseFragment {
	String sSelectedDays;
	FontTextView tvAuto = null;
	FontTextView tvSilent = null;
	FontTextView tvOne = null;
	FontTextView tvTwo = null;
	FontTextView tvThree = null;
	FontTextView tvTurbo = null;
	ImageView ivDiv1 = null;
	ImageView ivDiv2 = null;
	ImageView ivDiv3 = null;
	ImageView ivDiv4 = null;
	ImageView ivDiv5 = null;
	ImageView ivDiv6 = null;
	boolean bIsAutoSelected = false;
	boolean bIsSilentSelected = false;
	boolean bIsOneSelected = false;
	boolean bIsTwoSelected = false;
	boolean bIsThreeSelected = false;
	boolean bIsTurboSelected = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ALog.i("Scheduler", "FanspeedFragment::onCreateView method beginning is called ");
		View view = inflater.inflate(R.layout.fanspeed_scheduler, null);
		initViews(view);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.FAN_SPEED);
		ALog.i("Scheduler", "FanspeedFragment::onCreateView method ending is called ");
		return view;
	}

	private void initViews(View view) {
		ALog.i("Scheduler", "FanspeedFragment::initViews() method beginning is called ");
		tvAuto = (FontTextView) view.findViewById(R.id.auto);
		tvSilent = (FontTextView) view.findViewById(R.id.silent);
		tvOne = (FontTextView) view.findViewById(R.id.one);
		tvTwo = (FontTextView) view.findViewById(R.id.two);
		tvThree = (FontTextView) view.findViewById(R.id.three);
		tvTurbo = (FontTextView) view.findViewById(R.id.turbo);
		ivDiv1 = (ImageView) view.findViewById(R.id.divider1);
		ivDiv2 = (ImageView) view.findViewById(R.id.divider2);
		ivDiv3 = (ImageView) view.findViewById(R.id.divider3);
		ivDiv4 = (ImageView) view.findViewById(R.id.divider4);
		ivDiv5 = (ImageView) view.findViewById(R.id.divider5);
		ivDiv6 = (ImageView) view.findViewById(R.id.divider6);
		
		tvAuto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectAuto(v, tvAuto, ivDiv1);
				ALog.i("Scheduler", "Bundle is empty");
				}
		});
		
		tvSilent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectSilent(v, tvSilent, ivDiv2);
			}
		});
		
		tvOne.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectOne(v, tvOne, ivDiv3);
			}
		});
		
		tvTwo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectTwo(v, tvTwo, ivDiv4);
			}
		});
		
		tvThree.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectThree(v, tvThree, ivDiv5);
			}
		});
		
		tvTurbo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectTurbo(v, tvTurbo, ivDiv6);
			}
		});
		ALog.i("Scheduler", "FanspeedFragment::initViews() method ending is called ");
	}
	
	private void selectAuto(View view, FontTextView tvAuto, ImageView ivDiv1) {
		ALog.i("Scheduler", "FanspeedFragment::selectAuto() method beginning is called ");
		if (bIsAutoSelected == false) {
			tvAuto.setTextColor(Color.WHITE);	
			tvAuto.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			//ivDiv1.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv1.setBackgroundColor(Color.WHITE);
			((SchedulerActivity)getActivity()).dispatchInformations2("Auto");
			bIsAutoSelected = true;
			toggleSelectionForOthers("auto");
		} 
		ALog.i("Scheduler", "FanspeedFragment::selectAuto() method ending is called ");
	}
	
	private void selectSilent(View view, FontTextView tvSilent, ImageView ivDiv2) {
		ALog.i("Scheduler", "FanspeedFragment::selectSilent() method beginning is called ");
		if (bIsSilentSelected == false) {
			ALog.i("Scheduler", "Monday is pressed ");
			tvSilent.setTextColor(Color.WHITE);	
			tvSilent.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			//ivDiv2.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv2.setBackgroundColor(Color.WHITE);
			((SchedulerActivity)getActivity()).dispatchInformations2("Silent");
			bIsSilentSelected = true;
			toggleSelectionForOthers("silent");
		} 
		ALog.i("Scheduler", "FanspeedFragment::selectSilent() method ending is called ");
	}
	
	private void selectOne(View view, FontTextView tvOne, ImageView ivDiv3) {
		ALog.i("Scheduler", "FanspeedFragment::selectOne() method beginning is called ");
		if (bIsOneSelected == false) {
			ALog.i("Scheduler", "Tuesday is pressed ");
			tvOne.setTextColor(Color.WHITE);	
			tvOne.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			//ivDiv3.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv3.setBackgroundColor(Color.WHITE);
			((SchedulerActivity)getActivity()).dispatchInformations2("1");
			bIsOneSelected = true;
			toggleSelectionForOthers("1");
		} 
		ALog.i("Scheduler", "FanspeedFragment::selectOne() method ending is called ");
	}
	
	private void selectTwo(View view, FontTextView tvTwo, ImageView ivDiv4) {
		ALog.i("Scheduler", "FanspeedFragment::selectTwo() method beginning is called ");
		if (bIsTwoSelected == false) {
			ALog.i("Scheduler", "Tuesday is pressed ");
			tvTwo.setTextColor(Color.WHITE);	
			tvTwo.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			//ivDiv4.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv4.setBackgroundColor(Color.WHITE);
			((SchedulerActivity)getActivity()).dispatchInformations2("2");
			bIsTwoSelected = true;
			toggleSelectionForOthers("2");
		} 
		ALog.i("Scheduler", "FanspeedFragment::selectTwo() method ending is called ");
	}
	
	private void selectThree(View view, FontTextView tvThree, ImageView ivDiv5) {
		ALog.i("Scheduler", "FanspeedFragment::selectThree() method beginning is called ");
		if (bIsThreeSelected == false) {
			ALog.i("Scheduler", "Tuesday is pressed ");
			tvThree.setTextColor(Color.WHITE);	
			tvThree.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			//ivDiv5.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv5.setBackgroundColor(Color.WHITE);
			((SchedulerActivity)getActivity()).dispatchInformations2("3");
			bIsThreeSelected = true;
			toggleSelectionForOthers("3");
		} 
		ALog.i("Scheduler", "FanspeedFragment::selectThree() method ending is called ");
	}
	
	private void selectTurbo(View view, FontTextView tvTurbo, ImageView ivDiv6) {
		ALog.i("Scheduler", "FanspeedFragment::selectTurbo() method beginning is called ");
		if (bIsTurboSelected == false) {
			ALog.i("Scheduler", "Tuesday is pressed ");
			tvTurbo.setTextColor(Color.WHITE);	
			tvTurbo.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			//ivDiv6.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv6.setBackgroundColor(Color.WHITE);
			((SchedulerActivity)getActivity()).dispatchInformations2("Turbo");
			bIsTurboSelected = true;
			toggleSelectionForOthers("turbo");
		}
		ALog.i("Scheduler", "FanspeedFragment::selectTurbo() method ending is called ");
	}
	
	private void toggleSelectionForOthers(String fanspeed) {
		ALog.i("Scheduler", "FanspeedFragment::toggleSelectionForOthers() method beginning is called ");
		if(!fanspeed.equals("auto")) {
			tvAuto.setTextColor(Color.BLACK);
			tvAuto.setBackgroundColor(Color.WHITE);
			sSelectedDays = sSelectedDays + "Auto";
			bIsAutoSelected = false;
			
			if(fanspeed.equals("silent"))
				ivDiv1.setBackgroundColor(Color.WHITE);
			else
				ivDiv1.setBackgroundColor(Color.BLACK);
		}
		
		if(!fanspeed.equals("silent")) {
			tvSilent.setTextColor(Color.BLACK);
			tvSilent.setBackgroundColor(Color.WHITE);
			bIsSilentSelected = false;
			
			if(fanspeed.equals("1"))
				ivDiv2.setBackgroundColor(Color.WHITE);
			else
				ivDiv2.setBackgroundColor(Color.BLACK);
		} 
		
		if(!fanspeed.equals("1")) {
			tvOne.setTextColor(Color.BLACK);
			tvOne.setBackgroundColor(Color.WHITE);
			bIsOneSelected = false;
			
			if(fanspeed.equals("2"))
				ivDiv3.setBackgroundColor(Color.WHITE);
			else
				ivDiv3.setBackgroundColor(Color.BLACK);
		} 
		
		if(!fanspeed.equals("2")) {
			tvTwo.setTextColor(Color.BLACK);
			tvTwo.setBackgroundColor(Color.WHITE);
			bIsTwoSelected = false;
			
			if(fanspeed.equals("3"))
				ivDiv4.setBackgroundColor(Color.WHITE);
			else
				ivDiv4.setBackgroundColor(Color.BLACK);
		} 
		
		if(!fanspeed.equals("3")) {
			tvThree.setTextColor(Color.BLACK);
			tvThree.setBackgroundColor(Color.WHITE);
			bIsThreeSelected = false;
			
			if(fanspeed.equals("turbo"))
				ivDiv5.setBackgroundColor(Color.WHITE);
			else
				ivDiv5.setBackgroundColor(Color.BLACK);
		} 
		
		if(!fanspeed.equals("turbo")) {
			tvTurbo.setTextColor(Color.BLACK);
			tvTurbo.setBackgroundColor(Color.WHITE);
			ivDiv6.setBackgroundColor(Color.BLACK);
			bIsTurboSelected = false;
		} 
		ALog.i("Scheduler", "FanspeedFragment::toggleSelectionForOthers() method ending is called ");
	}
}
