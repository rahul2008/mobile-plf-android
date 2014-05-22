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

public class RepeatFragment extends BaseFragment {
	String sSelectedDays;
	String initDays;
	FontTextView tvSun = null;
	FontTextView tvMon = null;
	FontTextView tvTue = null;
	FontTextView tvWed = null;
	FontTextView tvThu = null;
	FontTextView tvFri = null;
	FontTextView tvSat = null;
	ImageView ivDiv1 = null;
	ImageView ivDiv2 = null;
	ImageView ivDiv3 = null;
	ImageView ivDiv4 = null;
	ImageView ivDiv5 = null;
	ImageView ivDiv6 = null;
	ImageView chkBox1 = null;
	ImageView chkBox2 = null;
	ImageView chkBox3 = null;
	ImageView chkBox4 = null;
	ImageView chkBox5 = null;
	ImageView chkBox6 = null;
	ImageView chkBox7 = null;
	boolean bIsSunClicked = false;
	boolean bIsMonClicked = false;
	boolean bIsTueClicked = false;
	boolean bIsWedClicked = false;
	boolean bIsThuClicked = false;
	boolean bIsFriClicked = false;
	boolean bIsSatClicked = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::onCreateView() method enter");
		View view = inflater.inflate(R.layout.repeat_scheduler, null);
		initViews(view);
		
		try {
			initDays = getArguments().getString(SchedulerConstants.DAYS);
			ALog.i(ALog.SCHEDULER, "RepeatFragment::onCreateView() method initDays is " + initDays);
			initSelectedDays(view);
		}
		catch (Exception e) {
			ALog.i(ALog.SCHEDULER, "RepeatFragment::onCreateView() method exception caught while getting arguments");
			initDays = "";
		}
		
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.REPEAT);
		ALog.i(ALog.SCHEDULER, "RepeatFragment::onCreateView() method exit");
		return view;
	}
	
	private void initSelectedDays(View v) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::onCreateView() method initSelectedDays");
		
			if (initDays.contains(SchedulerConstants.SUNDAY)) { 
				setClickSunday(tvSun, ivDiv1, chkBox1);
			} 
			
			if (initDays.contains(SchedulerConstants.MONDAY)) {
				setClickMonday(tvMon, ivDiv2, chkBox2);
			} 
			
			if (initDays.contains(SchedulerConstants.TUESDAY)) {
				setClickTuesday(tvTue, ivDiv3, chkBox3);
			} 
			
			if (initDays.contains(SchedulerConstants.WEDNESDAY)) {
				setClickWednesday(tvWed, ivDiv4, chkBox4);
			} 
			
			if (initDays.contains(SchedulerConstants.THURSDAY)) {
				setClickThursday(tvThu, ivDiv5, chkBox5);
			} 
			
			if (initDays.contains(SchedulerConstants.FRIDAY)) {
				setClickFriday(tvFri, ivDiv6, chkBox6);
			} 
			
			if (initDays.contains(SchedulerConstants.SATURDAY)) {
				setClickSaturday(tvSat, chkBox7);
			}
	}

	private void initViews(View view) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::initViews() method enter");
		tvSun = (FontTextView) view.findViewById(R.id.tvSunday);
		tvMon = (FontTextView) view.findViewById(R.id.tvMonday);
		tvTue = (FontTextView) view.findViewById(R.id.tvTuesday);
		tvWed = (FontTextView) view.findViewById(R.id.tvWednesday);
		tvThu = (FontTextView) view.findViewById(R.id.tvThursday);
		tvFri = (FontTextView) view.findViewById(R.id.tvFriday);
		tvSat = (FontTextView) view.findViewById(R.id.tvSaturday);
		ivDiv1 = (ImageView) view.findViewById(R.id.divider1);
		ivDiv2 = (ImageView) view.findViewById(R.id.divider2);
		ivDiv3 = (ImageView) view.findViewById(R.id.divider3);
		ivDiv4 = (ImageView) view.findViewById(R.id.divider4);
		ivDiv5 = (ImageView) view.findViewById(R.id.divider5);
		ivDiv6 = (ImageView) view.findViewById(R.id.divider6);
		chkBox1 = (ImageView) view.findViewById(R.id.checkBox1);
		chkBox2 = (ImageView) view.findViewById(R.id.checkBox2);
		chkBox3 = (ImageView) view.findViewById(R.id.checkBox3);
		chkBox4 = (ImageView) view.findViewById(R.id.checkBox4);
		chkBox5 = (ImageView) view.findViewById(R.id.checkBox5);
		chkBox6 = (ImageView) view.findViewById(R.id.checkBox6);
		chkBox7 = (ImageView) view.findViewById(R.id.checkBox7);
		
		tvSun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickSunday(tvSun, ivDiv1, chkBox1);
				}
		});
		
		chkBox1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickSunday(tvSun, ivDiv1, chkBox1);
			}
		});	
		
		tvMon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickMonday(tvMon, ivDiv2, chkBox2);
			}
		});
		
		chkBox2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickMonday(tvMon, ivDiv2, chkBox2);
			}
		});
		
		tvTue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickTuesday(tvTue, ivDiv3, chkBox3);
			}
		});
		
		chkBox3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickTuesday(tvTue, ivDiv3, chkBox3);
			}
		});
		
		tvWed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickWednesday(tvWed, ivDiv4, chkBox4);
			}
		});
		
		chkBox4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickWednesday(tvWed, ivDiv4, chkBox4);
			}
		});
		
		tvThu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickThursday(tvThu, ivDiv5, chkBox5);
			}
		});
		
		chkBox5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickThursday(tvThu, ivDiv5, chkBox5);
			}
		});
		
		tvFri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickFriday(tvFri, ivDiv6, chkBox6);
			}
		});
		
		chkBox6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickFriday(tvFri, ivDiv6, chkBox6);
			}
		});
		
		tvSat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickSaturday(tvSat, chkBox7);
			}
		});
		
		chkBox7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickSaturday(tvSat, chkBox7);
			}
		});
		ALog.i(ALog.SCHEDULER, "RepeatFragment::initViews method exit");
	}
	
	private void setClickSunday(FontTextView tvSun, ImageView ivDiv1, ImageView chkBox1) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickSunday method enter");
		if (bIsSunClicked == false) {
			ALog.i(ALog.SCHEDULER, "Sunday is clicked");
			tvSun.setTextColor(Color.WHITE);	
			tvSun.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv1.setBackgroundColor(Color.WHITE);
			chkBox1.setVisibility(0);
			//chkBox1.setEnabled(true);
			bIsSunClicked = true;
			chkBox1.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvSun.setTextColor(Color.BLACK);
			tvSun.setBackgroundColor(Color.WHITE);
			chkBox1.setVisibility(8);
			//chkBox1.setEnabled(false);
			bIsSunClicked = false;
			ivDiv1.setBackgroundColor(Color.BLACK);
			chkBox1.setBackgroundColor(Color.WHITE);
			sSelectedDays = sSelectedDays + "Sunday";
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickSunday method exit");
	}
	
	private void setClickMonday(FontTextView tvMon, ImageView ivDiv2, ImageView chkBox2) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickMonday() method enter");
		if (bIsMonClicked  == false) {
			ALog.i(ALog.SCHEDULER, "Monday is clicked");
			tvMon.setTextColor(Color.WHITE);	
			tvMon.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv1.setBackgroundColor(Color.WHITE);
			ivDiv2.setBackgroundColor(Color.WHITE);
			chkBox2.setVisibility(0);
			//chkBox2.setEnabled(true);
			bIsMonClicked = true;
			chkBox2.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvMon.setTextColor(Color.BLACK);
			tvMon.setBackgroundColor(Color.WHITE);
			chkBox2.setVisibility(8);
			//chkBox2.setEnabled(false);
			bIsMonClicked = false;
			chkBox2.setBackgroundColor(Color.WHITE);
			ivDiv1.setBackgroundColor(Color.BLACK);
			ivDiv2.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickMonday() method exit");
	}
	
	private void setClickTuesday(FontTextView tvTue, ImageView ivDiv3, ImageView chkBox3) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickTuesday() method enter");
		if (bIsTueClicked == false) {
			ALog.i(ALog.SCHEDULER, "Tuesday is clicked");
			tvTue.setTextColor(Color.WHITE);	
			tvTue.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv2.setBackgroundColor(Color.WHITE);
			ivDiv3.setBackgroundColor(Color.WHITE);
			chkBox3.setVisibility(0);
			chkBox3.setEnabled(true);
			bIsTueClicked = true;
			chkBox3.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvTue.setTextColor(Color.BLACK);
			tvTue.setBackgroundColor(Color.WHITE);
			chkBox3.setVisibility(8);
			chkBox3.setEnabled(false);
			bIsTueClicked = false;
			chkBox3.setBackgroundColor(Color.WHITE);
			ivDiv2.setBackgroundColor(Color.BLACK);
			ivDiv3.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickTuesday() method exit");
	}
	
	private void setClickWednesday(FontTextView tvWed, ImageView ivDiv4, ImageView chkBox4) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickWednesday() method enter");
		if (bIsWedClicked == false) {
			ALog.i(ALog.SCHEDULER, "Wednesday is clicked");
			tvWed.setTextColor(Color.WHITE);	
			tvWed.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv3.setBackgroundColor(Color.WHITE);
			ivDiv4.setBackgroundColor(Color.WHITE);
			chkBox4.setVisibility(0);
			chkBox4.setEnabled(true);
			bIsWedClicked = true;
			chkBox4.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvWed.setTextColor(Color.BLACK);
			tvWed.setBackgroundColor(Color.WHITE);
			chkBox4.setVisibility(8);
			chkBox4.setEnabled(false);
			bIsWedClicked = false;
			chkBox4.setBackgroundColor(Color.WHITE);
			ivDiv3.setBackgroundColor(Color.BLACK);
			ivDiv4.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickWednesday() method exit");
	}
	
	private void setClickThursday(FontTextView tvThu, ImageView ivDiv5, ImageView chkBox5) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickThursday() method enter");
		if (bIsThuClicked == false) {
			ALog.i(ALog.SCHEDULER, "Thursday is clicked");
			tvThu.setTextColor(Color.WHITE);	
			tvThu.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv4.setBackgroundColor(Color.WHITE);
			ivDiv5.setBackgroundColor(Color.WHITE);
			chkBox5.setVisibility(0);
			chkBox5.setEnabled(true);
			bIsThuClicked = true;
			chkBox5.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvThu.setTextColor(Color.BLACK);
			tvThu.setBackgroundColor(Color.WHITE);
			chkBox5.setVisibility(8);
			chkBox5.setEnabled(false);
			bIsThuClicked = false;
			chkBox5.setBackgroundColor(Color.WHITE);
			ivDiv4.setBackgroundColor(Color.BLACK);
			ivDiv5.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickThursday() method exit");
	}
	
	private void setClickFriday(FontTextView tvFri, ImageView ivDiv6, ImageView chkBox6) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickFriday() method enter");
		if (bIsFriClicked == false) {
			ALog.i(ALog.SCHEDULER, "Friday is clicked");
			tvFri.setTextColor(Color.WHITE);	
			tvFri.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv5.setBackgroundColor(Color.WHITE);
			ivDiv6.setBackgroundColor(Color.WHITE);
			chkBox6.setVisibility(0);
			chkBox6.setEnabled(true);
			bIsFriClicked = true;
			chkBox6.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvFri.setTextColor(Color.BLACK);
			tvFri.setBackgroundColor(Color.WHITE);
			chkBox6.setVisibility(8);
			chkBox6.setEnabled(false);
			bIsFriClicked = false;
			chkBox6.setBackgroundColor(Color.WHITE);
			ivDiv5.setBackgroundColor(Color.BLACK);
			ivDiv6.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickFriday() method exit");
	}
	
	private void setClickSaturday(FontTextView tvSat, ImageView chkBox7) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickSaturday() method enter");
		if (bIsSatClicked == false) {
			ALog.i("Scheduler", "Saturday is pressed ");
			tvSat.setTextColor(Color.WHITE);	
			tvSat.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv6.setBackgroundColor(Color.WHITE);
			chkBox7.setVisibility(0);
			chkBox7.setEnabled(true);
			bIsSatClicked = true;
			chkBox7.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvSat.setTextColor(Color.BLACK);
			tvSat.setBackgroundColor(Color.WHITE);
			chkBox7.setVisibility(8);
			chkBox7.setEnabled(false);
			bIsSatClicked = false;
			chkBox7.setBackgroundColor(Color.WHITE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickSaturday() method exit");
	}
	
	private String setDaysString() {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setDaysString() method enter");
		sSelectedDays = "";
		
		if (bIsSunClicked  == true) {
			sSelectedDays = sSelectedDays + "0";
		}
		if (bIsMonClicked  == true) {
			sSelectedDays = sSelectedDays + "1";
		}
		if (bIsTueClicked  == true) {
			sSelectedDays = sSelectedDays + "2";
		}
		if (bIsWedClicked  == true) {
			sSelectedDays = sSelectedDays + "3";
		}
		if (bIsThuClicked  == true) {
			sSelectedDays = sSelectedDays + "4";
		}
		if (bIsFriClicked  == true) {
			sSelectedDays = sSelectedDays + "5";
		}
		if (bIsSatClicked  == true) {
			sSelectedDays = sSelectedDays + "6";
		}
		
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setDaysString() method exit");
		return sSelectedDays;
	}
}
