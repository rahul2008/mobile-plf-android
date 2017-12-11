/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.cocoversion;

/**
 * @author: Created by philips on 4/18/17.
 */
public class CocoVersionItem {
    private String title;
    private String version;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDescriptionShowing() {
        return isDescriptionShowing;
    }

    public void setDescriptionShowing(boolean descriptionShowing) {
        isDescriptionShowing = descriptionShowing;
    }

    private boolean isDescriptionShowing;
}
