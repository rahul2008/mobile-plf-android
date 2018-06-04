/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.CswConstants;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.description.DescriptionView;
import com.philips.platform.mya.csw.dialogs.ConfirmDialogTextResources;
import com.philips.platform.mya.csw.dialogs.ConfirmDialogView;
import com.philips.platform.mya.csw.dialogs.DialogView;
import com.philips.platform.mya.csw.dialogs.ProgressDialogView;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.mya.csw.permission.helper.ErrorMessageCreator;
import com.philips.platform.mya.csw.permission.uielement.LinkSpanClickListener;
import com.philips.platform.mya.csw.utils.CswLogger;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PermissionFragment extends CswBaseFragment implements PermissionContract.View, HelpClickListener, View.OnClickListener {

    public static final String TAG = "PermissionFragment";
    private ProgressDialogView progressDialog;
    private RecyclerView recyclerView;

    private List<ConsentDefinition> consentDefinitionList = null;
    private PermissionContract.Presenter presenter;
    private ConfirmDialogView confirmDialogView;
    private DialogView errorDialogViewWithClickListener;

    private boolean mIsStateAlreadySaved;

    @Override
    public void setPresenter(final PermissionContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.csw_privacy_settings;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_permission_view, container, false);
        recyclerView = view.findViewById(R.id.consentsRecycler);

        if (getArguments() != null)
            consentDefinitionList = (List<ConsentDefinition>) getArguments().getSerializable(CswConstants.CONSENT_DEFINITIONS);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.trackPageName();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsStateAlreadySaved = false;
        presenter.fetchConsentStates(consentDefinitionList);
    }


    @Override
    public void onPause() {
        super.onPause();
        mIsStateAlreadySaved = true;
        if (confirmDialogView != null) {
            confirmDialogView.hideDialog();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PermissionAdapter adapter = new PermissionAdapter(createConsentsList(), this);
        adapter.setPrivacyNoticeClickListener(new LinkSpanClickListener() {
            @Override
            public void onClick() {
                onPrivacyNoticeClicked();
            }
        });

        new PermissionPresenter(this, adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecyclerViewSeparatorItemDecoration separatorItemDecoration = new RecyclerViewSeparatorItemDecoration(recyclerView.getContext());
        recyclerView.addItemDecoration(separatorItemDecoration);
        recyclerView.setAdapter(adapter);
    }

    private void onPrivacyNoticeClicked() {
        boolean isOnline = getRestClient().isInternetReachable();
        if (isOnline) {
            PermissionHelper.getInstance().getMyAccountUIEventListener().onPrivacyNoticeClicked();
        } else {
            showErrorDialog(false, getString(R.string.csw_offline_title), getString(R.string.csw_offline_message));
        }
    }

    @Override
    public void showProgressDialog() {
        final FragmentActivity activity = getActivity();
        if (activity != null && !(activity.isFinishing())) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialogView();
            }
            progressDialog.showDialog(activity);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isDialogShown()) {
            progressDialog.hideDialog();
        }
    }

    @Override
    public void showErrorDialog(boolean goBack, int titleRes, ConsentError error) {
        String message = toErrorMessage(error);
        String title = getString(titleRes);
        showErrorDialog(goBack, title, message);
    }

    @Override
    public void showErrorDialog(boolean goBack, int titleRes, int messageRes) {
        if (!mIsStateAlreadySaved) {
            String title = getString(titleRes);
            String message = getString(messageRes);
            showErrorDialog(goBack, title, message);
        }
    }

    private void showErrorDialog(boolean goBack, String title, String message) {
        CswLogger.e(TAG, message);
        DialogView dialogView = getDialogView(goBack);
        dialogView.showDialog(getActivity(), title, message);
    }

    @Override
    public void showConfirmRevokeConsentDialog(final ConfirmDialogTextResources dialogTexts, final ConfirmDialogView.ConfirmDialogResultHandler handler) {
        confirmDialogView = new ConfirmDialogView();
        confirmDialogView.setupDialog(dialogTexts);
        confirmDialogView.setResultHandler(handler);
        confirmDialogView.showDialog(getActivity());
    }

    @Override
    public void onHelpClicked(int helpText) {
        DescriptionView.show(getFragmentManager(), helpText, R.id.permissionView);
    }

    @Override
    public void onClick(View view) {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    private RestInterface getRestClient() {
        return CswInterface.get().getDependencies().getAppInfra().getRestClient();
    }

    private String toErrorMessage(ConsentError error) {
        return ErrorMessageCreator.getMessageErrorBasedOnErrorCode(getContext(), error.getErrorCode());
    }

    @NonNull
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    protected DialogView getDialogView(boolean goBack) {
        if (!goBack) {
            return new DialogView();
        }

        if (errorDialogViewWithClickListener == null) {
            errorDialogViewWithClickListener = new DialogView(this);
        }
        return errorDialogViewWithClickListener;
    }

    private List<ConsentView> createConsentsList() {
        final List<ConsentView> consentViewList = new ArrayList<>();
        for (ConsentDefinition consentDefinition : consentDefinitionList) {
            try {
                consentViewList.add(new ConsentView(consentDefinition));
            } catch (RuntimeException exception) {
                CswLogger.d("RuntimeException", exception.getMessage());
            }
        }
        return consentViewList;
    }
}
