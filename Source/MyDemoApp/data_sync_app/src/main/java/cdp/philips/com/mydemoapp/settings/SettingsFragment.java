package cdp.philips.com.mydemoapp.settings;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.cdp.prxclient.datamodels.assets.Data;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import cdp.philips.com.mydemoapp.consents.ConsentDialogAdapter;
import cdp.philips.com.mydemoapp.database.table.OrmConsent;

/**
 * Created by sangamesh on 09/01/17.
 */

public class SettingsFragment extends DialogFragment implements DBRequestListener,DBChangeListener, View.OnClickListener {

    private Button mBtnOk;
    private Button mBtnCancel;
    SettingsFragmentPresenter settingsFragmentPresenter;
    private ProgressDialog mProgressDialog;
    ArrayList<? extends ConsentDetail> consentDetails;
    private Context mContext;
    private DataServicesManager mDataServicesManager;
    private Spinner mSpinner_metrics,mSpinner_Local;
    private ArrayList<Settings> settingses;
    boolean isDataChanged=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_settings, container,
                false);

        mDataServicesManager = DataServicesManager.getInstance();
        mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnOk.setEnabled(false);
        mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);
        settingsFragmentPresenter = new SettingsFragmentPresenter(getActivity(), this);
        mProgressDialog = new ProgressDialog(getActivity());
        mDataServicesManager.registerDBChangeListener(this);

        mSpinner_metrics = (Spinner)rootView.findViewById(R.id.spinner_metrics);
        mSpinner_Local = (Spinner)rootView.findViewById(R.id.spinner_locale);
        ArrayAdapter<CharSequence> adapterMetrics = ArrayAdapter.createFromResource(getActivity(),
                R.array.metrics, android.R.layout.simple_spinner_item);
        adapterMetrics.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_metrics.setAdapter(adapterMetrics);

        ArrayAdapter<CharSequence> adapterLocale = ArrayAdapter.createFromResource(getActivity(),
                R.array.locals, android.R.layout.simple_spinner_item);
        adapterLocale.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_Local.setAdapter(adapterLocale);
        mSpinner_metrics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBtnOk.setEnabled(true);
            }
        });

        mSpinner_Local.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBtnOk.setEnabled(true);
            }
        });

        fetchSettings();
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(final ArrayList<? extends Object> ormObjectList) {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(ormObjectList!=null){
                        settingses=(ArrayList<Settings>) ormObjectList;
                        for(Settings settings:settingses){

                            updateUi(settings.getType(),settings.getValue());

                        }
                    }
                    dismissProgressDialog();
                }
            });
        }
    }

    private void updateUi(String type, String value) {

        switch (type){

            case Settings.METRICS:
                mSpinner_metrics.setSelection(Arrays.asList(getResources().getStringArray(R.array.metrics)).indexOf(value));
                break;

            case Settings.LOCALE:
                mSpinner_Local.setSelection(Arrays.asList(getResources().getStringArray(R.array.locals)).indexOf(value));
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onSuccess(Object data) {

       /* final OrmConsent ormConsent = (OrmConsent) data;
        if (getActivity() != null && ormConsent != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    mBtnOk.setEnabled(true);
                    dismissProgressDialog();
                }
            });
        }*/

    }

    @Override
    public void onFailure(final Exception exception) {

        if (getActivity() != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOK:


                for (Settings settings:settingses){

                    switch (settings.getType()){

                        case Settings.METRICS:
                            settings.setValue(mSpinner_metrics.getSelectedItem().toString());
                            break;

                        case Settings.LOCALE:
                            settings.setValue(mSpinner_Local.getSelectedItem().toString());
                            break;

                    }

                }

                DataServicesManager.getInstance().updateSettings(settingses,this);
                dismissConsentDialog(getDialog());
                break;
            case R.id.btnCancel:
                dismissConsentDialog(getDialog());
                break;

        }
    }

    private void dismissConsentDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        DataServicesManager.getInstance().unRegisterDBChangeListener();
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
        dialog.setTitle(R.string.settings);
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void fetchSettings() {
        showProgressDialog();
        DataServicesManager.getInstance().fetchSettings(this);
    }

    @Override
    public void dBChangeSuccess() {
        DataServicesManager.getInstance().fetchSettings(this);
    }

    @Override
    public void dBChangeFailed(final Exception e) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getActivity(),"Exception :"+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
