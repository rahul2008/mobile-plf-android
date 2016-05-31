
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;

public class RegistrationClientId {

    private String developmentId;

    private String evaluationId;

    private String productionId;

    private String testingId;

    private String stagingId;

    /**
     * Get production id if defined in JSON or DYNAMICALLY
     *
     * @return String
     */
    public String getProductionId() {
        return productionId;
    }

    /**
     * Set production id
     *
     * @return String
     */
    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    /**
     * Get evaluation id if defined in JSON or DYNAMICALLY
     *
     * @return String
     */
    public String getEvaluationId() {
        return evaluationId;
    }

    /**
     * Set evaluation id
     *
     * @return String
     */
    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    /**
     * Get develoment id if defined in JSON or DYNAMICALLY
     *
     * @return String
     */
    public String getDevelopmentId() {
        return developmentId;
    }

    /**
     * Set development id
     *
     * @return String
     */
    public void setDevelopmentId(String developmentId) {
        this.developmentId = developmentId;
    }

    /**
     * Get testing id if defined in JSON or DYNAMICALLY
     *
     * @return String
     */
    public String getTestingId() {
        return testingId;
    }

    /**
     * Set testing  id
     *
     * @return String
     */
    public void setTestingId(String testingId) {
        this.testingId = testingId;
    }

    /**
     * Get staging id if defined in JSON or DYNAMICALLY
     *
     * @return String
     */
    public String getStagingId() {
        return stagingId;
    }

    /**
     * Set stagging id
     *
     * @return String
     */
    public void setStagingId(String stagingId) {
        this.stagingId = stagingId;
    }


}
