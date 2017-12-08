/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.support.annotation.NonNull;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.visit.Topic;
import com.americanwell.sdk.entity.visit.TriageQuestion;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.exception.FeatureNotEnabledException;

import java.util.List;
import java.util.Set;

public class THSVisitContext {

    private VisitContext visitContext;

    public THSVisitContext() {

    }

    public VisitContext getVisitContext() {
        return visitContext;
    }

    public void setVisitContext(VisitContext visitContext) {
        this.visitContext = visitContext;
    }

    boolean isRequireAddress() {
        return visitContext.isRequireAddress();
    }

    boolean isCanPrescribe() {
        return visitContext.isCanPrescribe();
    }

    boolean showTriageQuestions() {
        return visitContext.showTriageQuestions();
    }

    boolean showConditionsQuestion() {
        return visitContext.showConditionsQuestion();
    }

    boolean showAllergiesQuestion() {
        return visitContext.showAllergiesQuestion();
    }

    boolean showMedicationsQuestion() {
        return visitContext.showMedicationsQuestion();
    }

    boolean showVitalsQuestion() {
        return visitContext.showVitalsQuestion();
    }

    boolean isHasHealthHistory() {
        return visitContext.isHasHealthHistory();
    }

    List<Topic> getTopics() {
        return visitContext.getTopics();
    }

    List<LegalText> getLegalTexts() {
        return visitContext.getLegalTexts();
    }

    List<TriageQuestion> getTriageQuestions() {
        return visitContext.getTriageQuestions();
    }

    boolean hasProvider() {
        return visitContext.hasProvider();
    }

    String getProviderName() {
        return visitContext.getProviderName();
    }

    boolean hasOnDemandSpecialty() {
        return visitContext.hasOnDemandSpecialty();
    }

    boolean hasAppointment() {
        return visitContext.hasAppointment();
    }

    void setShareHealthSummary(boolean isShareHealthSummary) {
        visitContext.setShareHealthSummary(isShareHealthSummary);
    }

    void setOtherTopic(@NonNull String otherTopic) {
        visitContext.setOtherTopic(otherTopic);
    }

    void setCallbackNumber(@NonNull String callbackNumber) {
        visitContext.setCallbackNumber(callbackNumber);
    }

    String getCallbackNumber() {
        return visitContext.getCallbackNumber();
    }

    Consumer getAuthenticatedConsumer() {
        return visitContext.getAuthenticatedConsumer();
    }

    String getProposedCouponCode() {
        return visitContext.getProposedCouponCode();
    }

    void setGuestInvitationEmails(@NonNull Set<String> guestInvitationEmails) throws FeatureNotEnabledException {
        visitContext.setGuestInvitationEmails(guestInvitationEmails);
    }

}
