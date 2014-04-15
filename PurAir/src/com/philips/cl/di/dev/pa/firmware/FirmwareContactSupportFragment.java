package com.philips.cl.di.dev.pa.firmware;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.util.Fonts;


public class FirmwareContactSupportFragment extends BaseFragment implements OnClickListener{
		
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.firmware_contact_support, container, false);
		initViews(view);
		((FirmwareUpdateActivity) getActivity()).setActionBar(6);
		return view;
	}

	private void initViews(View view) {
		Button btnCall = (Button) view.findViewById(R.id.btn_call);
		btnCall.setTypeface(Fonts.getGillsans(getActivity()));
		btnCall.setOnClickListener(this);
		Button btnSendEmail = (Button) view.findViewById(R.id.btn_send_email);
		btnSendEmail.setOnClickListener(this);
		btnSendEmail.setTypeface(Fonts.getGillsans(getActivity()));
		Button btnSendReport = (Button) view.findViewById(R.id.btn_send_report);
		btnSendReport.setOnClickListener(this);
		btnSendReport.setTypeface(Fonts.getGillsans(getActivity()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_call:
			Intent dialSupportIntent = new Intent(Intent.ACTION_DIAL);
			dialSupportIntent.setData(Uri.parse("tel:" + getString(R.string.contact_philips_support_phone_num)));
			startActivity(Intent.createChooser(dialSupportIntent, "Air Purifier support"));
			break;
		case R.id.btn_send_email:
			Intent supportEmailIntent = new Intent(
					Intent.ACTION_SENDTO, Uri.fromParts("mailto","sangamesh.bn@philips.com", null));
			supportEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support");
			supportEmailIntent.putExtra(Intent.EXTRA_TEXT, "No template");
			startActivity(Intent.createChooser(supportEmailIntent, "Air Purifier support"));
			break;
		case R.id.btn_send_report:
			//TODO : Get clarification of what has to be in the report.
			break;
		default:
			break;
		
	 }
	}
}
