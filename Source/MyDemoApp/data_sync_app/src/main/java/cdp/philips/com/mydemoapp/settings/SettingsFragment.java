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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.Arrays;
import java.util.List;

import cdp.philips.com.mydemoapp.R;
import android.support.v4.app.Fragment;

public class SettingsFragment extends Fragment implements DBFetchRequestListner<Settings>,DBRequestListener<Settings>, DBChangeListener, View.OnClickListener {

    private Button mBtnOk;
    private Button mBtnCancel;
    SettingsFragmentPresenter settingsFragmentPresenter;
    private ProgressDialog mProgressDialog;
    private DataServicesManager mDataServicesManager;
    private Spinner mSpinner_Unit, mSpinner_Local;
    private Settings settings;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_settings, container,
                false);

        mDataServicesManager = DataServicesManager.getInstance();
        mBtnOk = (Button) rootView.findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        mBtnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);
        settingsFragmentPresenter = new SettingsFragmentPresenter(getActivity(), this);
        mProgressDialog = new ProgressDialog(getActivity());
        mSpinner_Unit = (Spinner) rootView.findViewById(R.id.spinner_metrics);
        mSpinner_Local = (Spinner) rootView.findViewById(R.id.spinner_locale);
        ArrayAdapter<CharSequence> adapterMetrics = ArrayAdapter.createFromResource(getActivity(),
                R.array.metrics, android.R.layout.simple_spinner_item);
        adapterMetrics.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_Unit.setAdapter(adapterMetrics);

        ArrayAdapter<CharSequence> adapterLocale = ArrayAdapter.createFromResource(getActivity(),
                R.array.locals, android.R.layout.simple_spinner_item);
        adapterLocale.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner_Local.setAdapter(adapterLocale);

        fetchSettings();
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSuccess(final List<? extends Settings> data) {
        refreshUi(data);
    }

    private void refreshUi(final List<? extends Settings> data) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (data != null) {
                        settings = data.get(0);
                        updateUi(settings.getUnit(), settings.getLocale());

                    }
                    dismissProgressDialog();
                }
            });
        }
    }

    private void updateUi(String unit, String locale) {

        mSpinner_Unit.setSelection(Arrays.asList(getResources().getStringArray(R.array.metrics)).indexOf(unit));
        mSpinner_Local.setSelection(Arrays.asList(getResources().getStringArray(R.array.locals)).indexOf(locale));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onFailure(final Exception exception) {

        refreshOnFailure(exception);

    }

    private void refreshOnFailure(final Exception exception) {
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

                settings.setUnit(mSpinner_Unit.getSelectedItem().toString());
                settings.setLocale(mSpinner_Local.getSelectedItem().toString());
                settingsFragmentPresenter.updateSettings(settings);

                getFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
            case R.id.btnCancel:
                getFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;

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
        mDataServicesManager.registerDBChangeListener(this);
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
        DataServicesManager.getInstance().fetchUserSettings(this);
    }

    @Override
    public void dBChangeSuccess(SyncType type) {
        if(type!=SyncType.SETTINGS)return;
        DataServicesManager.getInstance().fetchUserSettings(this);
    }

    @Override
    public void dBChangeFailed(final Exception e) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Exception :" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onFetchSuccess(final List<? extends Settings> data) {
        refreshUi(data);
    }

    @Override
    public void onFetchFailure(final Exception exception) {
        refreshOnFailure(exception);
    }
}
