package com.philips.cl.di.dev.pa.pureairui.fragments;

import android.content.Context;
import android.os.Bundle;
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

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.cppdatabase.CppDatabaseAdapter;
import com.philips.cl.di.dev.pa.cppdatabase.CppDatabaseModel;
import com.philips.cl.di.dev.pa.pureairui.MainActivity;
import com.philips.cl.di.dev.pa.utils.Utils;

public class ToolsFragment extends Fragment implements OnClickListener {
	
	private static final String TAG = ToolsFragment.class.getSimpleName();
	private TextView tvIpaddress ;
	private Button submitButton ;
	private View vMain ;
	
	private EditText tvRegId ;
	private Button signOnButton ;
	
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
			String regStr = tvRegId.getText().toString();
			if (regStr != null && regStr.length() == 5) {
				CppDatabaseAdapter cppDatabaseAdapter = new CppDatabaseAdapter(getActivity());
				cppDatabaseAdapter.open();
				CppDatabaseModel cppDatabaseModel = cppDatabaseAdapter.getCppInfo(regStr);
				if (cppDatabaseModel != null) {
					Log.i(TAG, "Macid= " + cppDatabaseModel.getMacId());
					Log.i(TAG, "EUID= " + cppDatabaseModel.getEuId());
					Log.i(TAG, "NC= " + cppDatabaseModel.getSetNc());
					Log.i(TAG, "CTN= " + cppDatabaseModel.getCtn());
					Log.i(TAG, "P Key= " + cppDatabaseModel.getPrivateKey());
					Log.i(TAG, "Sys key= " + cppDatabaseModel.getSysKey());
					Log.i(TAG, "REg ID= " + cppDatabaseModel.getRegId());
					Log.i(TAG, "Distribution= " + cppDatabaseModel.getDistribution());
					tvRegId.setText("");
				}
			}
			break;
		default:
			break;
		}
		
	}
}
