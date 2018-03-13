package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.entity.visit.TriageQuestion;
import com.americanwell.sdk.entity.visit.VisitContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class THSVisitContextTest {
    THSVisitContext pthVisitContext;

    @Mock
    VisitContext visitContextMock;

    @Mock
    List<Topic> topicsMock;

    @Mock
    List<LegalText> legalTextsMock;

    @Mock
    List<TriageQuestion> triageQuestionsListMock;

    @Mock
    Consumer consumerMock;

    @Mock
    Set<String> guestInvitationEmails;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pthVisitContext = new THSVisitContext();
        pthVisitContext.setVisitContext(visitContextMock);
    }

    @Test
    public void getVisitContext() throws Exception {
        VisitContext visitContext = pthVisitContext.getVisitContext();
        assertThat(visitContext).isNotNull();
        assertThat(visitContext).isInstanceOf(VisitContext.class);
    }

    @Test
    public void isRequireAddress() throws Exception {
        when(visitContextMock.isRequireAddress()).thenReturn(true);
        boolean requireAddress = pthVisitContext.isRequireAddress();
        assertTrue(requireAddress);
    }

    @Test
    public void isCanPrescribe() throws Exception {
        when(visitContextMock.isCanPrescribe()).thenReturn(true);
        boolean canPrescribe = pthVisitContext.isCanPrescribe();
        assertTrue(canPrescribe);
    }

    @Test
    public void showTriageQuestions() throws Exception {
        when(visitContextMock.showTriageQuestions()).thenReturn(true);
        boolean traigeQuestions = pthVisitContext.showTriageQuestions();
        assertTrue(traigeQuestions);
    }

    @Test
    public void showConditionsQuestion() throws Exception {
        when(visitContextMock.showConditionsQuestion()).thenReturn(true);
        boolean showConditionsQuestion = pthVisitContext.showConditionsQuestion();
        assertTrue(showConditionsQuestion);
    }

    @Test
    public void showAllergiesQuestion() throws Exception {
        when(visitContextMock.showAllergiesQuestion()).thenReturn(true);
        boolean showAllergiesQuestion = pthVisitContext.showAllergiesQuestion();
        assertTrue(showAllergiesQuestion);
    }

    @Test
    public void showMedicationsQuestion() throws Exception {
        when(visitContextMock.showMedicationsQuestion()).thenReturn(true);
        boolean showMedicationsQuestion = pthVisitContext.showMedicationsQuestion();
        assertTrue(showMedicationsQuestion);
    }

    @Test
    public void showVitalsQuestion() throws Exception {
        when(visitContextMock.showVitalsQuestion()).thenReturn(true);
        boolean showVitalsQuestion = pthVisitContext.showVitalsQuestion();
        assertTrue(showVitalsQuestion);
    }

    @Test
    public void isHasHealthHistory() throws Exception {
        when(visitContextMock.isHasHealthHistory()).thenReturn(true);
        boolean hasHealthHistory = pthVisitContext.isHasHealthHistory();
        assertTrue(hasHealthHistory);
    }

    @Test
    public void getTopics() throws Exception {
        when(visitContextMock.getTopics()).thenReturn(topicsMock);
        List<Topic> topics = pthVisitContext.getTopics();
        assertNotNull(topics);
        assertThat(topics).isInstanceOf(List.class);
    }

    @Test
    public void getLegalTexts() throws Exception {
        when(visitContextMock.getLegalTexts()).thenReturn(legalTextsMock);
        List<LegalText> legalTexts = pthVisitContext.getLegalTexts();
        assertNotNull(legalTexts);
        assertThat(legalTexts).isInstanceOf(List.class);
    }

    @Test
    public void getTriageQuestions() throws Exception {
        when(visitContextMock.getTriageQuestions()).thenReturn(triageQuestionsListMock);
        List<TriageQuestion> triageQuestions = pthVisitContext.getTriageQuestions();
        assertNotNull(triageQuestions);
        assertThat(triageQuestions).isInstanceOf(List.class);
    }

    @Test
    public void hasProvider() throws Exception {
        when(visitContextMock.hasProvider()).thenReturn(true);
        boolean hasProvider = pthVisitContext.hasProvider();
        assertTrue(hasProvider);
    }

    @Test
    public void getProviderName() throws Exception {
        when(visitContextMock.getProviderName()).thenReturn("Spoorti");
        String providerName = pthVisitContext.getProviderName();
        assertNotNull(providerName);
        assertEquals(providerName,"Spoorti");
    }

    @Test
    public void hasOnDemandSpecialty() throws Exception {
        when(visitContextMock.hasOnDemandSpecialty()).thenReturn(true);
        boolean hasOnDemandSpecialty = pthVisitContext.hasOnDemandSpecialty();
        assertTrue(hasOnDemandSpecialty);
    }

    @Test
    public void hasAppointment() throws Exception {
        when(visitContextMock.hasAppointment()).thenReturn(true);
        boolean hasAppointment = pthVisitContext.hasAppointment();
        assertTrue(hasAppointment);
    }

    @Test
    public void setShareHealthSummary() throws Exception {
        pthVisitContext.setShareHealthSummary(true);
        verify(visitContextMock).setShareHealthSummary(true);
    }

    @Test
    public void setOtherTopic() throws Exception {
        pthVisitContext.setOtherTopic("Spoorti");
        verify(visitContextMock).setOtherTopic("Spoorti");
    }

    @Test
    public void setCallbackNumber() throws Exception {
        pthVisitContext.setCallbackNumber("Spoorti");
        verify(visitContextMock).setCallbackNumber("Spoorti");
    }

    @Test
    public void getCallbackNumber() throws Exception {
        when(visitContextMock.getCallbackNumber()).thenReturn("1234");
        String callbackNumber = pthVisitContext.getCallbackNumber();
        assertNotNull(callbackNumber);
        assertEquals(callbackNumber,"1234");
    }

    /*@Test
    public void getAuthenticatedConsumer() throws Exception {
        when(visitContextMock.getAuthenticatedConsumer()).thenReturn(consumerMock);
        Consumer authenticatedConsumer = pthVisitContext.getAuthenticatedConsumer();
        assertNotNull(authenticatedConsumer);
        assertThat(authenticatedConsumer).isInstanceOf(Consumer.class);
    }*/


    @Test
    public void getProposedCouponCode() throws Exception {
        when(visitContextMock.getProposedCouponCode()).thenReturn("1234");
        String proposedCouponCode = pthVisitContext.getProposedCouponCode();
        assertNotNull(proposedCouponCode);
        assertEquals(proposedCouponCode,"1234");
    }

    @Test
    public void setGuestInvitationEmails() throws Exception {
        pthVisitContext.setGuestInvitationEmails(guestInvitationEmails);
        verify(visitContextMock).setGuestInvitationEmails(guestInvitationEmails);
    }

}