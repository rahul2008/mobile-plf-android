/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.servicediscovery;

import java.util.List;

/**
 * Created by 310238655 on 6/3/2016.
 */
public class Message {

    String micrositeId;
    List urls;
    UrlsModel urlModel;

    Message(String micrositeId, UrlsModel urlModel){
        this.micrositeId=micrositeId;
        this.urlModel= urlModel;
    }
}
