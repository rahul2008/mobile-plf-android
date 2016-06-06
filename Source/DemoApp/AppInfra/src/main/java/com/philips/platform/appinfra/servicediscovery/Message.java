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
