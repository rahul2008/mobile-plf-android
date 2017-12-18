/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.connectionsuccessful;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseFragment;
import com.philips.platform.ews.common.callbacks.FragmentCallback;
import com.philips.platform.ews.databinding.FragmentConnectionSuccessfulBinding;
import com.philips.platform.ews.microapp.EWSActionBarListener;

import javax.inject.Inject;

public class ConnectionSuccessfulFragment extends BaseFragment implements
        FragmentCallback {

    @Inject
    ConnectionSuccessfulViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((EWSActionBarListener) getContext()).closeButton(false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentConnectionSuccessfulBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_connection_successful, container, false);
        viewModel = createViewModel();
        binding.setViewModel(viewModel);
        viewModel.setFragmentCallback(this);
        startImageAnimation((ImageView) binding.getRoot().findViewById(R.id.ews_04_01_pairing_successful_image));
        return binding.getRoot();
    }

    private void startImageAnimation(ImageView imageView) {
        if (imageView != null) {
            startDrawableAnimation(imageView);
        }
    }

    private void startDrawableAnimation(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    @NonNull
    private ConnectionSuccessfulViewModel createViewModel() {
        return getEWSComponent().connectionSuccessfulViewModel();
    }

    @Override
    public boolean handleBackEvent() {
        // Do nothing, back disabled in this screen
        return true;
    }

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }

    @Override
    public void finishMicroApp() {
        getActivity().finish();
    }
}