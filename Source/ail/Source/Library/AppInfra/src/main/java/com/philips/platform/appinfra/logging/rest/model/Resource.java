
package com.philips.platform.appinfra.logging.rest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resource {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("resourceType")
    @Expose
    private String resourceType;
    @SerializedName("component")
    @Expose
    private String component;
    @SerializedName("serverName")
    @Expose
    private String serverName;
    @SerializedName("logData")
    @Expose
    private LogData logData;
    @SerializedName("applicationName")
    @Expose
    private String applicationName;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("severity")
    @Expose
    private String severity;
    @SerializedName("applicationInstance")
    @Expose
    private String applicationInstance;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;
    @SerializedName("originatingUser")
    @Expose
    private String originatingUser;
    @SerializedName("logTime")
    @Expose
    private String logTime;
    @SerializedName("applicationVersion")
    @Expose
    private String applicationVersion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public LogData getLogData() {
        return logData;
    }

    public void setLogData(LogData logData) {
        this.logData = logData;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getApplicationInstance() {
        return applicationInstance;
    }

    public void setApplicationInstance(String applicationInstance) {
        this.applicationInstance = applicationInstance;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getOriginatingUser() {
        return originatingUser;
    }

    public void setOriginatingUser(String originatingUser) {
        this.originatingUser = originatingUser;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

}
