package com.philips.cdp2.ews.common.callbacks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.common.util.DateUtil;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class DialogHelper {

    @NonNull private final Fragment fragment;

    public DialogHelper(@NonNull Fragment fragment) {
        this.fragment = fragment;
    }

    public void showTroubleshootHomeWifi() {
        Context context = fragment.getActivity();
        View view = LayoutInflater.from(context).inflate(R.layout.troubleshoot_home_wifi_fragment,
                null, false);

        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(context)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(true);
        final AlertDialogFragment alertDialogFragment = builder.create();
        alertDialogFragment.show(fragment.getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());

        TextView textView = (TextView) view.findViewById(R.id.label_ews_home_network_body);
        ImageView imageView = (ImageView) view.findViewById(R.id.ic_close);
        String explanation = String.format(DateUtil.getSupportedLocale(), context.getString(R.string.label_ews_home_network_body), context.getString(R.string.af_app_name));
        textView.setText(TextUtil.getHTMLText(explanation));
        FontIconDrawable drawable = new FontIconDrawable(context, context.getResources().getString(R.string.dls_cross_24), TypefaceUtils
                .load(context.getAssets(), "fonts/iconfont.ttf"))
                .sizeRes(R.dimen.ews_gs_icon_size).colorRes(R.color.black);
        imageView.setBackground(drawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogFragment.dismissAllowingStateLoss();
            }
        });
    }

}
