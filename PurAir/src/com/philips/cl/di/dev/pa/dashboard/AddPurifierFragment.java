package com.philips.cl.di.dev.pa.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.fragment.BaseFragment;
import com.philips.cl.di.dev.pa.fragment.StartFlowChooseFragment;

public class AddPurifierFragment extends BaseFragment implements OnClickListener{
	
	private ImageView ivAddNewPurifier;
	private Button btnGotoShop;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_new_purifier, null);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ivAddNewPurifier = (ImageView) getActivity().findViewById(R.id.iv_circle_add_purifier);
		ivAddNewPurifier.setOnClickListener(this);
		btnGotoShop = (Button) getActivity().findViewById(R.id.btn_go_to_shop);
		btnGotoShop.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_circle_add_purifier:
			((MainActivity) getActivity()).showFragment(new StartFlowChooseFragment());
			break;
		case R.id.btn_go_to_shop:
			//TODO : Open browser with shop URL
			
			break;

		default:
			break;
		}
	}

}
