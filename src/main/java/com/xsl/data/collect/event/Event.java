package com.xsl.data.collect.event;

import java.util.Map;

/**
 * Created by howard on 16/4/12.
 */
public interface Event {

    /**
     * get headers
     * @return Map<String, String>
     */
    Map<String, String> getHeaders();

    /**
     * set headers
     * @param headers
     */
    void setHeaders(Map<String, String> headers);

    /**
     * get body
     * @return
     */
    byte[] getBody();

    /**
     * set body
     * @param body
     */
    void setBody(byte[] body);
}
