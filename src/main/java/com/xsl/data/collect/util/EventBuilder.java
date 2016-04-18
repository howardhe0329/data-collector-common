package com.xsl.data.collect.util;

import com.xsl.data.collect.event.Event;
import com.xsl.data.collect.event.SimpleEvent;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by howard on 16/4/14.
 */
public class EventBuilder {

    public static Event withBody(byte[] body, Map<String, String> headers) {
        Event event = new SimpleEvent();
        if (body == null) {
            body = new byte[0];
        }
        event.setBody(body);
        if (headers != null) {
            event.setHeaders(new HashMap<>(headers));
        }
        return event;
    }

    public static Event withBody(byte[] body) {
        return withBody(body, null);
    }

    public static Event withBody(String body, Charset charset,
                                 Map<String, String> headers) {
        return withBody(body.getBytes(charset), headers);
    }

    public static Event withBody(String body, Charset charset) {
        return withBody(body, charset, null);
    }
}
