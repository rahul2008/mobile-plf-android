package com.philips.cdp.registration.ui.traditional;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.RLog;

/**
 * Created by 310190722 on 11/13/2015.
 */
public class PhilipsNewsFragment extends RegistrationBaseFragment implements View.OnClickListener {

    private Context mContext;
    private LinearLayout mLlNewsContainer;
    private Button mBtnBack;
    private RelativeLayout mRlBackBtnContainer;


    @Override
    public void onAttach(Activity activity) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onCreateView");
        mContext = getRegistrationFragment().getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_philips_news, null);
        handleOrientation(view);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        consumeTouch(view);
        mLlNewsContainer = (LinearLayout)view.findViewById(R.id.ll_reg_news_container);
        mBtnBack = (Button)view.findViewById(R.id.reg_btn_back);
        mBtnBack.setOnClickListener(this);
        mRlBackBtnContainer = (RelativeLayout)view.findViewById(R.id.rl_reg_btn_back_container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onDestroy");
        RLog.i(RLog.EVENT_LISTENERS, "PhilipsNewsFragment unregister: NetworStateListener");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "PhilipsNewsFragment : onConfigurationChanged");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.reg_btn_back) {
            RLog.d(RLog.ONCLICK, "PhilipsNewsFragment : back pressed");
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void setViewParams(Configuration config, int width) {
        applyParams(config, mLlNewsContainer, width);
        applyParams(config, mRlBackBtnContainer, width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.Philips_News_Title;
    }
}
