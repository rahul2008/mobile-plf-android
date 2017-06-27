package com.philips.amwelluapp.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.base.UIBaseView;
import com.philips.platform.uappframework.listener.BackEventListener;

public class PTHRegistrationDetailsFragment extends PTHBaseFragment implements BackEventListener {
    public static final String TAG = PTHRegistrationDetailsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pth_registration_details_fragment, container, false);
        Toast.makeText(getContext(),"REGISTRATION FRAGMENT",Toast.LENGTH_SHORT).show();
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
    public int getContainerID() {
        return 0;
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }
}
