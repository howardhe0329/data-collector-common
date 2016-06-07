package com.xsl.data.collect.util;

import com.xsl.data.collect.metrics.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by howard on 16/6/7.
 */
public class JMXPollUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JMXPollUtil.class);
    private static MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

    public static Map<String, String> getAllMBeans() {
        Map<String, String> resultMap = new HashMap<>();
        Set<ObjectInstance> objectInstanceSet = mBeanServer.queryMBeans(null, null);
        String packagePath = Counter.class.getPackage().getName();
        for (ObjectInstance objectInstance : objectInstanceSet) {
            if(!objectInstance.getObjectName().toString().startsWith(packagePath)) {
                continue;
            }
            try {
                MBeanInfo mbeanInfo = mBeanServer.getMBeanInfo(objectInstance.getObjectName());
                MBeanAttributeInfo[] attrs = mbeanInfo.getAttributes();
                String[] attributes = new String[attrs.length];
                for (int i = 0; i < attrs.length; i++) {
                    attributes[i] = attrs[i].getName();
                }
                AttributeList attributeList = mBeanServer.getAttributes(objectInstance.getObjectName(), attributes);
                for (Object attr : attributeList) {
                    Attribute localAttr = (Attribute) attr;
                    resultMap.put(localAttr.getName(), localAttr.getValue().toString());
                }
            } catch (Exception e) {
                LOGGER.error("Unable to poll JMX for metrics.", e);
            }
        }
        return resultMap;
    }
}
