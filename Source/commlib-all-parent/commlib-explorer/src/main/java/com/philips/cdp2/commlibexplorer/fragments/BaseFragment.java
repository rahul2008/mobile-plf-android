/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.commlibexplorer.CommlibExplorerApp;
import com.philips.cdp2.commlibexplorer.MainActivity;
import com.philips.cdp2.commlibexplorer.background.BackgroundConnectionService;

abstract public class BaseFragment extends Fragment {
    protected MainActivity getMainActivity() {
        return (MainActivity) this.getActivity();
    }

    protected CommlibExplorerApp getApp() {
        return (CommlibExplorerApp) getMainActivity().getApplication();
    }

    protected BackgroundConnectionService getConnectionService() {
        return getMainActivity().getBoundService();
    }

    @Nullable
    @Override
    final public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(getLayoutId(), container, false);
        setupFragmentView(fragmentView);
        return fragmentView;
    }

    @LayoutRes
    abstract protected int getLayoutId();

    abstract void setupFragmentView(View fragmentView);
}
