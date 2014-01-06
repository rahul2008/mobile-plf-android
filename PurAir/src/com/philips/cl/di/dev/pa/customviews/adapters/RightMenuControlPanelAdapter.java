package com.philips.cl.di.dev.pa.customviews.adapters;

import java.util.ArrayList;

import com.nineoldandroids.animation.ObjectAnimator;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;
import com.philips.cl.di.dev.pa.listeners.ChildClickListener;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.util.AnimatorConstants;
import com.philips.cl.di.dev.pa.util.Utils;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ToggleButton;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class RightMenuControlPanelAdapter extends BaseExpandableListAdapter implements OnClickListener{
	private static final String TAG = RightMenuControlPanelAdapter.class.getSimpleName();
	
	private Activity activity;
    private ArrayList<Object> childItems;
    private LayoutInflater inflater;
    private ArrayList<String> child;
    private ArrayList<String> parentItems;
    
    private ViewGroup expandableListView;
    
    private ChildClickListener childClickListener;
    
    public RightMenuControlPanelAdapter(ArrayList<String> parents, ArrayList<Object> childern) {
    	this.parentItems = parents;
        this.childItems = childern;
        childClickListener = new ChildClickListener();
        
	}
    
    public void setInflater(LayoutInflater inflater, Activity activity)
    {
        this.inflater = inflater;
        this.activity = activity;
    }
    
	@Override
	public Object getChild(int groupPosition, int childPosition) {
//		Log.i(TAG, "getChild groupPosition " + groupPosition + " childPosition " + childPosition);
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
//		Log.i(TAG, "getChildId groupPosition " + groupPosition + " childPosition " + childPosition);
		return 0;
	}
	
	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Log.i(TAG, "getChildView groupPosition " + groupPosition + " childPosition " + childPosition);
		child = (ArrayList<String>) childItems.get(groupPosition);

        TextView textView = null;
        if(convertView != null) {
        	((ViewGroup) convertView).removeAllViews();
        	convertView = null;
        }
        
        Log.i(TAG, "getChildView parent is exlist? " + (parent instanceof ExpandableListView));
//        parent.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 700));
        expandableListView = parent;
        Button btn = null;
        if (convertView == null) {
        	if(groupPosition == 2) {
        		Log.i(TAG, "Inflating TIMER");
        		convertView = inflater.inflate(R.layout.rl_rm_timer, null);
//        		btn = (Button) convertView.findViewById(R.id.off);
        		btn.setOnClickListener(this);
        		btn = (Button) convertView.findViewById(R.id.two_hours);
        		btn.setOnClickListener(this);
        		btn = (Button) convertView.findViewById(R.id.four_hours);
        		btn.setOnClickListener(this);
        		btn = (Button) convertView.findViewById(R.id.eight_hours);
        		btn.setOnClickListener(this);
        	} else if (groupPosition == 1){
        		Log.i(TAG, "Inflating FAN_SPEED");
        		convertView = inflater.inflate(R.layout.rl_rm_fan_speed, null);
        		btn = (Button) convertView.findViewById(R.id.fan_speed_silent);
        		btn.setOnClickListener(this);
        		btn = (Button) convertView.findViewById(R.id.fan_speed_turbo);
        		btn.setOnClickListener(this);
        		btn = (Button) convertView.findViewById(R.id.fan_speed_auto);
        		btn.setOnClickListener(this);
        		btn = (Button) convertView.findViewById(R.id.fan_speed_one);
        		btn.setOnClickListener(this);
        		btn = (Button) convertView.findViewById(R.id.fan_speed_two);
        		btn.setOnClickListener(this);
        		btn = (Button) convertView.findViewById(R.id.fan_speed_three);
        		btn.setOnClickListener(this);
        	} else {
        		Log.i(TAG, "Inflating HELLO WORLD");
        		convertView = inflater.inflate(R.layout.ll_rm_control_panel_child_view, null);
        	}
        }
   
        return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
//		Log.i(TAG, "getChildrenCount groupPosition " + groupPosition);
		return ((ArrayList<String>) childItems.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
//		Log.i(TAG, "getGroup groupPosition " + groupPosition);
		return null;
	}

	@Override
	public int getGroupCount() {
//		Log.i(TAG, "getGroupCount groupPosition ");
		return parentItems.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
//		Log.i(TAG, "getGroupId groupPosition " + groupPosition);
		return 0;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
		Log.i(TAG, "getGroupView groupPosition " + groupPosition + " isExpanded " + isExpanded); 
		expandableListView = parent;
		
		if (convertView == null) {
            convertView = inflater.inflate(R.layout.rl_rm_control_panel, null);
        }
		TextView tv = (TextView) convertView.findViewById(R.id.control_panel_item_text);
		
		tv.setText(parentItems.get(groupPosition));
		tv.setFocusable(true);
		
		Button v = (Button) convertView.findViewById(R.id.control_panel_item_button);
		
		AirPurifierEventDto airPurifierEventDto = MainActivity.getAirPurifierEventDto();
		
		Log.i(TAG, "getGroupView button text " + v.getText());
		if(null != airPurifierEventDto) {
			switch(groupPosition) {
			case 0 : v.setText("Off");
					ToggleButton test = (ToggleButton) v;
					Log.i(TAG, "getGroupView power " + getPowerModeText(airPurifierEventDto));
					v.setText(getPowerModeText(airPurifierEventDto));
				break;
			case 1 :
				Log.i(TAG, "getGroupView fan speed text :: " + (Utils.getFanSpeedText(airPurifierEventDto.getFanSpeed())));
				v.setText(Utils.getFanSpeedText(airPurifierEventDto.getFanSpeed()));
				break;
			case 2 :
				Log.i(TAG, "getGroupView timer text " + getTimerText(airPurifierEventDto));
				v.setText(getTimerText(airPurifierEventDto));
				break;
			case 3 : v.setText("N.A.");
				break;
			case 4 : v.setText(getChildLockText(airPurifierEventDto)); 
				break;
			case 5 : v.setText(getIndicatorLightText(airPurifierEventDto));
				break;
			
			}
		}
		v.setFocusable(false);
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isExpanded) {
					((ExpandableListView) parent).collapseGroup(groupPosition);
				} else {
					((ExpandableListView) parent).expandGroup(groupPosition);
				}
			}
		});
		
	    return convertView;
	}
	
	private String getPowerModeText(AirPurifierEventDto airPurifierEventDto) {
		String powerMode = airPurifierEventDto.getPowerMode();
		Log.i(TAG, "powerMode " + powerMode);
		if(powerMode.equals("1")) {
			return "On";
		} else {
			return "Off";
		}
	}
	
	private String getTimerText(AirPurifierEventDto airPurifierEventDto) {
		if(airPurifierEventDto.getDtrs() > 0) {
			return "-";
		} else {
			return "Off";
		}
	}
	
	private String getChildLockText(AirPurifierEventDto airPurifierEventDto) {
		int childLock = airPurifierEventDto.getChildLock();
		if(childLock == 1) {
			return "On";
		} else {
			return "Off";
		}
	}
	
	private String getIndicatorLightText(AirPurifierEventDto airPurifierEventDto) {
		int aqiLight = airPurifierEventDto.getAqiL();
		if(aqiLight == 1) {
			return "On";
		} else {
			return "Off";
		}
	}
	
	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	@Override
	public void onGroupCollapsed(int groupPosition) {
		Log.i(TAG, "onGroupCollapsed groupPosition " + groupPosition + " previousGroupPosition " + previousGroupPosition +" list height " + expandableListView.getMeasuredHeight());
		super.onGroupCollapsed(groupPosition);
	}

	int previousGroupPosition = -1;
	boolean expanded = false;
	
	@Override
	public void onGroupExpanded(int groupPosition) {
		Log.i(TAG, "onGroupExpanded groupPosition " + groupPosition + " previousGroupPosition " + previousGroupPosition);
		
		if(previousGroupPosition != groupPosition) {
			((ExpandableListView) expandableListView).collapseGroup(previousGroupPosition);
			previousGroupPosition = groupPosition;
		}
		
		if(groupPosition == 1 || groupPosition == 2) {
			if(!expanded) {
				expanded = true;
				Button childButton = (Button) expandableListView.findViewById(R.id.control_panel_item_button);
				Log.i(TAG, "onGroupExpanded childHeight " + childButton.getHeight() + " X " + childButton.getMeasuredHeight() + " + " + expandableListView.getMeasuredHeight());
				int totalHeight = expandableListView.getMeasuredHeight() + (2 * childButton.getMeasuredHeight() + 30);
				expandableListView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, totalHeight));
			}
		} else {
			expanded = false;
			setListViewHeightBasedOnChildren((ExpandableListView) expandableListView);
		}
		
		previousGroupPosition = groupPosition;
		super.onGroupExpanded(groupPosition);
	}

	public static void setListViewHeightBasedOnChildren(ExpandableListView listView) {
		Log.i(TAG, "setListViewHeightBasedOnChildren");
	    ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
	    if (listAdapter == null) {
	        return;
	    }
	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getGroupCount(); i++) {
	    	view = listAdapter.getGroupView(i, false, view, listView);
	        if (i == 0) {
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));
	        }
	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
	    Log.i(TAG, "setListViewHeight " + params.height);
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	}
	
	@Override
	public void onClick(View v) {
		Log.i(TAG, "onClick");
		View view;
		Button tv;
		switch (v.getId()) {
//		case R.id.off:
		case R.id.two_hours:
		case R.id.four_hours:
		case R.id.eight_hours:
			
			Log.i(TAG, "onClick TIMER OFF");
			view = ((ExpandableListView) expandableListView).getChildAt(2);
			tv = (Button) view.findViewById(R.id.control_panel_item_button);
			Log.i(TAG, "tv " + tv.getText().toString() + " Button Text " + (((Button) v).getText()));
			tv.setText(((Button) v).getText());
			((ExpandableListView) expandableListView).collapseGroup(2);
			setListViewHeightBasedOnChildren((ExpandableListView) expandableListView);
			expandableListView.requestLayout();
			break;
			
		case R.id.fan_speed_silent:
		case R.id.fan_speed_turbo:
		case R.id.fan_speed_auto:
		case R.id.fan_speed_one:
		case R.id.fan_speed_two:
		case R.id.fan_speed_three:
			
			Log.i(TAG, "onClick TIMER OFF");
			view = ((ExpandableListView) expandableListView).getChildAt(1);
			tv = (Button) view.findViewById(R.id.control_panel_item_button);
			Log.i(TAG, "tv " + tv.getText().toString() + " Button Text " + (((Button) v).getText()));
			tv.setText(((Button) v).getText());
			((ExpandableListView) expandableListView).collapseGroup(1);
			setListViewHeightBasedOnChildren((ExpandableListView) expandableListView);
			MainActivity.getAirPurifierEventDto().setFanSpeed((String) ((Button) v).getText());
			expandableListView.requestLayout();
			break;
		default:
			break;
		}
	}

}
