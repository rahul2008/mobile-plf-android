package com.philips.cl.di.dev.pa.screens.fragments;

import java.text.SimpleDateFormat;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;
import com.philips.cl.di.dev.pa.utils.DBHelper;
import com.philips.cl.di.dev.pa.utils.Utils;

// TODO: Auto-generated Javadoc
/**
 * The Class AddCityFragment.
 */
public class AddCityFragment extends Fragment implements OnClickListener {

	public static final String TAG = AddCityFragment.class.getSimpleName();

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

	private View v;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.activity_addcity, container, false);
		adapter = new DatabaseAdapter(getActivity());
		v.setOnClickListener(this);
		initializeViews();
		return v;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSubmit:
			String sMessage = assignValuesAndValidate();
			if (sMessage != null) {
				Toast.makeText(getActivity(), sMessage, Toast.LENGTH_LONG)
						.show();

			} else {
				// Send the values to data base ;
				Log.i(TAG, "Send to database");
				helper = new DBHelper(getActivity());
				database = helper.getWritableDatabase();
				if (insertIntoDatabase()) {
					Toast.makeText(getActivity(), "Successful update",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getActivity(), "Unable to update",
							Toast.LENGTH_LONG).show();
				}

			}

			break;
		default:
			break;
		}

	}

	/**
	 * Initialize views.
	 */
	private void initializeViews() {
		btnSubmit = (Button) v.findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		etAQI = (EditText) v.findViewById(R.id.etAQIValue);
		spCityName = (Spinner) v.findViewById(R.id.spCity);
		spProvinceName = (Spinner) v.findViewById(R.id.spProvince);
		dpDate = (DatePicker) v.findViewById(R.id.dpDate);
		tvCity = (TextView) v.findViewById(R.id.tvCity);
		tvCity.setTypeface(Utils.getTypeFace(getActivity()
				.getApplicationContext()));
		tvDate = (TextView) v.findViewById(R.id.tvDate);
		tvDate.setTypeface(Utils.getTypeFace(getActivity()
				.getApplicationContext()));
		tvAQI = (TextView) v.findViewById(R.id.tvAQI);
		tvAQI.setTypeface(Utils.getTypeFace(getActivity()
				.getApplicationContext()));
		tvProvince = (TextView) v.findViewById(R.id.tvProvince);
		tvProvince.setTypeface(Utils.getTypeFace(getActivity()
				.getApplicationContext()));

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

	/**
	 * Insert into database.
	 * 
	 * @return true, if successful
	 */
	public boolean insertIntoDatabase() {
		return adapter.insertIntoDatabase(sCityName, iAQI, sProvinceName,
				sDate, "TIME");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philips.cl.di.dev.pa.screens.BaseActivity#onPause()
	 */
	@Override
	public void onPause() {
		adapter.close();
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		adapter.open();
		super.onResume();
	}

}
