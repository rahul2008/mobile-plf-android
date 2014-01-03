package com.philips.cl.di.dev.pa.customviews.adapters;

import java.util.ArrayList;

import com.nineoldandroids.animation.ObjectAnimator;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.listeners.ChildClickListener;
import com.philips.cl.di.dev.pa.util.AnimatorConstants;

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
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class RightMenuControlPanelAdapter extends BaseExpandableListAdapter {
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
        		btn = (Button) convertView.findViewById(R.id.off);
        		btn.setOnClickListener(childClickListener);
        	} else if (groupPosition == 1){
        		Log.i(TAG, "Inflating FAN_SPEED");
        		convertView = inflater.inflate(R.layout.rl_rm_fan_speed, null);
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

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
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

}
