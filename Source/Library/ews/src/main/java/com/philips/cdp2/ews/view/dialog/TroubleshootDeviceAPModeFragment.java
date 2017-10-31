/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view.dialog;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.TroubleshootApModeViewBinding;
import com.philips.cdp2.ews.databinding.TroubleshootConnectionResetViewBinding;
import com.philips.cdp2.ews.databinding.TroubleshootEnableApModeViewBinding;
import com.philips.cdp2.ews.databinding.TroubleshootWrongPhoneConnectedViewBinding;
import com.philips.cdp2.ews.injections.EWSComponent;

public class TroubleshootDeviceAPModeFragment extends BaseDialogFragment<TroubleshootApModeViewBinding> implements OnDeviceAPModeTroubleshootListener {

    @Override
    protected void bindViewModel(final TroubleshootApModeViewBinding viewDataBinding) {
        showWrongPhoneConnectedGuidelineView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.troubleshoot_ap_mode_view;
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
    }

    @Override
    public void showWrongPhoneConnectedGuidelineView() {
        this.<TroubleshootWrongPhoneConnectedViewBinding>addView(R.layout.troubleshoot_wrong_phone_connected_view).setListener(this);
    }

    @Override
    public void showEnableAPModeGuidelineView() {
        this.<TroubleshootEnableApModeViewBinding>addView(R.layout.troubleshoot_enable_ap_mode_view).setListener(this);
    }

    @Override
    public void showFullMenuGuidelineView() {
        this.<TroubleshootWrongPhoneConnectedViewBinding>addView(R.layout.troubleshoot_wrong_phone_connected_view).setListener(this);
    }

    @Override
    public void showResetConnectionGuidelineView() {
        this.<TroubleshootConnectionResetViewBinding>addView(R.layout.troubleshoot_connection_reset_view).setListener(this);
    }

    @Override
    public void dismissAPModeTroubleshootingView() {
        this.dismissAllowingStateLoss();
    }

    @SuppressWarnings("unchecked")
    private <T extends ViewDataBinding> T addView(@LayoutRes final int layoutId) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), layoutId,
                this.viewDataBinding.apModeContainer, false);
        this.viewDataBinding.apModeContainer.removeAllViews();
        this.viewDataBinding.apModeContainer.addView(viewDataBinding.getRoot());
        return (T) viewDataBinding;
    }
}
