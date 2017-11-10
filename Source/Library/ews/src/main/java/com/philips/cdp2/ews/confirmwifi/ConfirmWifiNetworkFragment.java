/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.confirmwifi;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.databinding.FragmentConfirmWifiNetworkBinding;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.util.ColorsUtil;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.Locale;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class ConfirmWifiNetworkFragment extends BaseFragment
        implements ConfirmWifiNetworkViewModel.ViewCallback {

    @VisibleForTesting
    public ConfirmWifiNetworkViewModel viewModel;


    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentConfirmWifiNetworkBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_wifi_network, container, false);
        viewModel = createViewModel();
        viewModel.setViewCallback(this);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @VisibleForTesting
    @NonNull
    private ConfirmWifiNetworkViewModel createViewModel() {
        return EWSDependencyProvider.getInstance().getEwsComponent().confirmWifiNetworkViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }

    @Override
    public void onDestroyView() {
        viewModel.setViewCallback(null);
        super.onDestroyView();
    }

    @Override
    public void showTroubleshootHomeWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration) {
        if (getChildFragmentManager().findFragmentByTag(AlertDialogFragment.class.getCanonicalName()) == null) {
            Context context = getContext();
            final View view = LayoutInflater.from(context).inflate(R.layout.troubleshoot_home_wifi_fragment,
                    null, false);

            AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(context)
                    .setDialogView(view)
                    .setDialogType(DialogConstants.TYPE_DIALOG)
                    .setDimLayer(DialogConstants.DIM_STRONG)
                    .setCancelable(false);
            final AlertDialogFragment alertDialogFragment = builder.create();
            alertDialogFragment.show(getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());
            getChildFragmentManager().executePendingTransactions();
            TextView textView = view.findViewById(R.id.label_ews_home_network_body);
            ImageView imageView = view.findViewById(R.id.ic_close);
            String explanation = String.format(Locale.getDefault(), context.getString(R.string.label_ews_home_network_body),
                    context.getString(baseContentConfiguration.getAppName()));
            textView.setText(TextUtil.getHTMLText(explanation));
            FontIconDrawable drawable = new FontIconDrawable(context, context.getResources().getString(R.string.dls_cross_24), TypefaceUtils
                    .load(context.getAssets(), "fonts/iconfont.ttf"))
                    .sizeRes(R.dimen.ews_gs_icon_size).color(ColorsUtil.getAttributeColor(context, R.attr.uidContentItemPrimaryNormalIconColor));
            imageView.setBackground(drawable);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogFragment.dismiss();
                    getChildFragmentManager().popBackStackImmediate();
                    viewModel.refresh();
                }
            });
        }
    }


}
