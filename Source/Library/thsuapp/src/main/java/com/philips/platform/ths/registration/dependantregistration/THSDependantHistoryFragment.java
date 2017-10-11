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
import com.philips.platform.ths.settings.THSScheduledVisitsFragment;
import com.philips.platform.ths.settings.THSVisitHistoryFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;

public class THSDependantHistoryFragment extends THSPracticeFragment implements OnItemClickListener, View.OnClickListener {
    public static final String TAG = THSDependantHistoryFragment.class.getSimpleName();

    private RecyclerView mPracticeRecyclerView;
    private THSDependentListAdapter thsDependentListAdapter;
    private ActionBarListener actionBarListener;
    private THSDependentPresenter mThsDependentPresenter;
    private RelativeLayout mParentContainer;
    private Label mLabelParentName;
    private ImageView mImageViewLogo;
    private int mLaunchInput = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_dependant_list, container, false);

        mLaunchInput = getArguments().getInt(THSConstants.THS_LAUNCH_INPUT);

        thsDependentListAdapter = new THSDependentListAdapter(getContext());
        mPracticeRecyclerView = (RecyclerView)view.findViewById(R.id.ths_recycler_view_dependent_list);
        mParentContainer = (RelativeLayout) view.findViewById(R.id.ths_parent_container);
        mParentContainer.setOnClickListener(this);

        mLabelParentName = (Label) view.findViewById(R.id.ths_parent_name);
        mLabelParentName.setText(THSManager.getInstance().getThsConsumer().getFirstName());

        mImageViewLogo = (ImageView) view.findViewById(R.id.ths_parent_logo);
        showProfilePic(THSManager.getInstance().getThsConsumer());

        mParentContainer.setOnClickListener(this);
        mThsDependentPresenter = new THSDependentPresenter(this);
        mPracticeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        showDependentList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
            launchRequestedInput(THSManager.getInstance().getThsConsumer());
        }
    }

    private void launchRequestedInput(THSConsumer thsConsumer) {
        switch (mLaunchInput){
            case THSConstants.THS_SCHEDULED_VISITS:
                addFragment(new THSScheduledVisitsFragment(), THSScheduledVisitsFragment.TAG, null, false);
                break;
            case THSConstants.THS_VISITS_HISTORY:
                addFragment(new THSVisitHistoryFragment(), THSScheduledVisitsFragment.TAG, null, false);
                break;
            case THSConstants.THS_PRACTICES:
                addFragment(new THSPracticeFragment(), THSPracticeFragment.TAG, null, false);
                break;
        }
    }

    protected void showProfilePic(THSConsumer thsConsumer) {
        if(thsConsumer.getProfilePic()!= null){
            Bitmap b = BitmapFactory.decodeStream(thsConsumer.getProfilePic());
            b.setDensity(Bitmap.DENSITY_NONE);
            Drawable d = new BitmapDrawable(getContext().getResources(),b);
            mImageViewLogo.setImageDrawable(d);
        }else {
            mImageViewLogo.setImageResource(R.mipmap.child_icon);
        }
    }
}