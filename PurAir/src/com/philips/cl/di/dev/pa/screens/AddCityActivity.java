package com.philips.cl.di.dev.pa.screens;

import java.text.SimpleDateFormat;
import java.util.Stack;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constants.AppConstants;
import com.philips.cl.di.dev.pa.screens.BaseActivity.ClickListenerForScrolling;
import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;
import com.philips.cl.di.dev.pa.utils.DBHelper;
import com.philips.cl.di.dev.pa.utils.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class AddCityActivity.
 */
public class AddCityActivity extends BaseActivity implements OnClickListener {

	/** The btn submit. */
	private Button btnSubmit = null;
	
	/** The et aqi. */
	private EditText etAQI = null;
	
	/** The sp city name. */
	private Spinner spCityName = null;
	
	/** The sp province name. */
	private Spinner spProvinceName = null;
	
	/** The dp date. */
	private DatePicker dpDate = null;
	
	/** The s city name. */
	private String sCityName;
	
	/** The s province name. */
	private String sProvinceName;
	
	/** The i aqi. */
	private int iAQI;
	
	/** The s date. */
	private String sDate;
	
	/** The tag. */
	private String TAG = getClass().getName();
	
	/** The helper. */
	private DBHelper helper;
	
	/** The database. */
	private SQLiteDatabase database;
	
	/** The adapter. */
	private DatabaseAdapter adapter;

	/** The tv city. */
	private TextView tvCity;
	
	/** The tv province. */
	private TextView tvProvince;
	
	/** The tv date. */
	private TextView tvDate;
	
	/** The tv aqi. */
	private TextView tvAQI;
	/** The ImageViews . */
	private ImageView ivLeftMenu, ivRightSettings;

	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.screens.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.in, R.anim.out);
		adapter = new DatabaseAdapter(this);

		// Setting click listeners for left menu and right settings
		ViewGroup tabBar = (ViewGroup) centerView
				.findViewById(R.id.rlTopNavigation);

		ivLeftMenu = (ImageView) tabBar.findViewById(R.id.ivMenu);
		ivLeftMenu.setOnClickListener(new ClickListenerForScrolling(scrollView,
				leftMenu));
		ivRightSettings = (ImageView) tabBar.findViewById(R.id.ivSettings);
		ivRightSettings.setOnClickListener(new ClickListenerForScrolling(
				scrollView, rightSettings));
		initializeViews();
	}

	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.screens.BaseActivity#onPause()
	 */
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.in, R.anim.out);
		adapter.close();
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		adapter.open();
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see com.philips.cl.di.dev.pa.screens.BaseActivity#getCenterView()
	 */
	@Override
	protected View getCenterView() {
		return inflater.inflate(R.layout.activity_addcity, null);
	}

	/**
	 * Initialize views.
	 */
	private void initializeViews() {
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		etAQI = (EditText) findViewById(R.id.etAQIValue);
		spCityName = (Spinner) findViewById(R.id.spCity);
		spProvinceName = (Spinner) findViewById(R.id.spProvince);
		dpDate = (DatePicker) findViewById(R.id.dpDate);
		tvCity = (TextView) findViewById(R.id.tvCity);
		tvCity.setTypeface(Utils.getTypeFace(getApplicationContext()));
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvDate.setTypeface(Utils.getTypeFace(getApplicationContext()));
		tvAQI = (TextView) findViewById(R.id.tvAQI);
		tvAQI.setTypeface(Utils.getTypeFace(getApplicationContext()));
		tvProvince = (TextView) findViewById(R.id.tvProvince);
		tvProvince.setTypeface(Utils.getTypeFace(getApplicationContext()));

	}

	/**
	 * Assign values and validate.
	 *
	 * @return the string
	 */
	private String assignValuesAndValidate() {
		sCityName = spCityName.getSelectedItem().toString();
		Log.i(TAG, sCityName);
		sProvinceName = spProvinceName.getSelectedItem().toString();
		Log.i(TAG, sProvinceName);
		sDate = new SimpleDateFormat("yyyy.MM.dd").format(dpDate
				.getCalendarView().getDate());
		Log.i(TAG, sDate);
		String sAQI = etAQI.getText().toString();
		if ("".equals(sAQI)) {
			return "Please enter a valid AQI";

		} else {
			iAQI = Integer.parseInt(etAQI.getText().toString());
			if (iAQI > 500) {
				return "Please enter a valid AQI";
			}
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSubmit:
			String sMessage = assignValuesAndValidate();
			if (sMessage != null) {
				Toast.makeText(this, sMessage, Toast.LENGTH_LONG).show();

			} else {
				// Send the values to data base ;
				Log.i(TAG, "Send to database");
				helper = new DBHelper(this);
				database = helper.getWritableDatabase();
				if (insertIntoDatabase()) {
					Toast.makeText(this, "Successful update", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(this, "Unable to update", Toast.LENGTH_LONG)
							.show();
				}

			}

			break;

		default:
			break;
		}

	}

	/**
	 * Insert into database.
	 *
	 * @return true, if successful
	 */
	public boolean insertIntoDatabase() {
		return adapter.insertIntoDatabase(sCityName, iAQI, sProvinceName,
				sDate, "TIME");
	}

}
