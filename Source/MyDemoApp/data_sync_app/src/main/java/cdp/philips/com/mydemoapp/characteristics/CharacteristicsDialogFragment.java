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

import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;

import cdp.philips.com.mydemoapp.R;

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
                "          \"value\": \"Upper Teeth\",\n" +
                "          \"characteristics\": [\n" +
                "            {\n" +
                "              \"type\": \"hair\",\n" +
                "              \"value\": \"black\",\n" +
                "              \"characteristics\": [\n" +
                "            {\n" +
                "              \"type\": \"BrokenTeeth\",\n" +
                "              \"value\": \"1,2,3,4,5\",\n" +
                "              \"characteristics\": []\n" +
                "            }\n" +
                "          ]\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\": \"Mouth\",\n" +
                "          \"value\": \"Lower Teeth\",\n" +
                "          \"characteristics\": [\n" +
                "            {\n" +
                "              \"type\": \"BrokenTeeth\",\n" +
                "              \"value\": \"6,7,8,9\",\n" +
                "              \"characteristics\": []\n" +
                "            }\n" +
                "          ]\n" +
                "        },\n" +
                "         {\n" +
                "          \"type\": \"skin\",\n" +
                "          \"value\": \"fair\",\n" +
                "          \"characteristics\": [\n" +
                "            {\n" +
                "              \"type\": \"color\",\n" +
                "              \"value\": \"fair\",\n" +
                "              \"characteristics\": []\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        mEtCharacteristics.setText(mSampleJsonString);
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

    @Override
    public void onSuccess(final Object data) {
        //Display User characteristics UI
//        final OrmCharacteristics ormCharacteristics = (OrmCharacteristics) data;
//        ormCharacteristics.setSynchronized(true);
//        if (ormCharacteristics != null) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Gson gson = new GsonBuilder().create();
//
//                    List<CharacteristicsDetail> characteristicsDetailList = new ArrayList<>(ormCharacteristics.getCharacteristicsDetails());
//
//                    String jsonCharacteristicsToDisplay = gson.toJson(characteristicsDetailList.get(0).getCharacteristicsDetail());
//
//                    DSLog.d(DSLog.LOG, "Inder = jsonCharacteristicsToDisplay =" + jsonCharacteristicsToDisplay);
//
//                    mEtCharacteristics.setText(jsonCharacteristicsToDisplay);
//                }
//            });
//        }

    }

    @Override
    public void onFailure(final Exception exception) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
