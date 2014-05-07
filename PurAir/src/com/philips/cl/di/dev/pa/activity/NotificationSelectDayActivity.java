package com.philips.cl.di.dev.pa.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;

public class NotificationSelectDayActivity extends BaseActivity {

	private String[] days={"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday","Saturday"};
	private ArrayAdapter<String> mAdapter; 
	private ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_select_days);
		initActionBar();
		setActionBarTitle(R.string.repeat);
		lv=(ListView) findViewById(R.id.daysList);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, days);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setAdapter(mAdapter);		
	}
	
	/*Initialize action bar */
	private void initActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(null);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = getLayoutInflater().inflate(R.layout.action_bar, null);
		((ImageView)view.findViewById(R.id.right_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.left_menu_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.back_to_home_img)).setVisibility(View.GONE);
		((ImageView)view.findViewById(R.id.add_location_img)).setVisibility(View.GONE);
		actionBar.setCustomView(view);
	}

	/*Sets Action bar title */
	public void setActionBarTitle(int tutorialTitle) {    	
		TextView textView = (TextView) findViewById(R.id.action_bar_title);
		textView.setTypeface(Fonts.getGillsansLight(this));
		textView.setTextSize(24);
		textView.setText(this.getText(tutorialTitle));
	}
	
	@Override
	public void onBackPressed() {
		returnSelectedDays();
	}
	
	public void returnSelectedDays()
	{
		SparseBooleanArray checked = lv.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add day if it is checked i.e.) == TRUE!
            if (checked.valueAt(i))
                selectedItems.add(mAdapter.getItem(position));
        }
 
        String[] outputStrArr = new String[selectedItems.size()];
 
        for (int i = 0; i < selectedItems.size(); i++) {
            outputStrArr[i] = selectedItems.get(i);
        }
        
        Intent in= new Intent();
        in.putExtra("SELECTED_DAYS", outputStrArr);
        setResult(RESULT_OK,in);
        finish();
	}
}
