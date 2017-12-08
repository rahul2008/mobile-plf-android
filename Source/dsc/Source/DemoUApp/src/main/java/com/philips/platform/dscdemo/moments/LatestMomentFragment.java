/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.moments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;

import java.util.ArrayList;
import java.util.List;

public class LatestMomentFragment extends DSBaseFragment
        implements DBFetchRequestListner<Moment>, DBRequestListener<Moment>, DBChangeListener {

    private Context mContext;
    private DataServicesManager mDataServicesManager;

    private EditText mMomentTypeEt;
    private TextView mNoActiveLatestMoment;
    private RecyclerView mMomentsRecyclerView;

    private MomentPresenter mMomentPresenter;
    private MomentAdapter mMomentAdapter;
    private ArrayList<? extends Moment> mMomentList = new ArrayList();
    private String mMomentType;

    @Override
    public int getActionbarTitleResId() {
        return 0;
    }

    @Override
    public String getActionbarTitle() {
        return null;
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMomentPresenter = new MomentPresenter(mContext, this);
        mDataServicesManager = DataServicesManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.latest_moment, container, false);
        mMomentAdapter = new MomentAdapter(getContext(), mMomentList, mMomentPresenter, false);

        mMomentTypeEt = (EditText) view.findViewById(R.id.et_moment_type);
        mNoActiveLatestMoment = (TextView) view.findViewById(R.id.tv_no_latest_moment);
        mMomentsRecyclerView = (RecyclerView) view.findViewById(R.id.latest_moment_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMomentsRecyclerView.setLayoutManager(layoutManager);
        mMomentsRecyclerView.setAdapter(mMomentAdapter);

        mMomentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                if (itemCount == 0) {
                    mNoActiveLatestMoment.setVisibility(View.VISIBLE);
                }
            }
        });

        Button mOkBtn = (Button) view.findViewById(R.id.btn_fetch);
        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMomentType = mMomentTypeEt.getText().toString();
                fetchLatestMomentWithType();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDataServicesManager.registerDBChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDataServicesManager.unRegisterDBChangeListener();
    }

    @Override
    public void onFetchSuccess(final List<? extends Moment> data) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI(data);
            }
        });
    }

    @Override
    public void onFetchFailure(Exception exception) {
        showErrorOnFailure(exception);
    }

    @Override
    public void onSuccess(List<? extends Moment> data) {
        fetchLatestMomentWithType();
    }

    @Override
    public void onFailure(Exception exception) {
        showErrorOnFailure(exception);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        if (type != SyncType.MOMENT) return;
        fetchLatestMomentWithType();
    }

    @Override
    public void dBChangeFailed(final Exception e) {
        showErrorOnFailure(e);
    }

    public void updateUI(final List<? extends Moment> moments) {
        if (getActivity() != null && moments != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mMomentList = (ArrayList<? extends Moment>) moments;
                    if (mMomentList.size() > 0) {
                        mMomentsRecyclerView.setVisibility(View.VISIBLE);
                        mNoActiveLatestMoment.setVisibility(View.GONE);

                        mMomentAdapter.setData(mMomentList);
                        mMomentAdapter.notifyDataSetChanged();
                    } else {
                        mMomentsRecyclerView.setVisibility(View.GONE);
                        mNoActiveLatestMoment.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    private void showErrorOnFailure(final Exception exception) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fetchLatestMomentWithType() {
        if (mMomentType != null && !mMomentType.isEmpty()) {
            mMomentPresenter.fetchLatestMoment(mMomentType.trim(), this);
        } else {
            Toast.makeText(mContext, "Please enter the valid moment type", Toast.LENGTH_SHORT).show();
        }
    }
}
