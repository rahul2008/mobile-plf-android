package com.philips.amwelluapp.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * Created by philips on 6/20/17.
 */

public class PTHBaseFragment extends Fragment implements UIBaseView{


    FragmentLauncher mFragmentLauncher;
    protected ProgressBar mPTHBaseFragmentProgressBar;
    private ActionBarListener actionBarListener;

    @Override
    public void finishActivityAffinity() {

    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return null;
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


}
