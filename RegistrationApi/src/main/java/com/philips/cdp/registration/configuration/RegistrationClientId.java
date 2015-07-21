
package com.philips.cdp.registration.configuration;

public class RegistrationClientId {

	private String developmentId;

	private String evaluationId;

	private String productionId;

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

}
