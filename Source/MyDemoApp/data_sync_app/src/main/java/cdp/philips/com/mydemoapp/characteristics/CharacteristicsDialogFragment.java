package cdp.philips.com.mydemoapp.characteristics;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.stetho.common.ArrayListAccumulator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.internal.Streams;
import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.datatypes.CharacteristicsDetail;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.characteristics.UCoreCharacteristics;
import com.philips.platform.datasync.characteristics.UCoreUserCharacteristics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.database.table.OrmCharacteristics;
import cdp.philips.com.mydemoapp.pojo.UserCharacteristics;

public class CharacteristicsDialogFragment extends DialogFragment implements View.OnClickListener, DBRequestListener {

    private Context mContext;
    private EditText mEtCharacteristics;
    private CharacteristicsDialogPresenter mCharacteristicsDialogPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataServicesManager.getInstance().registeredDBRequestListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_user_characteristics, container, false);

        Button mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);

        Button mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);

        mEtCharacteristics = (EditText) rootView.findViewById(R.id.et_characteristics);
        mCharacteristicsDialogPresenter = new CharacteristicsDialogPresenter(this);

        String mSampleJsonString = "{\n" +
                "  \"characteristics\": [\n" +
                "    {\n" +
                "      \"type\": \"User\",\n" +
                "      \"value\": \"Teja\",\n" +
                "      \"characteristics\": [\n" +
                "        {\n" +
                "          \"type\": \"Mouth\",\n" +
                "          \"value\": \"Mouth\",\n" +
                "          \"characteristics\": [\n" +
                "            {\n" +
                "              \"type\": \"hair\",\n" +
                "              \"value\": \"hair\",\n" +
                "              \"characteristics\": [\n" +
                "                {\n" +
                "                  \"type\": \"BrokenTeeth\",\n" +
                "                  \"value\": \"BrokenTeeth\",\n" +
                "                  \"characteristics\": []\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\": \"Mouth\",\n" +
                "          \"value\": \"Mouth\",\n" +
                "          \"characteristics\": [\n" +
                "            {\n" +
                "              \"type\": \"BrokenTeeth\",\n" +
                "              \"value\": \"BrokenTeeth\",\n" +
                "              \"characteristics\": []\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\": \"skin\",\n" +
                "          \"value\": \"skin\",\n" +
                "          \"characteristics\": [\n" +
                "            {\n" +
                "              \"type\": \"color\",\n" +
                "              \"value\": \"color\",\n" +
                "              \"characteristics\": []\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mEtCharacteristics.setText(mSampleJsonString);
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
                String userCharacteristics = mEtCharacteristics.getText().toString();
                if (userCharacteristics != null && !userCharacteristics.trim().isEmpty()) {
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
        List<CharacteristicsDetail> mCharacteristicsDetailList;
        if (characteristic != null) {
            for (int i = 0; i < characteristic.size(); i++) {
                mCharacteristicsDetailList = convertToCharacteristicDetail(characteristic.get(i).getCharacteristicsDetails());
                UCoreCharacteristics uCoreCharacteristics = new UCoreCharacteristics();
                uCoreCharacteristics.setType(mCharacteristicsDetailList.get(i).getType());
                uCoreCharacteristics.setValue(mCharacteristicsDetailList.get(i).getValue());
                uCoreCharacteristics.setCharacteristics(convertToUCoreCharacteristics(convertToCharacteristicDetail(mCharacteristicsDetailList.get(i).getCharacteristicsDetail())));
                uCoreCharacteristicsList.add(uCoreCharacteristics);
            }
        }
        uCoreUserCharacteristics.setCharacteristics(uCoreCharacteristicsList);
        return uCoreUserCharacteristics;
    }

    private List<UCoreCharacteristics> convertToUCoreCharacteristics(List<CharacteristicsDetail> characteristicsDetails) {
        List<UCoreCharacteristics> uCoreCharacteristicsList = new ArrayList<>();
        if (characteristicsDetails.size() > 0) {
            for (int i = 0; i < characteristicsDetails.size(); i++) {
                List<CharacteristicsDetail> characteristicsDetailList = convertToCharacteristicDetail(characteristicsDetails.get(i).getCharacteristicsDetail());
                UCoreCharacteristics characteristicsDetail = new UCoreCharacteristics();
                characteristicsDetail.setType(characteristicsDetails.get(i).getType());
                characteristicsDetail.setValue(characteristicsDetails.get(i).getValue());
                characteristicsDetail.setCharacteristics(convertToUCoreCharacteristics(characteristicsDetailList    ));
                uCoreCharacteristicsList.add(characteristicsDetail);
            }
        }
        return uCoreCharacteristicsList;
    }

    private List<CharacteristicsDetail> convertToCharacteristicDetail(Collection<? extends CharacteristicsDetail> characteristicsDetails) {
        List<CharacteristicsDetail> characteristicsDetailList = new ArrayList<>();
        if (characteristicsDetails == null)
            return null;
        for (CharacteristicsDetail detail : characteristicsDetails) {
            characteristicsDetailList.add(detail);
        }
        return characteristicsDetailList;
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

//                        JSONObject jsonObj = new JSONObject();
//                        JSONArray jsonArray1 = new JSONArray();
//
//                        for (int i = 0; i < characteristicsList.size(); i++) {
//                            Collection<? extends CharacteristicsDetail> characteristicsDetails = characteristicsList.get(i).getCharacteristicsDetails();
//                            List<CharacteristicsDetail> details = new ArrayList<>(characteristicsDetails);
//                            for (CharacteristicsDetail characteristicsDetail : details) {
//                                JSONObject jsonObj1 = new JSONObject();
//
//
//                                if (characteristicsDetail.getCharacteristicsDetail() == null) {
//                                    jsonObj1.put("type", characteristicsDetail.getType());
//                                    jsonObj1.put("value", characteristicsDetail.getValue());
//
//
//                                } else {
//                                    JSONObject jsonObj2 = new JSONObject();
//                                    JSONArray jsonArray2 = new JSONArray();
//                                    jsonObj2.put("type", characteristicsDetail.getType());
//                                    jsonObj2.put("value", characteristicsDetail.getValue());
//                                    jsonArray2.put(jsonObj2);
//                                    jsonObj1.put("characteristics", jsonArray2);
//
//                                }
//                                jsonArray1.put(jsonObj1);
//
//                            }
//                            jsonObj.put("characteristics", jsonArray1);
//                        }

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

//    private void getUserCharacteristicsFromLocalDBRecursively(List<CharacteristicsDetail> childCharacteristicsDetail) {
//        if (childCharacteristicsDetail != null && childCharacteristicsDetail.size() > 0) {
//            for (int i = 0; i < childCharacteristicsDetail.size(); i++) {
//                String type = childCharacteristicsDetail.get(i).getType();
//                String value = childCharacteristicsDetail.get(i).getValue();
//                Collection<? extends CharacteristicsDetail> characteristicsDetail = childCharacteristicsDetail.get(i).getCharacteristicsDetail();
//                List<CharacteristicsDetail> innerChildCharacteristicsDetail = new ArrayList<CharacteristicsDetail>(characteristicsDetail);
//                getUserCharacteristicsFromLocalDBRecursively(innerChildCharacteristicsDetail);
//            }
//        }
//    }

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
        DataServicesManager.getInstance().fetchUserCharacteristics(this);
    }
}
