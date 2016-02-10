package com.philips.multiproduct.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.multiproduct.homefragment.MultiProductBaseFragment;

/**
 * DirectFragment class is used as a welcome screen when CTN is not been choosen.
 *
 * @author : ritesh.jha@philips.com
 * @since : 20 Jan 2016
 */
public class DirectFragment extends MultiProductBaseFragment {

    private String TAG = DirectFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = mDummyView();
        return view;
    }


    RelativeLayout mDummyView() {

        RelativeLayout mView = new RelativeLayout(getContext());
        RelativeLayout.LayoutParams mViewParams = new RelativeLayout.LayoutParams(-1, -1);
        mViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mViewParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


        TextView mTextView = new TextView(getContext());
        mTextView.setText("If the Multi Product is one - This Screen/Logic will launch");
        RelativeLayout.LayoutParams mTextViewParams = new RelativeLayout.LayoutParams(-1, -1);
        mTextViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTextViewParams.addRule(Gravity.CENTER);

        mView.setLayoutParams(mViewParams);
        mTextView.setLayoutParams(mTextViewParams);
        mView.addView(mTextView);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public String getActionbarTitle() {
        return "Single Product";
    }


    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return "Single Product";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
