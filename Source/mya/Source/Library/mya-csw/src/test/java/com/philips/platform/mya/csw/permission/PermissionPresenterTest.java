package com.philips.platform.mya.csw.permission;

import android.content.Context;
import android.test.mock.MockContext;

import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.mya.csw.CswDependencies;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.CswSettings;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.mock.AppInfraInterfaceMock;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentVersionStates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PermissionPresenterTest {
    private PermissionPresenter mPermissionPresenter;
    private ConsentError givenError;
    private List<ConsentDefinition> givenConsentDefinitions = new ArrayList<>();
    private ConsentDefinitionStatus consentDefinitionStatus;

    @Mock
    private PermissionInterface mockPermissionInterface;
    @Mock
    private PermissionAdapter mockAdapter;
    @Mock
    private ConsentDefinition mockConsentDefinition;
    @Mock
    private Context mockContext;
    @Mock
    private ConsentManagerInterface consentManagerInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        givenPresenter();
        givenConsentDefinitions();
    }

    @Test
    public void testGetConsentsIsCalledOnInteractor_withEmptyConsentDefinitions() throws Exception {
        mPermissionPresenter.getConsentStatus(Collections.EMPTY_LIST);
        verify(consentManagerInterface, never()).fetchConsentStates(givenConsentDefinitions, mPermissionPresenter);
    }

    @Test
    public void testShowProgressDialog() throws Exception {
        givenCswComponent();
        mPermissionPresenter.getConsentStatus(givenConsentDefinitions);
        verify(mockPermissionInterface).showProgressDialog();
    }

    @Test
    public void testGetConsentsIsCalledOnInteractor() throws Exception {
        givenCswComponent();
        mPermissionPresenter.getConsentStatus(givenConsentDefinitions);
        verify(consentManagerInterface).fetchConsentStates(givenConsentDefinitions, mPermissionPresenter);
    }

    @Test
    public void testShowProgressDialog_withEmptyConsentDefinition() throws Exception {
        givenCswComponent();
        mPermissionPresenter.getConsentStatus(Collections.EMPTY_LIST);
        verify(mockPermissionInterface, never()).showProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onError() throws Exception {
        givenConsentError();
        mPermissionPresenter.onGetConsentsFailed(givenError);
        verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testHideProgressDialog_onSuccess() throws Exception {
        givenCswComponent();
        mPermissionPresenter.onGetConsentsSuccess(givenConsentDefinitionStatusList());
        verify(mockPermissionInterface).hideProgressDialog();
    }

    @Test
    public void testShouldShowErrorWhenGetConsentFails() throws Exception {
        String errorTitle = "test error title";
        String errorMessage = "test error message";
        givenConsentError();
        given(mockContext.getString(R.string.csw_problem_occurred_error_title)).willReturn(errorTitle);
        given(mockContext.getString(R.string.csw_problem_occurred_error_message, givenError.getErrorCode())).willReturn(errorMessage);
        whenGetConsentFailed();
        thenErrorIsShown(true, errorTitle, errorMessage);
    }

    @Test
    public void testShouldShowErrorWhenCreateConsentFails() throws Exception {
        String errorTitle = "test error title";
        String errorMessage = "test error message";
        givenConsentError();
        given(mockContext.getString(R.string.csw_problem_occurred_error_title)).willReturn(errorTitle);
        given(mockContext.getString(R.string.csw_problem_occurred_error_message, givenError.getErrorCode())).willReturn(errorMessage);
        whenCreateConsentFailed();
        thenErrorIsShown(false, errorTitle, errorMessage);
    }

    @Test
    public void testShouldShowLoaderWhenTogglingConsent() throws Exception {
        whenTogglingConsentTo(true);
        thenProgressIsShown();
    }

    @Test
    public void testShouldNotShowLoaderWhenTogglingConsent() throws Exception {
        String errorTitle = "test offline error title";
        String errorMessage = "test offline error message";
        given(mockContext.getString(R.string.csw_offline_title)).willReturn(errorTitle);
        given(mockContext.getString(R.string.csw_offline_message)).willReturn(errorMessage);
        whenTogglingConsentTo(true);
        thenOfflineErrorIsShown(false, errorTitle, errorMessage);
    }

    @Test
    public void testShouldHideLoaderWhenCreateConsentFails() throws Exception {
        givenCswComponent();
        givenConsentError();
        given(mockContext.getString(R.string.csw_problem_occurred_error_title)).willReturn("title");
        given(mockContext.getString(R.string.csw_problem_occurred_error_message, givenError.getErrorCode())).willReturn("message");
        whenTogglingConsentTo(true);
        whenCreateConsentFailed();
        thenProgressIsHidden();
    }

    @Test
    public void testShouldHideLoaderWhenCreateConsentSucceeds() throws Exception {
        whenTogglingConsentTo(false);
        whenCreateConsentSucceeds();
        thenProgressIsHidden();
    }

    private ArrayList<ConsentDefinitionStatus> givenConsentDefinitionStatusList() {
        ArrayList<ConsentDefinitionStatus> consentArrayList = new ArrayList<>();
        consentDefinitionStatus = new ConsentDefinitionStatus();
        consentDefinitionStatus.setConsentDefinition(mockConsentDefinition);
        consentDefinitionStatus.setConsentState(ConsentStates.active);
        consentDefinitionStatus.setConsentVersionState(ConsentVersionStates.AppVersionIsHigher);
        consentArrayList.add(consentDefinitionStatus);
        return consentArrayList;
    }

    private void givenCswComponent() {
        CswInterface cswInterface = new CswInterface();
        AppInfraInterfaceMock appInfraInterface = new AppInfraInterfaceMock();
        appInfraInterface.consentManagerInterface = consentManagerInterface;
        MockContext context = new MockContext();
        CswDependencies cswDependencies = new CswDependencies(appInfraInterface);
        CswSettings cswSettings = new CswSettings(context);
        cswInterface.init(cswDependencies, cswSettings);
    }

    private void givenConsentError() {
        givenError = new ConsentError("SOME ERROR", 401);
    }

    private void givenConsentDefinitions() {
        ConsentDefinition definition = new ConsentDefinition("", "", Collections.singletonList("moment"), 0);
        givenConsentDefinitions = Arrays.asList(definition);
        givenPresenter();
    }

    private void givenPresenter() {
        mPermissionPresenter = new PermissionPresenter(mockPermissionInterface, mockAdapter);
        mPermissionPresenter.mContext = mockContext;
    }

    public void whenCreateConsentSuccess() {
        mPermissionPresenter.onPostConsentSuccess();
    }

    private void whenGetConsentFailed() {
        mPermissionPresenter.onGetConsentsFailed(givenError);
    }

    private void whenCreateConsentFailed() {
        mPermissionPresenter.onPostConsentFailed(givenError);
    }

    private void whenCreateConsentSucceeds() {
        mPermissionPresenter.onPostConsentSuccess();
    }

    private void whenTogglingConsentTo(boolean toggled) {
        mPermissionPresenter.onToggledConsent(1, mockConsentDefinition, toggled);
    }

    private void thenErrorIsShown(boolean goBack, String title, String message) {
        verify(mockPermissionInterface).showErrorDialog(goBack, title, message);
    }

    private void thenOfflineErrorIsShown(boolean goBack, String title, String message) {
        verify(mockPermissionInterface).showErrorDialog(goBack, title, message);
    }

    private void thenProgressIsShown() {
        verify(mockPermissionInterface).showProgressDialog();
    }

    private void thenProgressIsHidden() {
        verify(mockPermissionInterface).hideProgressDialog();
    }
}
