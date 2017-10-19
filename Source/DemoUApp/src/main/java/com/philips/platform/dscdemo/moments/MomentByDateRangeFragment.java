package com.philips.platform.dscdemo.moments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.core.datatypes.DSPagination;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MomentByDateRangeFragment  extends DSBaseFragment
		implements DBFetchRequestListner<Moment>, DBRequestListener<Moment>, DBChangeListener , DSPagination{

	private Context mContext;
	private DataServicesManager mDataServicesManager;
	private RecyclerView mMomentsRecyclerView;

	private MomentPresenter mMomentPresenter;
	private MomentAdapter mMomentAdapter;
	private ArrayList<? extends Moment> mMomentList = new ArrayList();
	private String mMomentType;
	private String mStartDate;
	private String mEndDate;

	private EditText mMomentTypeEt;
	private EditText mMomentStartDateEt;
	private EditText mMomentEndDateEt;
	private TextView mNoMomentInDateRange;
	 Calendar myCalendar;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMomentPresenter = new MomentPresenter(mContext, this);
		mDataServicesManager = DataServicesManager.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.moment_by_daterange,container,false);
		mMomentTypeEt = (EditText) view.findViewById(R.id.et_moment_type);
		mMomentStartDateEt = (EditText) view.findViewById(R.id.et_moment_startDate);
		mMomentEndDateEt = (EditText) view.findViewById(R.id.et_moment_endDate);

		mMomentAdapter = new MomentAdapter(getContext(), mMomentList, mMomentPresenter, false);
		mNoMomentInDateRange = (TextView) view.findViewById(R.id.tv_no_latest_moment);
		mMomentsRecyclerView = (RecyclerView) view.findViewById(R.id.moment_dateRange_list);
		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
		mMomentsRecyclerView.setLayoutManager(layoutManager);
		mMomentsRecyclerView.setAdapter(mMomentAdapter);

		mMomentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeChanged(int positionStart, int itemCount) {
				super.onItemRangeChanged(positionStart, itemCount);
				if (itemCount == 0) {
					mNoMomentInDateRange.setVisibility(View.VISIBLE);
				}
			}
		});

		Button mOkBtn = (Button) view.findViewById(R.id.btn_fetch);
		mOkBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mMomentType = mMomentTypeEt.getText().toString();
                mStartDate = mMomentStartDateEt.getText().toString();
                mEndDate = mMomentEndDateEt.getText().toString();
				fetchLatestMomentWithDateRange();
			}
		});

		myCalendar = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
			                      int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateStartDate();
			}
		};

		final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
			                      int dayOfMonth) {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateEndDate();
			}
		};
		mMomentStartDateEt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(getActivity(), startDate, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		mMomentEndDateEt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new DatePickerDialog(getActivity(), endDate, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});

		return view;
	}

	private void updateStartDate() {
		String myFormat = "MM/dd/yy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		mMomentStartDateEt.setText(sdf.format(myCalendar.getTime()));
	}

	private void updateEndDate() {
		String myFormat = "MM/dd/yy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
		mMomentEndDateEt.setText(sdf.format(myCalendar.getTime()));
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

	private void fetchLatestMomentWithDateRange() {
		if (mMomentType != null && !mMomentType.isEmpty()) {
			mMomentPresenter.fetchLatestMoment(mMomentType.trim(), this);
		} else {
			Toast.makeText(mContext, "Please enter the valid moment type", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public String getOrderBy() {
		return null;
	}

	@Override
	public int getPageNumber() {
		return 0;
	}

	@Override
	public int getPageLimit() {
		return 0;
	}

	@Override
	public void setOrderBy(String orderBy) {

	}

	@Override
	public void setPageNumber(int pageNumber) {

	}

	@Override
	public void setPageLimit(int pageLimit) {

	}

	@Override
	public DSPaginationOrdering getOrdering() {
		return null;
	}

	@Override
	public void setOrdering(DSPaginationOrdering paginationOrdering) {

	}
}
