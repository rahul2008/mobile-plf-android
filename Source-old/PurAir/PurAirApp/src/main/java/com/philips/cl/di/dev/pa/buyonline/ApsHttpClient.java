package com.philips.cl.di.dev.pa.buyonline;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;

class ApsHttpClient {
    private DefaultHttpClient sHttpClient;
    protected String TAG;
    
    protected ApsHttpClient(DefaultHttpClient client,String tag){
        this.sHttpClient = client;
        this.TAG = tag;
    }
    
    protected HttpResponse execute(HttpHead head) throws IOException {
        return sHttpClient.execute(head);
    }

    protected HttpResponse execute(HttpUriRequest request) throws IOException {
        return sHttpClient.execute(request);
    }
    
    protected HttpResponse execute(HttpPost post) throws IOException {
        final HttpHost host = (HttpHost) sHttpClient.getParams().getParameter(ConnRouteParams.DEFAULT_PROXY);
        if (host != null) {
            sHttpClient.getParams().removeParameter(ConnRouteParams.DEFAULT_PROXY);
        }
        return sHttpClient.execute(post);

    }
    
}
