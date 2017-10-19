/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.dscdemo.moments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.pojo.Pagination;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MomentByDateRangeFragment extends DSBaseFragment
        implements View.OnClickListener, DBFetchRequestListner<Moment>, DBRequestListener<Moment>, DBChangeListener {

    private Context mContext;
    private RecyclerView mMomentsRecyclerView;

    private MomentPresenter mMomentPresenter;
    private MomentAdapter mMomentAdapter;
    private ArrayList<? extends Moment> mMomentList = new ArrayList();
    private String mMomentType;
    private Pagination mDSPagination;
    private int mPageLimit;
    private Date mStartDate;
    private Date mEndDate;

    private TextView mNoMomentInDateRange;
    private EditText mMomentTypeEt;
    private EditText mMomentStartDateEt;
    private EditText mMomentEndDateEt;
    private EditText mMomentOrderByEt;
    private EditText mMomentPageLimitEt;
    private Button mFetchByDateTypeBtn;
    private Button mFetchByDateRangeBtn;
    private Spinner mMomentOrdering;

    Calendar myCalendar;


    final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            try {
                updateStartDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            try {
                updateEndDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMomentPresenter = new MomentPresenter(mContext, this);
        myCalendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.moment_by_daterange, container, false);
        mMomentTypeEt = (EditText) view.findViewById(R.id.et_moment_type);
        mMomentStartDateEt = (EditText) view.findViewById(R.id.et_moment_startDate);
        mMomentEndDateEt = (EditText) view.findViewById(R.id.et_moment_endDate);
        mMomentOrderByEt = (EditText) view.findViewById(R.id.et_moment_orderBY);
        mMomentPageLimitEt = (EditText) view.findViewById(R.id.et_moment_pageLimit);
        mMomentOrdering = (Spinner) view.findViewById(R.id.momentOrdering);

        mFetchByDateTypeBtn = (Button) view.findViewById(R.id.btn_fetch_by_date_type);
        mFetchByDateRangeBtn = (Button) view.findViewById(R.id.btn_fetch_by_date_range);

        mMomentAdapter = new MomentAdapter(getContext(), mMomentList, mMomentPresenter, false);
        mNoMomentInDateRange = (TextView) view.findViewById(R.id.tv_no_latest_moment);
        mMomentsRecyclerView = (RecyclerView) view.findViewById(R.id.moment_dateRange_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mMomentsRecyclerView.setLayoutManager(layoutManager);
        mMomentsRecyclerView.setAdapter(mMomentAdapter);

        mMomentStartDateEt.setOnClickListener(this);
        mMomentEndDateEt.setOnClickListener(this);
        mFetchByDateRangeBtn.setOnClickListener(this);
        mFetchByDateTypeBtn.setOnClickListener(this);

        mMomentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                if (itemCount == 0) {
                    mNoMomentInDateRange.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void updateStartDate() throws ParseException {
        String myFormat = "MM/dd/yy";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String dateAsString = sdf.format(myCalendar.getTime());
        mMomentStartDateEt.setText(dateAsString);
        mStartDate = sdf.parse(dateAsString);
    }

    private void updateEndDate() throws ParseException {
        String myFormat = "MM/dd/yy";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String dateAsString = sdf.format(myCalendar.getTime());
        mMomentEndDateEt.setText(dateAsString);
        mEndDate = sdf.parse(dateAsString);
    }

    @Override
    public void onFetchSuccess(List<? extends Moment> data) {

    }

    @Override
    public void onFetchFailure(Exception exception) {

    }

    @Override
    public void onSuccess(List<? extends Moment> data) {

    }

    @Override
    public void onFailure(Exception exception) {

    }

    @Override
    public void dBChangeSuccess(SyncType type) {

    }

    @Override
    public void dBChangeFailed(Exception e) {

    }

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

    private void fetchMomentByDateRangeAndType() {
        if (mMomentType != null && !mMomentType.isEmpty()) {
            mMomentPresenter.fetchMomentByDateRangeAndType(mMomentType, mStartDate, mEndDate, mDSPagination, this);
        } else {
            Toast.makeText(mContext, "Please enter the valid moment type", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchMomentByDateRange() {
        if (mMomentType != null && !mMomentType.isEmpty()) {
            mMomentPresenter.fetchMomentByDateRange(mStartDate, mEndDate, mDSPagination, this);
        } else {
            Toast.makeText(mContext, "Please enter the valid moment type", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mFetchByDateTypeBtn) {
            mMomentType = mMomentTypeEt.getText().toString();
            fetchMomentByDateRangeAndType();
        } else if (v == mFetchByDateRangeBtn) {
            fetchMomentByDateRange();
        } else if (v == mMomentStartDateEt) {
            new DatePickerDialog(mContext, startDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        } else if (v == mMomentEndDateEt) {
            new DatePickerDialog(mContext, endDate, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private Pagination createPagination() {
        mDSPagination = new Pagination();
        mDSPagination.setOrderBy(mMomentOrderByEt.getText().toString().trim());
        mDSPagination.setOrdering(DSPagination.DSPaginationOrdering.ASCENDING);
        mDSPagination.setPageLimit(Integer.parseInt(mMomentPageLimitEt.getText().toString().trim()));
//		mDSPagination.setPageNumber(Integer.parseInt(mMome.getText().toString().trim()));
        return mDSPagination;
    }
}
