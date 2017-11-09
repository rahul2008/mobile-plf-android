package com.philips.platform.ths.visit;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.settings.THSVisitHistoryDetailPresenter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_HIPAA_PRIVACY_NOTICE;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_RESPONSE_OK;
import static com.philips.platform.ths.utility.THSConstants.THS_IN_APP_NOTIFICATION;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;

public class THSDownloadReportPrivacyNoticeFragment extends DialogFragment implements View.OnClickListener {
    public static final String TAG = THSDownloadReportPrivacyNoticeFragment.class.getSimpleName();

    THSVisitHistoryDetailPresenter mThsVisitHistoryPresenter;


    Button downloadButton;
    Label hippaNoticeLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LayoutInflater layoutInflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(this.getContext()));
        View view = layoutInflater.inflate(R.layout.ths_download_report_privacy_notice, container, false);


        downloadButton = (Button) view.findViewById(R.id.ths_download_report_privacy_notice_button);
        downloadButton.setOnClickListener(this);

        hippaNoticeLabel = (Label) view.findViewById(R.id.ths_download_report_hippa_notice_link);

        Resources resources = getResources();
        String hippaNoticeString = resources.getString(R.string.hippa_notice);
        String hippaNoticeContainerString = String.format(resources.getString(R.string.ths_download_report_hippa_notice_link_text), hippaNoticeString);

        SpannableString hippaLinkSpannableString = new SpannableString(hippaNoticeContainerString);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        };


        int startingIndex = hippaNoticeContainerString.indexOf(hippaNoticeString);
        int endIndex = startingIndex + hippaNoticeString.length();
        hippaLinkSpannableString.setSpan(clickableSpan, startingIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        hippaNoticeLabel.setText(hippaLinkSpannableString);
        hippaNoticeLabel.setMovementMethod(LinkMovementMethod.getInstance());
        hippaNoticeLabel.setOnClickListener(this);
        //hippaNoticeLabel.setHighlightColor(Color.TRANSPARENT);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, THS_IN_APP_NOTIFICATION, "privacyNotice");
        return view;
    }

    public void setPresenter(THSVisitHistoryDetailPresenter tHSVisitHistoryDetailPresenter) {
        mThsVisitHistoryPresenter = tHSVisitHistoryDetailPresenter;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ths_download_report_privacy_notice_button) {
            //todo
            dismiss();
            THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "reportDownloadAttempted");
            THSTagUtils.tagInAppNotification(THS_ANALYTICS_HIPAA_PRIVACY_NOTICE,THS_ANALYTICS_RESPONSE_OK);
            mThsVisitHistoryPresenter.downloadReport();
        }else if(v.getId() == R.id.ths_download_report_hippa_notice_link){
            mThsVisitHistoryPresenter.onEvent(R.id.ths_download_report_hippa_notice_link);
        }
    }
}
