/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.net.Uri;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.UploadAttachment;
import com.americanwell.sdk.entity.consumer.DocumentRecord;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.selectimage.THSUploadDocumentCallback;
import com.philips.platform.ths.providerslist.THSOnDemandSpeciality;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSFileUtils;
import com.philips.platform.ths.utility.THSManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_CREATE_VISIT_CONTEXT;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_ON_DEMAND_SPECIALITIES;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPLOAD_CLINICAL_ATTACHMENT;

public class THSSymptomsPresenter implements THSBasePresenter, THSVisitContextCallBack<THSVisitContext, THSSDKError>, THSUploadDocumentCallback {
    protected THSBaseFragment thsBaseView;
    protected THSProviderInfo mThsProviderInfo;
    protected THSVisitContext THSVisitContext;
    protected THSFileUtils fileUtils;
    private UploadAttachment uploadAttachment;
    private THSSymptomsFragmentViewInterface thsSymptomsFragmentViewInterface;


    THSSymptomsPresenter(THSBaseFragment uiBaseView, THSProviderInfo mThsProviderInfo) {
        this.thsBaseView = uiBaseView;
        this.thsSymptomsFragmentViewInterface = (THSSymptomsFragmentViewInterface) uiBaseView;
        this.mThsProviderInfo = mThsProviderInfo;
        fileUtils = new THSFileUtils();
    }

    THSSymptomsPresenter(THSBaseFragment uiBaseView) {
        this.thsBaseView = uiBaseView;
        this.thsSymptomsFragmentViewInterface = (THSSymptomsFragmentViewInterface) uiBaseView;
        fileUtils = new THSFileUtils();
    }


    public void uploadDocuments(final Uri uri) {

        try {
            uploadAttachment = fileUtils.getUploadAttachment(thsBaseView.getFragmentActivity(),
                    THSManager.getInstance().getAwsdk(thsBaseView.getFragmentActivity().getApplicationContext()), uri);
            thsSymptomsFragmentViewInterface.setContinueButtonState(false);
            THSManager.getInstance().uploadHealthDocument(thsBaseView.getFragmentActivity(), uploadAttachment, this);
        } catch (AWSDKInstantiationException | IOException e) {
            AmwellLog.e("uploadDocument",e.toString());
        }

    }


    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.continue_btn) {
            ((THSSymptomsFragment) thsBaseView).updateOtherTopic();

            ((THSSymptomsFragment) thsBaseView).addTags();
            final THSVitalsFragment fragment = new THSVitalsFragment();
            fragment.setFragmentLauncher(thsBaseView.getFragmentLauncher());
            thsBaseView.addFragment(fragment, THSVitalsFragment.TAG, null, false);
        }
    }

    @Override
    public void onResponse(THSVisitContext THSVisitContext, THSSDKError thssdkError) {
        if (null != thsBaseView && thsBaseView.isFragmentAttached()) {
            if (null != thssdkError.getSdkError()) {
                AmwellLog.e("uploadDocument",thssdkError.getSdkError().toString());
                thsBaseView.showError(THSSDKErrorFactory.getErrorType(thsBaseView.getFragmentActivity(), ANALYTICS_CREATE_VISIT_CONTEXT,thssdkError.getSdkError()), true, false);
            } else {
                AmwellLog.d("uploadDocument","success");
                updateSymptoms(THSVisitContext);
            }
        }
    }

    private void updateSymptoms(THSVisitContext THSVisitContext) {
        this.THSVisitContext = THSVisitContext;

        if (THSVisitContext != null) {
            ((THSSymptomsFragment) thsBaseView).addTopicsToView(THSVisitContext);
        }
        thsBaseView.hideProgressBar();
    }

    @Override
    public void onFailure(Throwable throwable) {
        if (null != thsBaseView && thsBaseView.isFragmentAttached()) {
            AmwellLog.e("uploadDocument",throwable.toString());
            thsBaseView.showError(thsBaseView.getString(R.string.ths_se_server_error_toast_message));
            thsBaseView.hideProgressBar();
        }
    }

    void getVisitContext() {
        if (mThsProviderInfo == null) {
            final Provider provider = ((THSSymptomsFragment) thsBaseView).getProvider();
            THSProviderInfo thsProviderInfo = new THSProviderInfo();
            thsProviderInfo.setTHSProviderInfo(provider);
            mThsProviderInfo = thsProviderInfo;
        }
        try {
            THSManager.getInstance().getVisitContext(thsBaseView.getFragmentActivity(), mThsProviderInfo, this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        } catch (AWSDKInitializationException e) {
            e.printStackTrace();
        }
    }

    public void getfirstAvailableProvider(THSOnDemandSpeciality onDemandSpecialties) throws AWSDKInstantiationException {
        THSManager.getInstance().getVisitContextWithOnDemandSpeciality(thsBaseView.getContext(), onDemandSpecialties, new THSVisitContextCallBack<THSVisitContext, THSSDKError>() {
            @Override
            public void onResponse(THSVisitContext pthVisitContext, THSSDKError thssdkError) {
                if (null != thsBaseView && thsBaseView.isFragmentAttached()) {
                    if (null != thssdkError.getSdkError()) {
                        thsBaseView.showError(THSSDKErrorFactory.getErrorType(thsBaseView.getFragmentActivity(), ANALYTICS_ON_DEMAND_SPECIALITIES,thssdkError.getSdkError()), true, false);
                    } else {
                        updateSymptoms(pthVisitContext);
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                if (null != thsBaseView && thsBaseView.isFragmentAttached()) {
                    thsBaseView.hideProgressBar();
                    thsBaseView.showError(thsBaseView.getString(R.string.ths_se_server_error_toast_message));
                }

            }
        });
    }


    @Override
    public void onUploadValidationFailure(Map<String, ValidationReason> map) {
        if (null != thsBaseView && thsBaseView.isFragmentAttached()) {
            thsSymptomsFragmentViewInterface.setContinueButtonState(true);
            thsBaseView.showError(thsBaseView.getString(R.string.ths_add_photo_validation_error_string));
        }
    }

    @Override
    public void onUploadDocumentSuccess(DocumentRecord documentRecord, SDKError sdkError) {
        if (null != thsBaseView && thsBaseView.isFragmentAttached()) {
            thsSymptomsFragmentViewInterface.setContinueButtonState(true);
            if (null != documentRecord && null == sdkError) {
                ((THSSymptomsFragment) thsBaseView).updateDocumentRecordList(documentRecord);
            } else if (null != sdkError) {
                thsBaseView.showError(THSSDKErrorFactory.getErrorType(thsBaseView.getFragmentActivity(), ANALYTICS_UPLOAD_CLINICAL_ATTACHMENT,sdkError));
                thsBaseView.showError(thsBaseView.getString(R.string.ths_add_photo_error_string) + sdkError.getMessage());
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (null != thsBaseView && thsBaseView.isFragmentAttached()) {
            thsSymptomsFragmentViewInterface.setContinueButtonState(true);
            thsBaseView.showError(thsBaseView.getString(R.string.ths_se_server_error_toast_message));
            thsBaseView.hideProgressBar();
        }
    }
}
