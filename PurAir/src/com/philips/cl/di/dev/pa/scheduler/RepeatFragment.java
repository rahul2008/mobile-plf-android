package com.philips.cl.di.dev.pa.scheduler;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SchedulerID;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.GraphConst;
import com.philips.cl.di.dev.pa.view.FontTextView;

public class RepeatFragment extends BaseFragment {
	String sSelectedDays;
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
	CheckBox chkBox1 = null;
	CheckBox chkBox2 = null;
	CheckBox chkBox3 = null;
	CheckBox chkBox4 = null;
	CheckBox chkBox5 = null;
	CheckBox chkBox6 = null;
	CheckBox chkBox7 = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::onCreateView() method enter");
		View view = inflater.inflate(R.layout.repeat_scheduler, null);
		initViews(view);
		((SchedulerActivity) getActivity()).setActionBar(SchedulerID.REPEAT);
		ALog.i(ALog.SCHEDULER, "RepeatFragment::onCreateView() method exit");
		return view;
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
		chkBox1 = (CheckBox) view.findViewById(R.id.checkBox1);
		chkBox2 = (CheckBox) view.findViewById(R.id.checkBox2);
		chkBox3 = (CheckBox) view.findViewById(R.id.checkBox3);
		chkBox4 = (CheckBox) view.findViewById(R.id.checkBox4);
		chkBox5 = (CheckBox) view.findViewById(R.id.checkBox5);
		chkBox6 = (CheckBox) view.findViewById(R.id.checkBox6);
		chkBox7 = (CheckBox) view.findViewById(R.id.checkBox7);
		
		tvSun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickSunday(v, tvSun, ivDiv1, chkBox1);
				}
		});
		
		chkBox1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickSunday(v, tvSun, ivDiv1, chkBox1);
			}
		});	
		
		tvMon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickMonday(v, tvMon, ivDiv2, chkBox2);
			}
		});
		
		chkBox2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickMonday(v, tvMon, ivDiv2, chkBox2);
			}
		});
		
		tvTue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickTuesday(v, tvTue, ivDiv3, chkBox3);
			}
		});
		
		chkBox3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickTuesday(v, tvTue, ivDiv3, chkBox3);
			}
		});
		
		tvWed.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickWednesday(v, tvWed, ivDiv4, chkBox4);
			}
		});
		
		chkBox4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickWednesday(v, tvWed, ivDiv4, chkBox4);
			}
		});
		
		tvThu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickThursday(v, tvThu, ivDiv5, chkBox5);
			}
		});
		
		chkBox5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickThursday(v, tvThu, ivDiv5, chkBox5);
			}
		});
		
		tvFri.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickFriday(v, tvFri, ivDiv6, chkBox6);
			}
		});
		
		chkBox6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickFriday(v, tvFri, ivDiv6, chkBox6);
			}
		});
		
		tvSat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickSaturday(v, tvSat, chkBox7);
			}
		});
		
		chkBox7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setClickSaturday(v, tvSat, chkBox7);
			}
		});
		ALog.i(ALog.SCHEDULER, "RepeatFragment::initViews method exit");
	}
	
	private void setClickSunday(View view, FontTextView tvSun, ImageView ivDiv1, CheckBox chkBox1) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickSunday method enter");
		if (chkBox1.isChecked() == false) {
			ALog.i(ALog.SCHEDULER, "Sunday is clicked");
			tvSun.setTextColor(Color.WHITE);	
			tvSun.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv1.setBackgroundColor(Color.WHITE);
			chkBox1.setVisibility(0);
			chkBox1.setChecked(true);
			chkBox1.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvSun.setTextColor(Color.BLACK);
			tvSun.setBackgroundColor(Color.WHITE);
			chkBox1.setVisibility(8);
			chkBox1.setChecked(false);
			ivDiv1.setBackgroundColor(Color.BLACK);
			chkBox1.setBackgroundColor(Color.WHITE);
			sSelectedDays = sSelectedDays + "Sunday";
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickSunday method exit");
	}
	
	private void setClickMonday(View view, FontTextView tvMon, ImageView ivDiv2, CheckBox chkBox2) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickMonday() method enter");
		if (chkBox2.isChecked() == false) {
			ALog.i(ALog.SCHEDULER, "Monday is clicked");
			tvMon.setTextColor(Color.WHITE);	
			tvMon.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv1.setBackgroundColor(Color.WHITE);
			ivDiv2.setBackgroundColor(Color.WHITE);
			chkBox2.setVisibility(0);
			chkBox2.setChecked(true);
			chkBox2.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvMon.setTextColor(Color.BLACK);
			tvMon.setBackgroundColor(Color.WHITE);
			chkBox2.setVisibility(8);
			chkBox2.setChecked(false);
			chkBox2.setBackgroundColor(Color.WHITE);
			ivDiv1.setBackgroundColor(Color.BLACK);
			ivDiv2.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickMonday() method exit");
	}
	
	private void setClickTuesday(View view, FontTextView tvTue, ImageView ivDiv3, CheckBox chkBox3) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickTuesday() method enter");
		if (chkBox3.isChecked() == false) {
			ALog.i(ALog.SCHEDULER, "Tuesday is clicked");
			tvTue.setTextColor(Color.WHITE);	
			tvTue.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv2.setBackgroundColor(Color.WHITE);
			ivDiv3.setBackgroundColor(Color.WHITE);
			chkBox3.setVisibility(0);
			chkBox3.setChecked(true);
			chkBox3.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvTue.setTextColor(Color.BLACK);
			tvTue.setBackgroundColor(Color.WHITE);
			chkBox3.setVisibility(8);
			chkBox3.setChecked(false);
			chkBox3.setBackgroundColor(Color.WHITE);
			ivDiv2.setBackgroundColor(Color.BLACK);
			ivDiv3.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickTuesday() method exit");
	}
	
	private void setClickWednesday(View view, FontTextView tvWed, ImageView ivDiv4, CheckBox chkBox4) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickWednesday() method enter");
		if (chkBox4.isChecked() == false) {
			ALog.i(ALog.SCHEDULER, "Wednesday is clicked");
			tvWed.setTextColor(Color.WHITE);	
			tvWed.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv3.setBackgroundColor(Color.WHITE);
			ivDiv4.setBackgroundColor(Color.WHITE);
			chkBox4.setVisibility(0);
			chkBox4.setChecked(true);
			chkBox4.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvWed.setTextColor(Color.BLACK);
			tvWed.setBackgroundColor(Color.WHITE);
			chkBox4.setVisibility(8);
			chkBox4.setChecked(false);
			chkBox4.setBackgroundColor(Color.WHITE);
			ivDiv3.setBackgroundColor(Color.BLACK);
			ivDiv4.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickWednesday() method exit");
	}
	
	private void setClickThursday(View view, FontTextView tvThu, ImageView ivDiv5, CheckBox chkBox5) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickThursday() method enter");
		if (chkBox5.isChecked() == false) {
			ALog.i(ALog.SCHEDULER, "Thursday is clicked");
			tvThu.setTextColor(Color.WHITE);	
			tvThu.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv4.setBackgroundColor(Color.WHITE);
			ivDiv5.setBackgroundColor(Color.WHITE);
			chkBox5.setVisibility(0);
			chkBox5.setChecked(true);
			chkBox5.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvThu.setTextColor(Color.BLACK);
			tvThu.setBackgroundColor(Color.WHITE);
			chkBox5.setVisibility(8);
			chkBox5.setChecked(false);
			chkBox5.setBackgroundColor(Color.WHITE);
			ivDiv4.setBackgroundColor(Color.BLACK);
			ivDiv5.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickThursday() method exit");
	}
	
	private void setClickFriday(View view, FontTextView tvFri, ImageView ivDiv6, CheckBox chkBox6) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickFriday() method enter");
		if (chkBox6.isChecked() == false) {
			ALog.i(ALog.SCHEDULER, "Friday is clicked");
			tvFri.setTextColor(Color.WHITE);	
			tvFri.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv5.setBackgroundColor(Color.WHITE);
			ivDiv6.setBackgroundColor(Color.WHITE);
			chkBox6.setVisibility(0);
			chkBox6.setChecked(true);
			chkBox6.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvFri.setTextColor(Color.BLACK);
			tvFri.setBackgroundColor(Color.WHITE);
			chkBox6.setVisibility(8);
			chkBox6.setChecked(false);
			chkBox6.setBackgroundColor(Color.WHITE);
			ivDiv5.setBackgroundColor(Color.BLACK);
			ivDiv6.setBackgroundColor(Color.BLACK);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickFriday() method exit");
	}
	
	private void setClickSaturday(View view, FontTextView tvSat, CheckBox chkBox7) {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickSaturday() method enter");
		if (chkBox7.isChecked() == false) {
			ALog.i("Scheduler", "Saturday is pressed ");
			tvSat.setTextColor(Color.WHITE);	
			tvSat.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			ivDiv6.setBackgroundColor(Color.WHITE);
			chkBox7.setVisibility(0);
			chkBox7.setChecked(true);
			chkBox7.setBackgroundColor(GraphConst.COLOR_DODLE_BLUE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		} else {
			tvSat.setTextColor(Color.BLACK);
			tvSat.setBackgroundColor(Color.WHITE);
			chkBox7.setVisibility(8);
			chkBox7.setChecked(false);
			chkBox7.setBackgroundColor(Color.WHITE);
			((SchedulerActivity)getActivity()).dispatchInformations(setDaysString());
		}
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setClickSaturday() method exit");
	}
	
	private String setDaysString() {
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setDaysString() method enter");
		sSelectedDays = "";
		
		if (chkBox1.isChecked() == true) {
			sSelectedDays = sSelectedDays + "0";
		}
		if (chkBox2.isChecked() == true) {
			sSelectedDays = sSelectedDays + "1";
		}
		if (chkBox3.isChecked() == true) {
			sSelectedDays = sSelectedDays + "2";
		}
		if (chkBox4.isChecked() == true) {
			sSelectedDays = sSelectedDays + "3";
		}
		if (chkBox5.isChecked() == true) {
			sSelectedDays = sSelectedDays + "4";
		}
		if (chkBox6.isChecked() == true) {
			sSelectedDays = sSelectedDays + "5";
		}
		if (chkBox7.isChecked() == true) {
			sSelectedDays = sSelectedDays + "6";
		}
		
		ALog.i(ALog.SCHEDULER, "RepeatFragment::setDaysString() method exit");
		return sSelectedDays;
	}
}
