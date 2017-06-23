package com.philips.amwelluapp.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.UIBasePresenter;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;

public class PTHWelcomeFragment extends PTHBaseFragment implements BackEventListener {
    public static final String TAG = PTHWelcomeFragment.class.getSimpleName();
    private UIBasePresenter presenter;
    private ProgressBar progressBar;

    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }


    private FragmentLauncher fragmentLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PTHWelcomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pth_welcome_fragment, container, false);
        mPTHBaseFragmentProgressBar = (ProgressBar) view.findViewById(R.id.initialize_progress_bar);
        presenter.onEvent(R.id.initialize_progress_bar);
        ((PTHWelcomePresenter)presenter).initializeAwsdk();
        return view;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
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
    public int getContainerID() {
        return fragmentLauncher.getParentContainerResourceID();
    }



    @Override
    public boolean handleBackEvent() {
        return false;
    }
}
