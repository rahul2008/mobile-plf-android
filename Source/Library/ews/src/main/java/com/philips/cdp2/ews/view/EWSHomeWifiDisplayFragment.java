/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.common.util.DateUtil;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.databinding.FragmentEwsHomeWifiDisplayScreenBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.cdp2.ews.viewmodel.EWSHomeWifiDisplayViewModel;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.logging.Handler;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class EWSHomeWifiDisplayFragment extends EWSBaseFragment<FragmentEwsHomeWifiDisplayScreenBinding>
        implements EWSHomeWifiDisplayViewModel.ViewCallback {

    @Inject
    EWSHomeWifiDisplayViewModel viewModel;

    private AlertDialogFragment alertDialogFragment;

    @Override
    public int getHierarchyLevel() {
        return 2;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }

    @Override
    protected void bindViewModel(final FragmentEwsHomeWifiDisplayScreenBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewModel.setViewCallback(this);
    }

    @Override
    public void onDestroyView() {
        viewModel.setViewCallback(null);
        super.onDestroyView();
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ews_home_wifi_display_screen;
    }

    @NonNull
    @Override
    protected String getPageName() {
        return Pages.CONFIRM_WIFI;
    }

    @Override
    public void showTroubleshootHomeWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration) {
        Context context = getContext();

        if (alertDialogFragment == null) {
            AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(context)
                    .setDialogLayout(R.layout.troubleshoot_home_wifi_fragment)
                    .setDialogType(DialogConstants.TYPE_DIALOG)
                    .setDimLayer(DialogConstants.DIM_STRONG)
                    .setCancelable(false);
            alertDialogFragment = builder.create();
        }


        if (alertDialogFragment.isAdded()) {
            alertDialogFragment.getDialog().show();
        } else {
            alertDialogFragment.show(getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());
            getChildFragmentManager().executePendingTransactions();
            View view = alertDialogFragment.getView();
            TextView textView = (TextView) view.findViewById(R.id.label_ews_home_network_body);
            ImageView imageView = (ImageView) view.findViewById(R.id.ic_close);
            String explanation = String.format(DateUtil.getSupportedLocale(), context.getString(R.string.label_ews_home_network_body),
                    context.getString(baseContentConfiguration.getAppName()));
            textView.setText(TextUtil.getHTMLText(explanation));
            FontIconDrawable drawable = new FontIconDrawable(context, context.getResources().getString(R.string.dls_cross_24), TypefaceUtils
                    .load(context.getAssets(), "fonts/iconfont.ttf"))
                    .sizeRes(R.dimen.ews_gs_icon_size).colorRes(R.color.black);
            imageView.setBackground(drawable);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogFragment.getDialog().dismiss();
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.refresh();
                        }
                    }, 500);
                }
            });
        }
    }
}
