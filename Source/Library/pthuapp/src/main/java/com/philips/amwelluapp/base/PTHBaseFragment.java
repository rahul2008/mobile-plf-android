package com.philips.amwelluapp.base;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.amwelluapp.R;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.view.widget.ProgressBar;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;


public class PTHBaseFragment extends Fragment implements PTHBaseView {


    public FragmentLauncher mFragmentLauncher;
    public com.philips.platform.uid.view.widget.ProgressBar mPTHBaseFragmentProgressBar;
    private ActionBarListener actionBarListener;

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
    public void showProgressBar() {
        if (mPTHBaseFragmentProgressBar != null) {
            mPTHBaseFragmentProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mPTHBaseFragmentProgressBar != null) {
            mPTHBaseFragmentProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void addFragment(PTHBaseFragment fragment, String fragmentTag, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(getContainerID(), fragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragment.setActionBarListener(getActionBarListener());
        fragmentTransaction.commit();
    }

    public void createCustomProgressBar() {
        mPTHBaseFragmentProgressBar = new ProgressBar(getContext(), null, R.attr.pth_cirucular_pb);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mPTHBaseFragmentProgressBar.setLayoutParams(params);
    }

    public void showToast(String message){
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
