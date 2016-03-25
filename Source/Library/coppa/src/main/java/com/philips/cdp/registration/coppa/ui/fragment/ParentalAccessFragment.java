
package com.philips.cdp.registration.coppa.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.ui.utils.RLog;

public class ParentalAccessFragment extends RegistrationCoppaBaseFragment implements OnClickListener {


    private Button mBtnOverAge;
    private Button mBtnUnderAge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onCreateView");

        View view = inflater.inflate(R.layout.fragment_reg_coppa_parental_access, null);
        initUi(view);
        handleOrientation(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " ParentalAccessFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserParentalAccessFragment : onConfigurationChanged");
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    private void initUi(View view) {
        consumeTouch(view);
        mBtnOverAge = (Button)view.findViewById(R.id.reg_btn_over_age);
        mBtnOverAge.setOnClickListener(this);

        mBtnUnderAge = (Button)view.findViewById(R.id.reg_btn_under_age);
        mBtnUnderAge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== R.id.reg_btn_over_age){
            Toast.makeText(getRegistrationFragment().getParentActivity().getApplicationContext(),"Over age",Toast.LENGTH_SHORT).show();
            ParentalAccessConfirmFragment parentalAccessConfirmFragment = new ParentalAccessConfirmFragment();
            getRegistrationFragment().addFragment(parentalAccessConfirmFragment);
        }else if(id == R.id.reg_btn_under_age){
            Toast.makeText(getRegistrationFragment().getParentActivity().getApplicationContext(),"Under  age",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getTitleResourceId() {
        return R.string.parental_access;
    }

}
