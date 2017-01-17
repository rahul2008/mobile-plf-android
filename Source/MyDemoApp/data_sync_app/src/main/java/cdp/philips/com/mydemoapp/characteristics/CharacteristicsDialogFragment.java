package cdp.philips.com.mydemoapp.characteristics;

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
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.characteristics.UCoreCharacteristics;
import com.philips.platform.datasync.characteristics.UCoreUserCharacteristics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.table.OrmCharacteristics;

public class CharacteristicsDialogFragment extends DialogFragment implements View.OnClickListener, DBRequestListener {
    Button mBtnOk,mBtnEdit;
    private Context mContext;
    private EditText mEtCharacteristics;
    private CharacteristicsPresenter mCharacteristicsDialogPresenter;
    private boolean isEditable ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataServicesManager.getInstance().registeredDBRequestListener(this);
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
        fetchData();
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DataServicesManager.getInstance().unRegisteredDBRequestListener();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        dialog.setTitle(getString(R.string.characteristics));
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
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
                getDialog().dismiss();
                break;
            case R.id.btnCancel:
                getDialog().dismiss();
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
    public void onSuccess(ArrayList<? extends Object> data) {
        //Display User characteristics from DB
    }


    //Application data type to DataCore type
    public UCoreUserCharacteristics convertToUCoreUserCharacteristics(List<Characteristics> characteristic) {
        UCoreUserCharacteristics uCoreUserCharacteristics = new UCoreUserCharacteristics();
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        if (characteristic != null) {
            for (int i = 0; i < characteristic.size(); i++) {
                Collection<? extends CharacteristicsDetail> characteristicsDetails = characteristic.get(i).getCharacteristicsDetails();
                List<CharacteristicsDetail> detailList = new ArrayList<>(characteristicsDetails);
               // UCoreCharacteristics uCoreCharacteristics = new UCoreCharacteristics();
                if (detailList.size() > 0) {
                    //for (int j = 0; j < detailList.size(); j++) {
                        UCoreCharacteristics uCoreCharacteristics1 = new UCoreCharacteristics();
                        uCoreCharacteristics1.setType(detailList.get(i).getType());
                        uCoreCharacteristics1.setValue(detailList.get(i).getValue());
                        Collection<? extends CharacteristicsDetail> characteristicsDetail = detailList.get(i).getCharacteristicsDetail();
                        List<CharacteristicsDetail> detailList1 = new ArrayList<>(characteristicsDetail);
                        uCoreCharacteristics1.setCharacteristics(convertToUCoreCharacteristics(detailList1));
                        uCoreCharacteristicsList.add(uCoreCharacteristics1);
                    //}
                }
            }
        }
        uCoreUserCharacteristics.setCharacteristics(uCoreCharacteristicsList);
        return uCoreUserCharacteristics;
    }

    private List<UCoreCharacteristics> convertToUCoreCharacteristics(List<CharacteristicsDetail> characteristicsDetails) {
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        if (characteristicsDetails.size() > 0) {
            for (int i = 0; i < characteristicsDetails.size(); i++) {
                Collection<? extends CharacteristicsDetail> characteristicsDetail1 = characteristicsDetails.get(i).getCharacteristicsDetail();
                List<CharacteristicsDetail> characteristicsDetailList = new ArrayList<>(characteristicsDetail1);
                UCoreCharacteristics characteristicsDetail = new UCoreCharacteristics();
                characteristicsDetail.setType(characteristicsDetails.get(i).getType());
                characteristicsDetail.setValue(characteristicsDetails.get(i).getValue());
                characteristicsDetail.setCharacteristics(convertToUCoreCharacteristics(characteristicsDetailList));
                uCoreCharacteristicsList.add(characteristicsDetail);
            }
        }
        return uCoreCharacteristicsList;
    }

    @Override
    public void onSuccess(final Object data) {
        //Display User characteristics UI
        if (data == null) return;
        if (getActivity() == null) return;
        final OrmCharacteristics ormCharacteristics = (OrmCharacteristics) data;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Characteristics> characteristicsList = new ArrayList<>();
                    characteristicsList.add(ormCharacteristics);

                    UCoreUserCharacteristics uCoreCharacteristics = convertToUCoreUserCharacteristics(characteristicsList);

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonObj = gson.toJson(uCoreCharacteristics);

                    DSLog.i(DSLog.LOG, "Inder Characteristics onSuccess= " + jsonObj);
                    mEtCharacteristics.setText(jsonObj);
                } catch (Exception e) {
                    DSLog.i(DSLog.LOG, "Inder Exception onSuccess= " + e.getMessage());
                    e.printStackTrace();
                }
            }


        });
    }

    @Override
    public void onFailure(final Exception exception) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void fetchData() {
        DSLog.i(DSLog.LOG, "Inder fetchData before editing");
        if (!isEditable) {
            DSLog.i(DSLog.LOG, "Inder fetchData editing");
            DataServicesManager.getInstance().fetchUserCharacteristics(this);
        }
    }

}
