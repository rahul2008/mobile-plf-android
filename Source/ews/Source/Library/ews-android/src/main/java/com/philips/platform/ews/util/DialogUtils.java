/*
 * Copyright (c) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.dialog.EWSAlertDialogFragment;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Label;

import java.util.Locale;

public class DialogUtils {

    private static volatile DialogUtils instance;
    private static Object mutex = new Object();

    private DialogUtils() {
    }

    public static DialogUtils getInstance() {
        DialogUtils result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null)
                    instance = result = new DialogUtils();
            }
        }
        return result;
    }

    public AlertDialogFragment presentTroubleshootHomeWifiDialog(@NonNull Context context, @NonNull FragmentManager childFragmentManager, @NonNull BaseContentConfiguration baseContentConfiguration, @NonNull final EWSTagger ewsTagger) {
        EWSAlertDialogFragment alertDialogFragment = null;
        if (childFragmentManager.findFragmentByTag(AlertDialogFragment.class.getCanonicalName()) == null) {
            final View view = LayoutInflater.from(context).cloneInContext(UIDHelper.getPopupThemedContext(context)).inflate(R.layout.troubleshoot_home_wifi_fragment,
                    null, false);

            AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(context)
                    .setDialogView(view)
                    .setDialogType(DialogConstants.TYPE_DIALOG)
                    .setDimLayer(DialogConstants.DIM_STRONG)
                    .setCancelable(false);
            alertDialogFragment = (EWSAlertDialogFragment) builder.create(new EWSAlertDialogFragment());
            alertDialogFragment.setDialogLifeCycleListener(new EWSAlertDialogFragment.DialogLifeCycleListener() {
                @Override
                public void onStart() {
                    ewsTagger.trackPage(Page.SELECT_HOME_WIFI);
                }
            });
            alertDialogFragment.show(childFragmentManager, AlertDialogFragment.class.getCanonicalName());
            childFragmentManager.executePendingTransactions();
            ((Label) view.findViewById(R.id.label_ews_select_wakeup_wifi_steps_4)).setText(String.format(Locale.getDefault(),
                    context.getString(R.string.label_ews_select_wakeup_wifi_steps_4),
                    context.getString(baseContentConfiguration.getAppName())));
        }
        return alertDialogFragment;
    }
}
