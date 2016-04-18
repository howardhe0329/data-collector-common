package com.xsl.data.collect.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by howard on 16/4/14.
 */
public class SimpleEvent implements Event {

    private Map<String, String> headers;
    private byte[] body;

    public SimpleEvent() {
        headers = new HashMap<>();
        body = new byte[0];
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public byte[] getBody() {
        return body;
    }

    @Override
    public void setBody(byte[] body) {
        if(body == null){
            body = new byte[0];
        }
        this.body = body;
    }

    @Override
    public String toString() {
        Integer bodyLen = null;
        if (body != null) bodyLen = body.length;
        return "[Event headers = " + headers + ", body.length = " + bodyLen + " ]";
    }
}
