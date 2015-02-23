package com.philips.cl.di.dev.pa.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.Fonts;

public class ProductRegistrationStepsFragment extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.product_registration_step_1,
				container, false);
		initializeView(view);
		return view;
	}

	private void initializeView(View view) {
		TextView registrationTitleStep = (TextView) view
				.findViewById(R.id.registration_step_1_title);
		TextView registrationEmailAddressSubtitle = (TextView) view
				.findViewById(R.id.registration_email_address_subtitle);
		TextView registrationEmailQues = (TextView) view
				.findViewById(R.id.registration_email_ques);
		TextView emailText = (TextView) view.findViewById(R.id.email_text);
		TextView promotionsCheck = (TextView) view
				.findViewById(R.id.promotions_check);
		final Button btnNextStep = (Button) view
				.findViewById(R.id.btn_next_step);

		registrationTitleStep.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		registrationEmailAddressSubtitle.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		registrationEmailQues.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		emailText.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		promotionsCheck.setTypeface(Fonts.getCentraleSansLight(getActivity()));
		btnNextStep.setTypeface(Fonts.getCentraleSansLight(getActivity()));

		emailText.addTextChangedListener(new TextWatcher() {

			private Pattern pattern;
			private Matcher matcher;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
				matcher = pattern.matcher(s);

				if (matcher.matches()) {
					btnNextStep.setEnabled(true);
				} else {
					btnNextStep.setEnabled(false);
				}
			}
		});
	}

}
