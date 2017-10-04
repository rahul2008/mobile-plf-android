/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.navigation.ScreenFlowParticipant;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.view.dialog.CancelDialogFragment;

public abstract class EWSBaseFragment<T extends ViewDataBinding> extends Fragment implements ScreenFlowParticipant {

    protected T viewDataBinding;

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        injectDependencies();
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        bindViewModel(viewDataBinding);

        setHasOptionsMenu(hasMenu());

        return viewDataBinding.getRoot();
    }

    protected boolean hasMenu() {
        return true;
    }

    @CallSuper
    @Override
    public void onResume() {
        super.onResume();
        EWSTagger.trackPage(getPageName());
    }

    @NonNull
    protected abstract String getPageName();

    private void injectDependencies() {
        inject(((EWSActivity) getActivity()).getEWSComponent());
    }

    protected abstract void bindViewModel(final T viewDataBinding);

    protected abstract void inject(final EWSComponent ewsComponent);

    @LayoutRes
    protected abstract int getLayoutId();

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(getMenuLayoutId(), menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.skip_setup) {
            showCancelSetupDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showCancelSetupDialog() {
        new CancelDialogFragment().show(getChildFragmentManager(), "cancelDialog");
    }

    protected int getMenuLayoutId() {
        return R.menu.ews_menu;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getNavigationIconId() {
        return ScreenFlowController.NAVIGATION_BACK_ICON;
    }

    @StringRes
    @Override
    public int getToolbarTitle() {
        return R.string.ews_title;
    }
}