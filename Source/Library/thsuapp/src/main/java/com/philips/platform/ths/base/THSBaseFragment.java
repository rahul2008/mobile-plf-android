package com.philips.platform.ths.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.philips.platform.ths.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.ProgressBar;


public class THSBaseFragment extends Fragment implements THSBaseView {


    public FragmentLauncher mFragmentLauncher;
    public com.philips.platform.uid.view.widget.ProgressBar mPTHBaseFragmentProgressBar;
    private ActionBarListener actionBarListener;
    protected final int SMALL = 0;
    protected final int MEDIUM = 1;
    protected final int BIG = 2;

    @Override
    public void finishActivityAffinity() {

    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public int getContainerID() {
        return mFragmentLauncher.getParentContainerResourceID();
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
     this.mFragmentLauncher = fragmentLauncher;
    }

    public FragmentLauncher getFragmentLauncher(){
        return mFragmentLauncher;
    }

    public void setActionBarListener(ActionBarListener actionBarListener){
        this.actionBarListener = actionBarListener;
    }

    public ActionBarListener getActionBarListener(){
        return actionBarListener;
    }

    public void hideProgressBar() {
        if (mPTHBaseFragmentProgressBar != null) {
            mPTHBaseFragmentProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void addFragment(THSBaseFragment fragment, String fragmentTag, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(getContainerID(), fragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragment.setActionBarListener(getActionBarListener());
        fragmentTransaction.commit();
    }

    public void createCustomProgressBar(ViewGroup group, int size) {
        switch (size){
            case BIG:
                getContext().getTheme().applyStyle(R.style.PTHCircularPBBig, true);
                break;
            case SMALL:
                getContext().getTheme().applyStyle(R.style.PTHCircularPBSmall, true);
                break;
            case MEDIUM:
                getContext().getTheme().applyStyle(R.style.PTHCircularPBMedium, true);
                break;
            default:
                getContext().getTheme().applyStyle(R.style.PTHCircularPBMedium, true);
                break;
        }

        mPTHBaseFragmentProgressBar = new ProgressBar(getContext(), null, R.attr.pth_cirucular_pb);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPTHBaseFragmentProgressBar.setLayoutParams(params);
        group.addView(mPTHBaseFragmentProgressBar);

        if (mPTHBaseFragmentProgressBar != null) {
            mPTHBaseFragmentProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void showToast(String message){
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
