package cdp.philips.com.mydemoapp.characteristics;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.philips.platform.core.datatypes.ConsentDetail;

import java.util.ArrayList;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.consents.ConsentDialogAdapter;

/**
 * Created by sangamesh on 08/11/16.
 */

public class CharacteristicsDialogFragment extends DialogFragment implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private Button mBtnOk;
    private Button mBtnCancel;
    private ConsentDialogAdapter lConsentAdapter;
    CharacteristicsDialogPresenter consentDialogPresenter;
    private ProgressDialog mProgressDialog;
    ArrayList<? extends ConsentDetail> consentDetails;
    EditText mEtcharacteristics;

    String sampleJsonString=null;
    CharacteristicsDialogPresenter characteristicsDialogPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_user_characteristics, container,
                false);

        mBtnOk=(Button)rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnCancel=(Button)rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);
        mEtcharacteristics=(EditText)rootView.findViewById(R.id.et_characteristics);
        characteristicsDialogPresenter=new CharacteristicsDialogPresenter(getActivity());

        sampleJsonString= "{\n" +
                "\t\"characteristics\": [{\n" +
                "\t\t\"type\": \"User\",\n" +
                "\t\t\"value\": \"John\",\n" +
                "\t\t\"characteristics\": [{\n" +
                "\t\t\t\"type\": \"Mouth\",\n" +
                "\t\t\t\"value\": \"Upper Teeth\",\n" +
                "\t\t\t\"characteristics\": [{\n" +
                "\t\t\t\t\"type\": \"BrokenTeeth\",\n" +
                "\t\t\t\t\"value\": \"1,2,3,4,5\",\n" +
                "\t\t\t\t\"characteristics\": []\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"type\": \"Mouth\",\n" +
                "\t\t\t\"value\": \"Lower Teeth\",\n" +
                "\t\t\t\"characteristics\": [{\n" +
                "\t\t\t\t\"type\": \"BrokenTeeth\",\n" +
                "\t\t\t\t\"value\": \"6,7,8,9\",\n" +
                "\t\t\t\t\"characteristics\": []\n" +
                "\t\t\t}]\n" +
                "\t\t}]\n" +
                "\t}]\n" +
                "}";
        mEtcharacteristics.setText(sampleJsonString);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void dismissConsentDialog(Dialog dialog){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissProgressDialog();
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
        dialog.setTitle(R.string.characteristics);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void showProgressDialog() {
        if(mProgressDialog!=null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnOK:
                // Parse and save JSON data here .
                //JsonObject jsonObject=new JsonObject(sampleJsonString);
                characteristicsDialogPresenter.createUpdateCharacteristics();
                getDialog().dismiss();

                break;

            case R.id.btnCancel:
                getDialog().dismiss();
                break;
        }
    }


}
