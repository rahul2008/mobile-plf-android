package cdp.philips.com.mydemoapp.characteristics;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import cdp.philips.com.mydemoapp.R;

public class CharacteristicsDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText mEtCharacteristics;
    private CharacteristicsDialogPresenter mCharacteristicsDialogPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_user_characteristics, container, false);

        Button mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);

        Button mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);

        mEtCharacteristics = (EditText) rootView.findViewById(R.id.et_characteristics);
        mCharacteristicsDialogPresenter = new CharacteristicsDialogPresenter();

        String mSampleJsonString = "{\n" +
                "  \"characteristics\": [\n" +
                "    {\n" +
                "      \"type\": \"User\",\n" +
                "      \"value\": \"John\",\n" +
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
        return rootView;
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
                mCharacteristicsDialogPresenter.createOrUpdateCharacteristics(userCharacteristics);
                getDialog().dismiss();
                break;
            case R.id.btnCancel:
                getDialog().dismiss();
                break;
        }
    }
}
