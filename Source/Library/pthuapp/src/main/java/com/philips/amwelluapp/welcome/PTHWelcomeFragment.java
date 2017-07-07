package com.philips.amwelluapp.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.PTHBasePresenter;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;

public class PTHWelcomeFragment extends PTHBaseFragment implements BackEventListener, View.OnClickListener {
    public static final String TAG = PTHWelcomeFragment.class.getSimpleName();
    private PTHBasePresenter presenter;
    private Button mInitButton;

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
        createCustomProgressBar(view, BIG);
        ((PTHWelcomePresenter)presenter).initializeAwsdk();
        getActionBarListener().updateActionBar("",false);
        mInitButton = (Button) view.findViewById(R.id.init_amwell);
        mInitButton.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.init_amwell) {
            presenter.onEvent(R.id.init_amwell);
        }
    }

    void enableInitButton(boolean isEnabled){
        if (mInitButton != null) {
            mInitButton.setEnabled(isEnabled);
        }
    }
}
