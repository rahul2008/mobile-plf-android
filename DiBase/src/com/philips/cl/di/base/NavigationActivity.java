package com.philips.cl.di.base;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class NavigationActivity extends FragmentActivity implements OnClickListener {
	
	protected static final int VERY_LOW 	= 0;
	protected static final int LOW 			= 1;
	protected static final int MEDIUM 		= 2;
	protected static final int HIGH 		= 3;
	protected static final int VERY_HIGH 	= 4;

	protected static final int STOP 		= -1;
	protected static final int RUNNING 		= 2;
	protected static final int PAUSED 		= 3;
	
	protected int state;

	protected DrawerLayout 			mDrawerLayout;
	protected ListView 				mDrawerList;
	protected ActionBarDrawerToggle mDrawerToggle;

	protected JSONArray 			mDrawerItems;
	protected JSONArray 			mModel;

	protected DataSyncTask 			mSyncTask;

	protected int 					mCurrentFragment;
	protected String 				mServer="http://di02.ehv.campus.philips.com";
	protected String 				mDevice="192.168.1.1";
	protected String 				mUserId="999";
	protected String 				mEwsSsid = "Philips Setup";

	protected abstract void updateViews(String progress) throws JSONException;
	protected abstract String syncData();
	private class DataSyncTask extends AsyncTask<String, String, String>{
		@Override
		protected String doInBackground(String... arg0) {
			String result = null;
			while(state!=STOP){
				if(state==RUNNING){
					result = syncData();
					publishProgress(result);
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			if(DiHelper.isJson(progress[0])){
				try {
					updateViews(progress[0]);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
	     }
	};
	public class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
			selectItem(position);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(mSyncTask==null){
			mSyncTask =new DataSyncTask();
			mSyncTask.execute(" ");
		}else{
			state=RUNNING;
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		state=PAUSED;
	}
	@Override
	protected void onStop() {
		super.onStop();
		state=STOP;
		mSyncTask =null;
	}
	@Override
	protected void onStart() {
		super.onStart();
		state=RUNNING;
	}
	public void selectItem(int position) {
		try {
			//mFragment = (Fragment) Class.forName((String) ((JSONObject)mDrawerItems.get(position)).get("class")).getConstructor().newInstance();
			//android.app.FragmentManager fragmentManager = getFragmentManager();
			//fragmentManager.beginTransaction().replace(R.id.fragment_container, mFragment).commit();
			// update selected item and title, then close the drawer
			//mDrawerList.setItemChecked(position, true);
			//setTitle(mDrawerItems.get(position).get);
			//mDrawerLayout.closeDrawer(mDrawerList);
            Intent intent = new Intent(this,Class.forName((String) ((JSONObject)mDrawerItems.get(position)).get("class")));
            startActivityForResult(intent, 12345);

		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	protected void configureDrawer(int layout,int drawer) {
		ArrayAdapter<String> drawItems = new ArrayAdapter<String>(this,	R.layout.drawer_list_item);
		try {
			mDrawerItems = mModel;
			for (Integer i = 0; i < mDrawerItems.length(); i++) {
				String obj = mDrawerItems.getJSONObject(i).getString("name");
				drawItems.add(obj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		mDrawerLayout = (DrawerLayout) findViewById(layout);
		mDrawerList = (ListView) findViewById(drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(drawItems);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		0,
		0
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle("Closed");

			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Open");
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
       if (mDrawerToggle.onOptionsItemSelected(item)) {
           return true;
       }
       return super.onOptionsItemSelected(item);
	}
	protected void loadDataFromPreference(int aJsonResource) {
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
		mServer = preference.getString("server", null);
		mDevice = preference.getString("device", null);
		mUserId = preference.getString("user_id", null);
		
	}


/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_empty);
		loadDataFromAssets(R.raw.menu);
		configureDrawer();

	}
*/

}
