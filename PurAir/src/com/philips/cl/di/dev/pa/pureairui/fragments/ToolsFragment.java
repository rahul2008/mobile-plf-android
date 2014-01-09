package com.philips.cl.di.dev.pa.pureairui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.utils.Utils;

public class ToolsFragment extends Fragment implements OnClickListener {
	
	private TextView tvIpaddress ;
	private Button submitButton ;
	private View vMain ;
	
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
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.submitButton:
			Utils.setIPAddress(tvIpaddress.getText().toString(), getActivity()) ;
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getActivity().getWindow().getCurrentFocus().getWindowToken(), 0);
			
			break;

		default:
			break;
		}
		
	}
}
