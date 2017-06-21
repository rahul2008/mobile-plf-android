package com.philips.amwelluapp.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Created by philips on 6/20/17.
 */

public class PTHBaseFragment extends Fragment implements UIBaseView{


    FragmentLauncher mFragmentLauncher;
    protected ProgressBar mProgressBar;
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

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
