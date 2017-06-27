package com.philips.amwelluapp.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.UIBasePresenter;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.ProgressBar;

public class PTHWelcomeFragment extends PTHBaseFragment implements BackEventListener {
    public static final String TAG = PTHWelcomeFragment.class.getSimpleName();
    private UIBasePresenter presenter;
    //private com.philips.platform.uid.view.widget.ProgressBar progressBar;

    public FragmentLauncher getFragmentLauncher() {
        return mFragmentLauncher;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PTHWelcomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.pth_welcome_fragment, container, false);
        getContext().getTheme().applyStyle(R.style.PTHCircularPB, true);
        createCustomProgressBar();
        view.addView(mPTHBaseFragmentProgressBar);
        ((PTHWelcomePresenter)presenter).initializeAwsdk();
        getActionBarListener().updateActionBar("",false);
        return view;
    }


    @Override
    public void finishActivityAffinity() {
        getActivity().finishAffinity();
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }



    @Override
    public boolean handleBackEvent() {
        return true;
    }
}
