package com.philips.cl.di.dev.pa.pureairui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.controller.CPPController;
import com.philips.cl.di.dev.pa.controller.SensorDataController;
import com.philips.cl.di.dev.pa.cppdatabase.CppDatabaseAdapter;
import com.philips.cl.di.dev.pa.cppdatabase.CppDatabaseModel;
import com.philips.cl.di.dev.pa.dto.SessionDto;
import com.philips.cl.di.dev.pa.interfaces.ICPDeviceDetailsListener;
import com.philips.cl.di.dev.pa.interfaces.SignonListener;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.utils.Utils;

public class ToolsFragment extends Fragment implements OnClickListener, SignonListener {

	private static final String TAG = ToolsFragment.class.getSimpleName();
	private TextView tvIpaddress ;
	private Button submitButton ;
	private View vMain ;

	private EditText tvRegId ;
	private Button signOnButton ;

	private TextView tvCPPDetails ;

	private CppDatabaseModel cppDatabaseModel ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		vMain = (View) inflater.inflate(R.layout.tools_fragment, container, false);
		initViews();
		return vMain;
	}

	private void initViews() {
		tvIpaddress = (EditText) vMain.findViewById(R.id.tvipaddress);
		tvIpaddress.setText(Utils.getIPAddress(getActivity())) ;
		submitButton = (Button) vMain.findViewById(R.id.submitButton) ;
		submitButton.setOnClickListener(this) ;

		tvRegId = (EditText) vMain.findViewById(R.id.tv_cpp_regId_txt);
		signOnButton = (Button) vMain.findViewById(R.id.get_cpp_btn) ;
		signOnButton.setOnClickListener(this) ;

		tvCPPDetails = (TextView) vMain.findViewById(R.id.tv_cpp_details) ;

		if( Utils.getAirPurifierID(getActivity()) != null &&
				Utils.getAirPurifierID(getActivity()).length() > 0 ) {
			tvCPPDetails.setVisibility(View.VISIBLE) ;
			signOnButton.setText("Reset") ;

			tvCPPDetails.setText("AirPurifier ID: "+Utils.getAirPurifierID(getActivity())) ;
			String regId = Utils.getRegistrationID(getActivity()) ;
			if ( regId != null && regId.length() > 0 ) {
				tvRegId.setText(regId.substring(regId.length()-5)) ;
			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitButton:
			Utils.setIPAddress(tvIpaddress.getText().toString(), getActivity()) ;
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);

			break;
		case R.id.get_cpp_btn:
			if(signOnButton.getText().toString().equals("Reset")) {
				Utils.clearCPPDetails(getActivity()) ;
				tvCPPDetails.setText("") ;
				signOnButton.setText("Sign On") ;
				tvRegId.setText("") ;
				((MainActivity)getActivity()).stopCPPPolling() ;
			}
			else {
				String regStr = tvRegId.getText().toString();
				if (regStr != null && regStr.length() == 5) {
					CppDatabaseAdapter cppDatabaseAdapter = new CppDatabaseAdapter(getActivity());
					cppDatabaseAdapter.open();
					cppDatabaseModel = cppDatabaseAdapter.getCppInfo(regStr);
					if (cppDatabaseModel != null) {

						CPPController.getInstance(getActivity()).addSignonListener(this) ;
						CPPController.getInstance(getActivity()).init() ;				
					}
					else {
						Toast.makeText(getActivity(), "Invalid Key", Toast.LENGTH_LONG).show() ;			
					}
				}
			}
			break;
		default:
			break;
		}

	}

	private boolean isSignon ;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(isSignon) {
				Utils.storeCPPKeys(getActivity(), cppDatabaseModel) ;
				Toast.makeText(getActivity(), "Signon Successfull", Toast.LENGTH_LONG).show();
				signOnButton.setText("Reset") ;
				tvCPPDetails.setVisibility(View.VISIBLE) ;
				tvCPPDetails.setText("AirPurifier: "+cppDatabaseModel.getDistribution()) ;
				((MainActivity)getActivity()).toggleConnection(false) ;
			}
			else {
				Toast.makeText(getActivity(), "Signon failed", Toast.LENGTH_LONG).show();
			}
		};
	};

	@Override
	public void signonStatus(boolean isSigonSuccess) {			
		this.isSignon = isSigonSuccess ;
		handler.sendEmptyMessage(0) ;
	}
}
