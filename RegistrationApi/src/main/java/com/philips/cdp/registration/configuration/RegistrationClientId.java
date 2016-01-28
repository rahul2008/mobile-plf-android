
package com.philips.cdp.registration.configuration;

public class RegistrationClientId {

    private String developmentId;

    private String evaluationId;

    private String productionId;

    private String testingId;

    private String stagingId;

  


    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getDevelopmentId() {
        return developmentId;
    }

    public void setDevelopmentId(String developmentId) {
        this.developmentId = developmentId;
    }

    public String getTestingId() {
        return testingId;
    }

    public void setTestingId(String testingId) {
        this.testingId = testingId;
    }

    public String getStagingId() {
        return stagingId;
    }

    public void setStagingId(String stagingId) {
        this.stagingId = stagingId;
    }


}
