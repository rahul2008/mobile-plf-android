/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.CircularImageView;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RatingBar;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.philips.platform.ths.utility.THSConstants.THS_WELCOME_BACK;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;

@SuppressWarnings("serial")
public class THSWelcomeBackFragment extends THSBaseFragment implements View.OnClickListener {

    public static final String TAG = THSWelcomeBackFragment.class.getSimpleName();
    Label mArrow;
    Label mLabel;
    Button mBtnGetStarted;
    protected THSWelcomeBackPresenter mThsWelcomeBackPresenter;
    private PracticeInfo mPracticeInfo;
    private Provider mProvider;
    private RatingBar mRatingBar;
    private CircularImageView mImageProvider;
    private Label mLabelProviderName;
    private Label mLabelPracticeName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_welcome_back_fragment, container, false);
        mThsWelcomeBackPresenter = new THSWelcomeBackPresenter(this);

        mLabel = (Label) view.findViewById(R.id.ths_appointment_time);
        mArrow = (Label) view.findViewById(R.id.ths_arrow);
        mArrow.setVisibility(View.GONE);
        mBtnGetStarted = (Button) view.findViewById(R.id.ths_get_started);
        mBtnGetStarted.setOnClickListener(this);
        mImageProvider = (CircularImageView) view.findViewById(R.id.ths_providerImage);
        mLabelProviderName = (Label) view.findViewById(R.id.providerNameLabel);
        mLabelPracticeName = (Label) view.findViewById(R.id.practiceNameLabel);

        mRatingBar = (RatingBar) view.findViewById(R.id.providerRatingValue);
        mRatingBar.setVisibility(View.VISIBLE);

        final Bundle arguments = getArguments();
        if(arguments!=null) {
            Long date = arguments.getLong(THSConstants.THS_DATE);
            mPracticeInfo = arguments.getParcelable(THSConstants.THS_PRACTICE_INFO);
            mProvider = arguments.getParcelable(THSConstants.THS_PROVIDER);

            final String format = new SimpleDateFormat(THSConstants.TIME_FORMATTER, Locale.getDefault()).format(date);
            String text = getString(R.string.ths_appointment_start_time, format);

            mLabel.setText(text);
            mRatingBar.setRating(mProvider.getRating());

            mLabelProviderName.setText(mProvider.getFullName());
            mLabelPracticeName.setText(mPracticeInfo.getName());

            if (mProvider.hasImage()) {
                try {
                    THSManager.getInstance().getAwsdk(mImageProvider.getContext()).
                            getPracticeProvidersManager().
                            newImageLoader(mProvider,
                                    mImageProvider, ProviderImageSize.LARGE).placeholder
                            (mImageProvider.getResources().getDrawable(R.drawable.doctor_placeholder, getActivity().getTheme())).
                            build().load();
                } catch (AWSDKInstantiationException e) {

                }
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBarListener actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(getString(R.string.ths_welcome_back),true);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.ths_get_started){
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "videoVisitStarted");
            mThsWelcomeBackPresenter.onEvent(R.id.ths_get_started);
        }
    }

    public PracticeInfo getPracticeInfo() {
        return mPracticeInfo;
    }

    public Provider getProvider() {
        return mProvider;
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_WELCOME_BACK,null,null);
    }
}
