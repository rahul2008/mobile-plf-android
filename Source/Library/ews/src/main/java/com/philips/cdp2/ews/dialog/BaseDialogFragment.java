/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.dialog;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.philips.cdp2.ews.R;

public abstract class BaseDialogFragment<T extends ViewDataBinding> extends DialogFragment {

    protected T viewDataBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO themes has to be dynamically passed
        //setStyle(STYLE_NO_FRAME, R.style.Theme_DLS_Blue_VeryDark);
    }

    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        setCancelable(false);
        bindViewModel(viewDataBinding);
        return viewDataBinding.getRoot();
    }

    protected abstract void bindViewModel(final T viewDataBinding);

    protected abstract int getLayoutId();


    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        @SuppressWarnings("ConstantConditions")
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.90), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    @Override
    public void show(final FragmentManager manager, final String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
