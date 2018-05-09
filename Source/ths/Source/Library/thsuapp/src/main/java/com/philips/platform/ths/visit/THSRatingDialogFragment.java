package com.philips.platform.ths.visit;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.philips.platform.ths.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.RatingBar;


public class THSRatingDialogFragment extends DialogFragment implements android.widget.RatingBar.OnRatingBarChangeListener {
    public RatingBar providerInputRatingBar;
    public RatingBar visitInputRatingBar;
    public Button okButton;

    public void setThsVisitSummaryPresenter(THSVisitSummaryPresenter thsVisitSummaryPresenter) {
        this.thsVisitSummaryPresenter = thsVisitSummaryPresenter;
    }

    THSVisitSummaryPresenter thsVisitSummaryPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(this.getContext()));
        View view = layoutInflater.inflate(R.layout.ths_rating, container, false);
        providerInputRatingBar = view.findViewById(R.id.ths_provider_ratingbar);
        visitInputRatingBar = view.findViewById(R.id.ths_visit_ratingbar);
        providerInputRatingBar.setOnRatingBarChangeListener(this);
        visitInputRatingBar.setOnRatingBarChangeListener(this);
        okButton = view.findViewById(R.id.ths_ratingbar_ok_button);
        okButton.setEnabled(false);
        setOkButtonState();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thsVisitSummaryPresenter.sendRatings(providerInputRatingBar.getRating(),
                        visitInputRatingBar.getRating());
                dismiss();
            }
        });
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;


    }

    private void setOkButtonState() {
        float providerRatingValue = providerInputRatingBar.getRating();
        float visitRatingValue = visitInputRatingBar.getRating();
        if (providerRatingValue > 0 && visitRatingValue > 0) {
            okButton.setEnabled(true);
        } else {
            okButton.setEnabled(false);
        }
    }


    @Override
    public void onRatingChanged(android.widget.RatingBar ratingBar, float v, boolean b) {
        int viewID = ratingBar.getId();
        if (viewID == R.id.ths_provider_ratingbar) {
            setOkButtonState();
        } else if (viewID == R.id.ths_visit_ratingbar) {
            setOkButtonState();
        }

    }
}
