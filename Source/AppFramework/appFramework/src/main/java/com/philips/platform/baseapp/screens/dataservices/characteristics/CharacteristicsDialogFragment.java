package com.philips.platform.baseapp.screens.dataservices.characteristics;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmCharacteristics;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.characteristics.UCoreCharacteristics;
import com.philips.platform.datasync.characteristics.UCoreUserCharacteristics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.support.v4.app.Fragment;

import static com.philips.platform.baseapp.screens.utility.Constants.JSON_PARSING_EXCEPTION;

public class CharacteristicsDialogFragment extends Fragment implements View.OnClickListener, DBFetchRequestListner<Characteristics>,DBRequestListener<Characteristics>,DBChangeListener {
    Button mBtnOk,mBtnEdit;
    private Context mContext;
    private EditText mEtCharacteristics;
    private CharacteristicsPresenter mCharacteristicsDialogPresenter;
    private boolean isEditable ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_user_characteristics, container, false);

        mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
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
    public void onResume() {
        super.onResume();
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        DataServicesManager.getInstance().registerDBChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:
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
                break;
            case R.id.btnCancel:
                getFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.btnEdit:
                isEditable = true;
                mBtnEdit.setEnabled(false);
                mEtCharacteristics.setEnabled(true);
                mEtCharacteristics.setFocusable(true);
                break;
        }
    }

    @Override
    public void onSuccess(List<? extends Characteristics> data) {
        refreshUi(data);

    }

    private void refreshUi(List<? extends Characteristics> data) {
        //Display User characteristics from DB
        //Display User characteristics UI
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

                   // DSLog.i(DSLog.LOG, "Inder AppUserCharacteristics onSuccess= " + jsonObj);
                    mEtCharacteristics.setText(jsonObj);
                } catch (Exception e) {
                    DSLog.i(DSLog.LOG, "Inder Exception onSuccess= " + e.getMessage());
                    AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, JSON_PARSING_EXCEPTION,
                            e.getMessage());
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
                // UCoreCharacteristics uCoreCharacteristics = new UCoreCharacteristics();
                if (detailList.size() > 0) {
                    //for (int j = 0; j < detailList.size(); j++) {
                    UCoreCharacteristics uCoreCharacteristics1 = new UCoreCharacteristics();
                    uCoreCharacteristics1.setType(detailList.get(i).getType());
                    uCoreCharacteristics1.setValue(detailList.get(i).getValue());
                    Collection<? extends Characteristics> characteristicsDetail = detailList.get(i).getCharacteristicsDetail();
                    List<Characteristics> detailList1 = new ArrayList<>(characteristicsDetail);
                    uCoreCharacteristics1.setCharacteristics(convertToUCoreCharacteristics(detailList1));
                    uCoreCharacteristicsList.add(uCoreCharacteristics1);
                    //}
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
        DSLog.i(DSLog.LOG, "Inder fetchData before editing");
        if(type!=SyncType.CHARACTERISTICS)return;
        if (!isEditable) {
            DSLog.i(DSLog.LOG, "Inder fetchData editing");
            DataServicesManager.getInstance().fetchUserCharacteristics(this);
        }
    }

    @Override
    public void dBChangeFailed(Exception e) {

    }

    @Override
    public void onFetchSuccess(List<? extends Characteristics> data) {
        //Display User characteristics from DB
        //Display User characteristics UI
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
