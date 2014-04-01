package com.philips.cl.di.dev.pa.pureairui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.utils.Fonts;

public class ProductRegFragment extends BaseFragment implements OnClickListener {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.prod_reg_fragment, container, false);
		intializeView(view);
		initMenu();
		return view;
	}

	private void intializeView(View rootView) {
		TextView productRegtitle= (TextView) rootView.findViewById(R.id.prod_reg_title);
		TextView lblKitchen=(TextView) rootView.findViewById(R.id.lbl_kitchen);
		TextView lblLivingRoom =(TextView) rootView.findViewById(R.id.lbl_living_room);
		TextView lblPurifierQuestion= (TextView) rootView.findViewById(R.id.lbl_purifier_ques);
		Button kitchenRegisterBtn=(Button) rootView.findViewById(R.id.btn_register_kitchen);
		Button livingRoomRegisterBtn=(Button) rootView.findViewById(R.id.btn_register_living_room);
		Button connectedPurifierBtn=(Button) rootView.findViewById(R.id.btn_connected_purifier);
		
		productRegtitle.setTypeface(Fonts.getGillsans(getActivity()));
		lblKitchen.setTypeface(Fonts.getGillsans(getActivity()));
		lblLivingRoom.setTypeface(Fonts.getGillsans(getActivity()));
		lblPurifierQuestion.setTypeface(Fonts.getGillsans(getActivity()));
		kitchenRegisterBtn.setTypeface(Fonts.getGillsans(getActivity()));
		livingRoomRegisterBtn.setTypeface(Fonts.getGillsans(getActivity()));
		connectedPurifierBtn.setTypeface(Fonts.getGillsans(getActivity()));
		
		kitchenRegisterBtn.setOnClickListener(this);
		livingRoomRegisterBtn.setOnClickListener(this);
	}
	
	private void initMenu() {
		MainActivity activity= (MainActivity) getActivity();
		activity.setRightMenuVisibility(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register_kitchen:
			MainActivity activity=(MainActivity) getActivity();
			activity.showFragment(new ProductRegistrationStepsFragment());
			activity.setTitle(getString(R.string.list_item_prod_reg));
			break;

		default:
			break;
		}
	}
}
