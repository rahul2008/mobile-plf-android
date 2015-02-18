package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCity;
import com.philips.cl.di.dev.pa.dashboard.OutdoorCityInfo;
import com.philips.cl.di.dev.pa.dashboard.OutdoorManager;
import com.philips.cl.di.dev.pa.fragment.DownloadAlerDialogFragement;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.Fonts;
import com.philips.cl.di.dev.pa.util.LanguageUtils;
import com.philips.cl.di.dev.pa.util.MetricsTracker;
import com.philips.cl.di.dev.pa.util.TrackPageConstants;
import com.philips.cl.di.dev.pa.view.AlphabeticalListView;
import com.philips.cl.di.dev.pa.view.AlphabeticalListView.OnTouchingLetterChangedListener;


public class AddOutdoorLocationActivity extends BaseActivity implements  OnTouchingLetterChangedListener {

    private ListView mOutdoorLocationListView;
    private AddOutdoorLocationAdapter mAdapter;
    private Button mActionBarCancelBtn;
    private EditText mEnteredCityName;
    private AlphabeticalListView alphabeticalListView;
    private RelativeLayout overLayout;
    private TextView overLayTextView;

    private List<OutdoorCityInfo> outdoorCityInfoList; 
    private HashMap<String, Integer> letterToPosition = new HashMap<String, Integer>();
    private Set<String> alphapetKeySet = new HashSet<String>();
    
    private WindowManager.LayoutParams wmParams;
    private WindowManager windowManager;
    
    private Handler handler = new Handler() ;
    private OverlayThread overlayThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.outdoor_locations_fragment);

        mOutdoorLocationListView = (ListView) findViewById(R.id.outdoor_locations_list);

        mOutdoorLocationListView.setOnItemClickListener(mOutdoorLocationsItemClickListener);

        initHeading();
        initAlphabetViews() ;
        populateOutdoorLocations();
    }

    private void initAlphabetViews() {
        LayoutInflater inflater = LayoutInflater.from(this);
        overlayThread = new OverlayThread();
        overLayout = (RelativeLayout) inflater.inflate(R.layout.overlay_layout, null);
        overLayTextView = (TextView) overLayout.findViewById(R.id.over_text);
        overLayout.setVisibility(View.INVISIBLE);
        wmParams = new WindowManager.LayoutParams(android.view.WindowManager.LayoutParams.WRAP_CONTENT,
                android.view.WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        try {
            windowManager.addView(overLayout, wmParams);
        } catch (Exception e) {

        }

        alphabeticalListView = (AlphabeticalListView) findViewById(R.id.letterlistview);
        alphabeticalListView.setVisibility(View.VISIBLE);

        alphabeticalListView.setOnTouchingLetterChangedListener(this) ;

    }

    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overLayout.setVisibility(View.GONE);
        }
    }

    private void updateOverlayLayoutPosition(float x, float y) {
        wmParams.y = (int) (y);
        windowManager.updateViewLayout(overLayout, wmParams);
    }

    @Override
    public void onResume() {
        super.onResume();
        MetricsTracker.trackPage(TrackPageConstants.ADD_OUTDOOR_LOCATION);
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	if( handler != null ) {
    		handler.removeCallbacks(overlayThread) ;
    	}
    }

    private void updateAdapter(String input) {
        alphapetKeySet.clear() ;
        letterToPosition.clear() ;
        List<OutdoorCityInfo> tempOutdoorCityInfoList = new ArrayList<OutdoorCityInfo>() ;
        for (OutdoorCityInfo outdoorCityInfo : outdoorCityInfoList) {

            String cityName = getCityNameWithRespectToLanguage(outdoorCityInfo);
            String addStr = outdoorCityInfo.getCityName().substring(0,1) ;
            input = input.toLowerCase();
            if( !input.isEmpty() && cityName.contains(input)) {
                tempOutdoorCityInfoList.add(outdoorCityInfo) ;
                if( alphapetKeySet.add(addStr)) {
                    letterToPosition.put(addStr,tempOutdoorCityInfoList.size()-1) ;
                }
            }
        }

        if( !input.isEmpty() || !tempOutdoorCityInfoList.isEmpty()) {
            mAdapter = new AddOutdoorLocationAdapter(this, R.layout.simple_list_item, tempOutdoorCityInfoList);
        }
        else {
            populateAlphabetKeys() ;
            mAdapter = new AddOutdoorLocationAdapter(this, R.layout.simple_list_item, outdoorCityInfoList);
        }
        mOutdoorLocationListView.setAdapter(mAdapter);
    }

    private String getCityNameWithRespectToLanguage(OutdoorCityInfo outdoorCityInfo) {
        String cityName = outdoorCityInfo.getCityName().toLowerCase();;

        if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANS")) {
            cityName = outdoorCityInfo.getCityNameCN() ;
        } else if(LanguageUtils.getLanguageForLocale(Locale.getDefault()).contains("ZH-HANT")) {
            cityName = outdoorCityInfo.getCityNameTW() ;
        }

        return cityName;
    }

    private void initHeading() {

        mActionBarCancelBtn = (Button) findViewById(R.id.search_bar_cancel_btn);
        mActionBarCancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEnteredCityName = (EditText) findViewById(R.id.search_bar_city_name);
        mEnteredCityName.setTypeface(Fonts.getGillsansLight(this));
        mEnteredCityName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateAdapter(s.toString().trim());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void showAlertDialog(String title, String message) {
        try {
            FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();

            Fragment prevFrag = getSupportFragmentManager().findFragmentByTag("max_purifier_reached");
            if (prevFrag != null) {
                fragTransaction.remove(prevFrag);
            }

            fragTransaction.add(DownloadAlerDialogFragement.
                    newInstance(title, message), "max_purifier_reached").commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            ALog.e(ALog.ERROR, "Error: " + e.getMessage());
        }
    }

    private OnItemClickListener mOutdoorLocationsItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

            if(OutdoorManager.getInstance().getUsersCitiesList().size() > 19) {
                showAlertDialog(null, getString(R.string.max_city_reached));
                return;
            }

            OutdoorCityInfo outdoorCityInfo = (OutdoorCityInfo) mAdapter.getItem(position);

            String city = outdoorCityInfo.getCityName();
            String key = AddOutdoorLocationHelper.getCityKeyWithRespectDataProvider(outdoorCityInfo);

            MetricsTracker.trackActionLocationWeather(city);
            OutdoorManager.getInstance().addAreaIDToUsersList(key);
            OutdoorManager.getInstance().resetUpdatedTime();
            new UserCitiesDatabase().insertCity(key, outdoorCityInfo.getDataProvider());
            OutdoorManager.getInstance().setOutdoorViewPagerCurrentPage(
            		OutdoorManager.getInstance().getUsersCitiesList().size());
            finish();
        }
    };

    
    private void populateOutdoorLocations() {
        Map<String, OutdoorCity> outdoorCityMap = OutdoorManager.getInstance().getCitiesMap() ;
        outdoorCityInfoList = AddOutdoorLocationHelper.getAllCityInfoList(outdoorCityMap) ;
        populateAlphabetKeys() ;
        mAdapter = new AddOutdoorLocationAdapter(this, R.layout.simple_list_item, outdoorCityInfoList);
        mOutdoorLocationListView.setAdapter(mAdapter);
    }

    private void populateAlphabetKeys() {
        alphapetKeySet.clear() ;
        letterToPosition.clear() ;
        String addStr = "";
        for( int index = 0 ; index < outdoorCityInfoList.size() ; index ++ ) {
            addStr = outdoorCityInfoList.get(index).getCityName().substring(0, 1) ;
            if( alphapetKeySet.add(addStr)) {
                letterToPosition.put(addStr, index);

            }
        }
    }

    @Override
    public void onTouchingLetterChanged(String alphabetSelected, float x, float y) {
        int position = -1;
        if(!letterToPosition.containsKey(alphabetSelected)) {
        	return ;
        }
        
    	position = letterToPosition.get(alphabetSelected);
    	mOutdoorLocationListView.setSelection(position);
        updateOverlayLayoutPosition(x, y);
        overLayTextView.setText(alphabetSelected);
        overLayout.setVisibility(View.VISIBLE);
        handler.removeCallbacks(overlayThread);
        handler.postDelayed(overlayThread, 1200);
        
    }
    
    @Override
    protected void onDestroy() {
    	if (null != windowManager && overLayout != null) {
			windowManager.removeView(overLayout);
		}
		windowManager = null;
		overLayout = null;
    	super.onDestroy();
    }
    
}
