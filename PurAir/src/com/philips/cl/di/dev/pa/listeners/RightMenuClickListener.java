package com.philips.cl.di.dev.pa.listeners;

import com.philips.cl.di.dev.pa.R;

import android.app.Dialog;
import android.content.Context;
import android.opengl.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RightMenuClickListener implements OnClickListener {
	
	private Context context;
	
	private Button fanSpeedManualButton, timerButton;
	
	public RightMenuClickListener(Context context) {
		this.context = context;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.control_panel, null);
		
		//TODO : Change button text on click.
		/**Unable to change text of the buttons
		fanSpeedManualButton = (Button) layout.findViewById(R.id.fan_speed_manual_btn);
		fanSpeedManualButton.setText("TESTING");
		timerButton = (Button) layout.findViewById(R.id.set_timer_btn);
		timerButton.setText("TIMER");*/
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.connect:
			Toast.makeText(context, "Connect", Toast.LENGTH_LONG).show();
			break;
		case R.id.fan_speed_manual_btn:
			Toast.makeText(context, "fan_speed_manual_btn", Toast.LENGTH_LONG).show();
			showFanSpeedDialog();
			break;
		case R.id.fan_speed_auto_btn:
			Toast.makeText(context, "fan_speed_auto_btn", Toast.LENGTH_LONG).show();
			break;
		case R.id.set_timer_btn:
			showTimerDialog();
			Toast.makeText(context, "set_timer_btn", Toast.LENGTH_LONG).show();
			break;
		case R.id.child_lock_btn:
			Toast.makeText(context, "child_lock_btn", Toast.LENGTH_LONG).show();
			break;
		case R.id.indicator_light_btn:
			Toast.makeText(context, "indicator_light_btn", Toast.LENGTH_LONG).show();
			break;
//		case R.id.power_on_off_btn:
//			Toast.makeText(context, "power_on_off_btn", Toast.LENGTH_LONG).show();
//			break;
			
		//Fan speed buttons
		case R.id.btn_fan_speed_turbo:
			Toast.makeText(context, "btn_fan_speed_turbo", Toast.LENGTH_LONG).show();
			fanSpeedDialog.dismiss();
			break;
		case R.id.btn_fan_speed_3:
			Toast.makeText(context, "btn_fan_speed_3", Toast.LENGTH_LONG).show();
			fanSpeedDialog.dismiss();
			break;
		case R.id.btn_fan_speed_2:
			Toast.makeText(context, "btn_fan_speed_2", Toast.LENGTH_LONG).show();
			fanSpeedDialog.dismiss();
			break;
		case R.id.btn_fan_speed_1:
			Toast.makeText(context, "btn_fan_speed_1", Toast.LENGTH_LONG).show();
			fanSpeedDialog.dismiss();
			break;
		case R.id.btn_fan_speed_silent:
			Toast.makeText(context, "btn_fan_speed_silent", Toast.LENGTH_LONG).show();
			fanSpeedDialog.dismiss();
			break;
			
		// Timer control buttons
		case R.id.btn_eighthour:
			Toast.makeText(context, "btn_eighthour", Toast.LENGTH_LONG).show();
			timerDialog.dismiss();
			break;
		case R.id.btn_fourhour:
			Toast.makeText(context, "btn_fourhour", Toast.LENGTH_LONG).show();
			timerDialog.dismiss();
			break;
		case R.id.btn_onehour:
			Toast.makeText(context, "btn_onehour", Toast.LENGTH_LONG).show();
			timerDialog.dismiss();
			break;
		case R.id.btn_timeroff:
			Toast.makeText(context, "btn_timeroff", Toast.LENGTH_LONG).show();
			timerDialog.dismiss();
			break;

		default:
			break;
		}
	}
	
	private Dialog fanSpeedDialog;
	private Dialog timerDialog;
	
	private void showFanSpeedDialog() {
		if(fanSpeedDialog == null) {
			fanSpeedDialog = new Dialog(context);
			fanSpeedDialog.getWindow().setBackgroundDrawableResource(R.color.whitesmoke);
			fanSpeedDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			fanSpeedDialog.getWindow().setGravity(Gravity.CENTER);
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fan_speed_dialog, null);
			fanSpeedDialog.setContentView(layout);
			
			setAllButtonListener(layout);
			
		}
		
		fanSpeedDialog.show();
	}
	
	private void showTimerDialog() {

		if(timerDialog == null) {
			timerDialog = new Dialog(context);
			timerDialog.getWindow().setBackgroundDrawableResource(R.color.whitesmoke);
			timerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			timerDialog.getWindow().setGravity(Gravity.CENTER);
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_timer, null);
			timerDialog.setContentView(layout);
			
			setAllButtonListener(layout);
			
		}
		
		timerDialog.show();
	}
	
	public void setAllButtonListener(ViewGroup viewGroup) {

	    View v;
	    for (int i = 0; i < viewGroup.getChildCount(); i++) {
	        v = viewGroup.getChildAt(i);
	        if (v instanceof ViewGroup) {
	            setAllButtonListener((ViewGroup) v);
	        } else if (v instanceof Button) {
	            ((Button) v).setOnClickListener(this);
	        }
	    }
	}
	
}
