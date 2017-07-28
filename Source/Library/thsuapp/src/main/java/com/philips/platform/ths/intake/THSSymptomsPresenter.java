package com.philips.platform.ths.intake;

import android.net.Uri;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.UploadAttachment;
import com.americanwell.sdk.entity.consumer.DocumentRecord;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.selectimage.THSDocumentRecordCallback;
import com.philips.platform.ths.intake.selectimage.THSUploadDocumentCallback;
import com.philips.platform.ths.providerslist.THSOnDemandSpeciality;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSFileUtils;
import com.philips.platform.ths.utility.THSManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class THSSymptomsPresenter implements THSBasePresenter, THSVisitContextCallBack<THSVisitContext, THSSDKError>, THSDocumentRecordCallback, THSUploadDocumentCallback {
    THSBaseFragment thsBaseView;
    THSProviderInfo THSProviderInfo;
    THSVisitContext THSVisitContext;
    private THSFileUtils fileUtils;
    private UploadAttachment uploadAttachment;


    THSSymptomsPresenter(THSBaseFragment uiBaseView, THSProviderInfo THSProviderInfo) {
        this.thsBaseView = uiBaseView;
        this.THSProviderInfo = THSProviderInfo;
        fileUtils = new THSFileUtils();
    }

    THSSymptomsPresenter(THSBaseFragment uiBaseView) {
        this.thsBaseView = uiBaseView;
        fileUtils = new THSFileUtils();
    }


    public void uploadDocuments(THSConsumer thsConsumer, final Uri uri) {
        try {

            uploadAttachment = fileUtils.getUploadAttachment(thsBaseView.getFragmentActivity(), THSManager.getInstance().getAwsdk(thsBaseView.getFragmentActivity().getApplicationContext()), uri);
            THSManager.getInstance().uploadHealthDocument(thsBaseView.getFragmentActivity(), uploadAttachment, this);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void fetchHealthDocuments(THSConsumer thsConsumer) {
        try {
            THSManager.getInstance().fetchHealthDocumentRecordList(thsBaseView.getFragmentActivity(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.continue_btn) {
            final THSVitalsFragment fragment = new THSVitalsFragment();
            fragment.setFragmentLauncher(thsBaseView.getFragmentLauncher());
            thsBaseView.addFragment(fragment, THSVitalsFragment.TAG, null);
        }
    }

    @Override
    public void onResponse(THSVisitContext THSVisitContext, THSSDKError THSSDKError) {
        updateSymptoms(THSVisitContext);
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
        thsBaseView.hideProgressBar();
    }

    void getVisitContext() {

        try {
            THSManager.getInstance().getVisitContext(thsBaseView.getFragmentActivity(), THSProviderInfo, this);
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
                updateSymptoms(pthVisitContext);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsBaseView.hideProgressBar();
            }
        });
    }

    @Override
    public void onDocumentRecordFetchSuccess(List<DocumentRecord> documentRecordList, SDKError sdkError) {
        if (documentRecordList.size() > 0) {
            thsBaseView.showToast("list size" + documentRecordList.size());
        } else if (null != sdkError) {
            thsBaseView.showToast("list is zero sdk error");
        } else {
            thsBaseView.showToast("list is zero");
        }
    }

    @Override
    public void onUploadValidationFailure(Map<String, ValidationReason> map) {
        thsBaseView.showToast("validation failure");
    }

    @Override
    public void onUploadDocumentSuccess(DocumentRecord documentRecord, SDKError sdkError) {
        if (null != documentRecord && null == sdkError) {
            thsBaseView.showToast("success with Document name");
        } else {
            thsBaseView.showToast("upload failed with sdk error");
        }

    }

    @Override
    public void onError(Throwable throwable) {
        thsBaseView.showToast("failure : "+throwable.getLocalizedMessage());
        thsBaseView.hideProgressBar();
    }
}
