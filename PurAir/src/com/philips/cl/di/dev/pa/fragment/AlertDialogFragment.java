package com.philips.cl.di.dev.pa.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.AlertDialogBtnInterface;
import com.philips.cl.di.dev.pa.util.Fonts;

public class AlertDialogFragment extends DialogFragment {

	private static final String EXTRA_TITLETEXTID = "com.philips.cl.di.dev.pa.extra_titletextid";
	private static final String EXTRA_MESSAGETEXTID = "com.philips.cl.di.dev.pa.extra_messagetextid";
	private static final String EXTRA_POSBTNTEXTID = "com.philips.cl.di.dev.pa.extra_posbtntextid";
	private static final String EXTRA_NEGBTNTEXTID = "com.philips.cl.di.dev.pa.extra_negbtntextid";
	
	private TextView title;
	private TextView message;
	private Button negBtn;
	private Button posBtn;
	
	private AlertDialogBtnInterface mBtnListener;

	public static AlertDialogFragment newInstance(int messageTextId, int posBtnTextId) {
		AlertDialogFragment fragment = new AlertDialogFragment();

		Bundle args = new Bundle();
		args.putSerializable(EXTRA_MESSAGETEXTID, messageTextId);
		args.putSerializable(EXTRA_POSBTNTEXTID, posBtnTextId);

		fragment.setArguments(args);		
		return fragment;		
	}

	public static AlertDialogFragment newNoTitleInstance(int messageTextId, int posBtnTextId, int negBtnTextId) {
		AlertDialogFragment fragment = new AlertDialogFragment();
		
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_MESSAGETEXTID, messageTextId);
		args.putSerializable(EXTRA_POSBTNTEXTID, posBtnTextId);
		args.putSerializable(EXTRA_NEGBTNTEXTID, negBtnTextId);
		
		fragment.setArguments(args);		
		return fragment;		
	}

	public static AlertDialogFragment newInstance(int titleTextId, int messageTextId, int posBtnTextId) {
		AlertDialogFragment fragment = new AlertDialogFragment();
		
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TITLETEXTID, titleTextId);
		args.putSerializable(EXTRA_MESSAGETEXTID, messageTextId);
		args.putSerializable(EXTRA_POSBTNTEXTID, posBtnTextId);
		
		fragment.setArguments(args);		
		return fragment;		
	}

	public static AlertDialogFragment newInstance(int titleTextId, int messageTextId, int posBtnTextId, int negBtnTextId) {
		AlertDialogFragment fragment = new AlertDialogFragment();
		
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_TITLETEXTID, titleTextId);
		args.putSerializable(EXTRA_MESSAGETEXTID, messageTextId);
		args.putSerializable(EXTRA_POSBTNTEXTID, posBtnTextId);
		args.putSerializable(EXTRA_NEGBTNTEXTID, negBtnTextId);
		
		fragment.setArguments(args);		
		return fragment;		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_alert, container, false);
		getDialog().requestWindowFeature(STYLE_NO_TITLE);
		setCancelable(false);
		initializeView(view);
		return view;
	}
	
	public void setOnClickListener(final AlertDialogBtnInterface buttonListener) {
		mBtnListener = buttonListener;
	}

	private void initializeView(View view) {	
		title = (TextView) view.findViewById(R.id.alert_title);
		message = (TextView) view.findViewById(R.id.alert_message);
		posBtn = (Button) view.findViewById(R.id.btn_yes);
		negBtn = (Button) view.findViewById(R.id.btn_close);
		
		title.setTypeface(Fonts.getGillsans(getActivity()));
		message.setTypeface(Fonts.getGillsans(getActivity()));
		posBtn = (Button) view.findViewById(R.id.btn_yes);
		negBtn = (Button) view.findViewById(R.id.btn_close);
		
		int titleTextId = getArguments().getInt(EXTRA_TITLETEXTID);
		if (titleTextId > 0) {
			title.setVisibility(View.VISIBLE);
			title.setText(titleTextId);
		} else {
			title.setVisibility(View.GONE);
		}

		int messageTextId = getArguments().getInt(EXTRA_MESSAGETEXTID);
		if (messageTextId > 0) {
			message.setVisibility(View.VISIBLE);
			message.setText(messageTextId);
		} else {
			message.setVisibility(View.GONE);
		}
		
		int posBtnTextId = getArguments().getInt(EXTRA_POSBTNTEXTID, -1);
		if (posBtnTextId > 0) {
			posBtn.setVisibility(View.VISIBLE);
			posBtn.setText(posBtnTextId);
		} else {
			posBtn.setVisibility(View.GONE);
		}

		int negBtnTextId = getArguments().getInt(EXTRA_NEGBTNTEXTID, -1);
		if (negBtnTextId > 0) {
			negBtn.setVisibility(View.VISIBLE);
			negBtn.setText(negBtnTextId);
		} else {
			negBtn.setVisibility(View.GONE);
		}
		
		setButtonListeners();
	}
	
	private void setButtonListeners() {
		posBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBtnListener!= null) {
					mBtnListener.onPositiveButtonClicked();
				}
				dismiss();
			}
		});
		negBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBtnListener!= null) {
					mBtnListener.onNegativeButtonClicked();
				}
				dismiss();
			}
		});
	}

}
