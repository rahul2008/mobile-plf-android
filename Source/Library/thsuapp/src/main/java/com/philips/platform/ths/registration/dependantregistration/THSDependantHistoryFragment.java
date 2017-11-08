/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;


import static com.philips.platform.ths.utility.THSConstants.THS_SELECT_PATIENT;

public class THSDependantHistoryFragment extends THSPracticeFragment implements OnItemClickListener, View.OnClickListener {
    public static final String TAG = THSDependantHistoryFragment.class.getSimpleName();

    private RecyclerView mPracticeRecyclerView;
    private THSDependentListAdapter thsDependentListAdapter;
    private ActionBarListener actionBarListener;
    protected THSDependentPresenter mThsDependentPresenter;
    private RelativeLayout mParentContainer;
    protected Label mLabelParentName,visitForLabel,choose_person;
    private ImageView mImageViewLogo;
    protected int mLaunchInput = -1;
    private RelativeLayout mRelativeLayoutContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_dependant_list, container, false);

        mRelativeLayoutContainer = (RelativeLayout) view.findViewById(R.id.activity_main);

        if(getArguments()!=null) {
            mLaunchInput = getArguments().getInt(THSConstants.THS_LAUNCH_INPUT);
        }

        thsDependentListAdapter = new THSDependentListAdapter(getContext());
        visitForLabel = (Label)view.findViewById(R.id.ths_visit_for);
        choose_person = (Label) view.findViewById(R.id.choose_person);
        mPracticeRecyclerView = (RecyclerView)view.findViewById(R.id.ths_recycler_view_dependent_list);
        mParentContainer = (RelativeLayout) view.findViewById(R.id.ths_parent_container);
        mParentContainer.setOnClickListener(this);

        mLabelParentName = (Label) view.findViewById(R.id.ths_parent_name);
        mLabelParentName.setText(THSManager.getInstance().getThsParentConsumer(getContext()).getFirstName());

        mImageViewLogo = (ImageView) view.findViewById(R.id.ths_parent_logo);
        showProfilePic(THSManager.getInstance().getThsParentConsumer(getContext()));

        mParentContainer.setOnClickListener(this);
        mThsDependentPresenter = new THSDependentPresenter(this);
        mPracticeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mThsDependentPresenter.updateDependents();
        showDependentList();
        updateUIBasedOnLaunchInput();
        return view;
    }

    protected void updateUIBasedOnLaunchInput() {
        switch (mLaunchInput) {
            case THSConstants.THS_PRACTICES:
                visitForLabel.setText(R.string.ths_visit_for);
                choose_person.setText(R.string.ths_select_name);
                break;
            case THSConstants.THS_SCHEDULED_VISITS:
                visitForLabel.setText(R.string.ths_select_patient);
                choose_person.setText(R.string.ths_select_patient_detail_appointment_text);
                break;
            case THSConstants.THS_VISITS_HISTORY:
                visitForLabel.setText(R.string.ths_select_patient);
                choose_person.setText(R.string.ths_select_patient_detail_visit_text);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_SELECT_PATIENT,null,null);
        actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(getString(R.string.ths_select_patient),true);
        }
    }

    public void showDependentList(){
        thsDependentListAdapter.setOnItemClickListener(this);
        mPracticeRecyclerView.setAdapter(thsDependentListAdapter);
    }

    @Override
    public void onItemClick(THSConsumer thsConsumer) {
        launchRequestedInput(thsConsumer);
    }

    @Override
    public void onClick(View view) {
        int resId = view.getId();
        if(resId == R.id.ths_parent_container){
            final THSConsumer thsConsumer = THSManager.getInstance().getThsParentConsumer(getContext());
            THSManager.getInstance().setThsConsumer(THSManager.getInstance().getThsConsumer(getContext()));
            launchRequestedInput(thsConsumer);
        }
    }

    private void launchRequestedInput(THSConsumer thsConsumer) {
        THSManager.getInstance().setThsConsumer(thsConsumer);
        mThsDependentPresenter.checkIfUserExists();
    }

    protected void showProfilePic(THSConsumer thsConsumer) {
        if(thsConsumer.getProfilePic()!= null){
            try {
                Bitmap b = BitmapFactory.decodeStream(thsConsumer.getProfilePic());
                b.setDensity(Bitmap.DENSITY_NONE);
                Drawable d = new BitmapDrawable(getContext().getResources(),b);
                mImageViewLogo.setImageDrawable(d);
            }catch (Exception e){
                mImageViewLogo.setImageResource(R.mipmap.child_icon);
            }
        }else {
            mImageViewLogo.setImageResource(R.mipmap.child_icon);
        }
    }
}