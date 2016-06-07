package com.xsl.data.collect.support;

import com.xsl.data.collect.util.JMXPollUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by howard on 16/6/7.
 */
@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @RequestMapping(value = "/counter", method = {RequestMethod.GET})
    public Map<String, String> counter() {
        return JMXPollUtil.getAllMBeans();
    }
}
