package com.xsl.data.collect.event;

import com.xsl.data.collect.common.DataCollectException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by howard on 16/4/12.
 */
public class JSONEvent implements Event, Serializable {

    private Map<String, String> headers;
    private String body;
    private transient String charset = "UTF-8";

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
        if(body != null) {
            try {
                return body.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                throw new DataCollectException(String.format("%s encoding not supported", charset), e);
            }
        } else {
            return new byte[0];
        }
    }

    @Override
    public void setBody(byte[] body) {
        if(body != null) {
            this.body = new String(body);
        } else {
            this.body = "";
        }
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    @Override
    public String toString() {
        return "JSONEvent{" +
                "headers=" + headers +
                ", body='" + body + '\'' +
                ", charset='" + charset + '\'' +
                '}';
    }
}
