/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.characteristics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.characteristics.UCoreCharacteristics;
import com.philips.platform.datasync.characteristics.UCoreUserCharacteristics;
import com.philips.platform.dscdemo.DSBaseFragment;
import com.philips.platform.dscdemo.R;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CharacteristicsFragment extends DSBaseFragment
        implements View.OnClickListener, DBFetchRequestListner<Characteristics>, DBRequestListener<Characteristics>, DBChangeListener {
    private Context mContext;
    private Button mBtnEdit;
    private EditText mEtCharacteristics;
    private CharacteristicsPresenter mCharacteristicsDialogPresenter;
    private boolean isEditable;

    @Override
    public int getActionbarTitleResId() {
        return R.string.characteristics_title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.characteristics_title);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_user_characteristics, container, false);

        Button mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);

        mBtnEdit = (Button) rootView.findViewById(R.id.btnEdit);
        mBtnEdit.setOnClickListener(this);

        Button mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);

        mEtCharacteristics = (EditText) rootView.findViewById(R.id.et_characteristics);

        mCharacteristicsDialogPresenter = new CharacteristicsPresenter(this);

        mEtCharacteristics.setEnabled(false);
        isEditable = false;
        DataServicesManager.getInstance().fetchUserCharacteristics(this);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        DataServicesManager.getInstance().registerDBChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnOK) {
            isEditable = false;
            String userCharacteristics = mEtCharacteristics.getText().toString().trim();
            if (!userCharacteristics.trim().isEmpty()) {
                boolean isUpdated = mCharacteristicsDialogPresenter.createOrUpdateCharacteristics(userCharacteristics);
                if (!isUpdated) {
                    Toast.makeText(mContext, "Please enter valid input", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Please enter valid input", Toast.LENGTH_SHORT).show();
            }
            getFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        } else if (i == R.id.btnCancel) {
            getFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        } else if (i == R.id.btnEdit) {
            isEditable = true;
            mBtnEdit.setEnabled(false);
            mEtCharacteristics.setEnabled(true);
            mEtCharacteristics.setFocusable(true);
        }
    }

    @Override
    public void onSuccess(List<? extends Characteristics> data) {
        refreshUi(data);
    }

    private void refreshUi(List<? extends Characteristics> data) {
        if (data == null) return;
        if (getActivity() == null) return;

        final ArrayList<OrmCharacteristics> ormCharacteristicsList = (ArrayList<OrmCharacteristics>) data;

        final List<Characteristics> parentList = new ArrayList<>();
        for (Characteristics characteristics : ormCharacteristicsList) {
            if (ormCharacteristicsList.size() > 0) {
                parentList.add(characteristics);
            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    UCoreUserCharacteristics uCoreCharacteristics = convertToUCoreUserCharacteristics(parentList);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonObj = gson.toJson(uCoreCharacteristics);
                    mEtCharacteristics.setText(jsonObj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Application data type to DataCore type
    public UCoreUserCharacteristics convertToUCoreUserCharacteristics(List<Characteristics> characteristic) {
        UCoreUserCharacteristics uCoreUserCharacteristics = new UCoreUserCharacteristics();
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        if (characteristic != null) {
            for (int i = 0; i < characteristic.size(); i++) {
                Collection<? extends Characteristics> characteristicsDetails = characteristic;
                List<Characteristics> detailList = new ArrayList<>(characteristicsDetails);
                if (detailList.size() > 0) {
                    UCoreCharacteristics uCoreCharacteristics1 = new UCoreCharacteristics();
                    uCoreCharacteristics1.setType(detailList.get(i).getType());
                    uCoreCharacteristics1.setValue(detailList.get(i).getValue());
                    Collection<? extends Characteristics> characteristicsDetail = detailList.get(i).getCharacteristicsDetail();
                    List<Characteristics> detailList1 = new ArrayList<>(characteristicsDetail);
                    uCoreCharacteristics1.setCharacteristics(convertToUCoreCharacteristics(detailList1));
                    uCoreCharacteristicsList.add(uCoreCharacteristics1);
                }
            }
        }
        uCoreUserCharacteristics.setCharacteristics(uCoreCharacteristicsList);
        return uCoreUserCharacteristics;
    }

    private List<UCoreCharacteristics> convertToUCoreCharacteristics(List<Characteristics> characteristicses) {
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        if (characteristicses.size() > 0) {
            for (int i = 0; i < characteristicses.size(); i++) {
                Collection<? extends Characteristics> characteristicsDetail1 = characteristicses.get(i).getCharacteristicsDetail();
                List<Characteristics> characteristicsList = new ArrayList<>(characteristicsDetail1);
                UCoreCharacteristics characteristicsDetail = new UCoreCharacteristics();
                characteristicsDetail.setType(characteristicses.get(i).getType());
                characteristicsDetail.setValue(characteristicses.get(i).getValue());
                characteristicsDetail.setCharacteristics(convertToUCoreCharacteristics(characteristicsList));
                uCoreCharacteristicsList.add(characteristicsDetail);
            }
        }
        return uCoreCharacteristicsList;
    }

    @Override
    public void onFailure(final Exception exception) {
        refreshOnFailure(exception);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        if (type != SyncType.CHARACTERISTICS) return;
        if (!isEditable) {
            DataServicesManager.getInstance().fetchUserCharacteristics(this);
        }
    }

    @Override
    public void dBChangeFailed(Exception e) {

    }

    @Override
    public void onFetchSuccess(List<? extends Characteristics> data) {
        refreshUi(data);
    }

    @Override
    public void onFetchFailure(final Exception exception) {
        refreshOnFailure(exception);
    }

    private void refreshOnFailure(final Exception exception) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
