package com.alogic.xscript.hbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alogic.pool.impl.Singleton;
import com.anysoft.util.Properties;
import com.anysoft.util.XmlElementProperties;
import com.anysoft.util.XmlTools;

/**
 * @author heweiguo
 *
 */
public class HConfFactory extends Singleton {

	protected static final Logger logger = LogManager.getLogger(HConfFactory.class);
	
    public static String krb5 = null;
    public static String core_site_xml = null;
    public static String hbase_site_xml = null;
    public static String loginUser = null;
    public static String keytabPath = null;

    @Override
    public void configure(Properties p) {
        // nothing to do
    }

    @Override
    public void configure(Element e, Properties p) {
        Properties props = new XmlElementProperties(e, p);

        NodeList nodeList = XmlTools.getNodeListByPath(e, "parameter");

        Field[] fields = this.getClass().getDeclaredFields();
        Object obj = null;
        try {
            obj = this.getClass().newInstance();
            for (Field field : fields) {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node n = nodeList.item(i);
                    if (n.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    Element element = (Element) n;
                    if (field.getName().equals(element.getAttribute("id"))) {
                        field.setAccessible(true);
                        field.set(obj, field.getType().cast(element.getAttribute("value")));
                    }
                }
            }
        } catch (Exception exc) {
        	 logger.error("error"+exc);
        }

        configure(props);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <pooled> pooled createObject(int priority, int timeout) {
        return (pooled) create();
    }

    public Configuration create() {

        // String callType = System.getProperty("os.name");
        // 1.加入krb5.ini文件
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        if (krb5 != null) {
            System.setProperty("java.security.krb5.conf", krb5);
        }
        // 此处获取Configuration的方式待优化
        Configuration conf = HBaseConfiguration.create();
        // 载入core-size.xml和hbase-site.xml必要文件
        try {
            if (core_site_xml != null) {
                conf.addResource(new FileInputStream(new File(core_site_xml)));
            }
        } catch (FileNotFoundException e) {
        	logger.error(String.format("Can not find the file[%s]", core_site_xml));
        }
        try {
            if (hbase_site_xml != null) {
                conf.addResource(new FileInputStream(new File(hbase_site_xml)));
            }
        } catch (FileNotFoundException e) {
        	logger.error(String.format("Can not find the file[%s]", hbase_site_xml));
        }

        // 采用loginUserFromKeytab登录，需要user和keytab
        if (loginUser != null && keytabPath != null) {
            UserGroupInformation.setConfiguration(conf);
            try {
                UserGroupInformation.loginUserFromKeytab(loginUser, keytabPath);
            } catch (IOException e) {
                logger.error("loginUserFromKeytab fail,e:" + e.toString());
            }
        }
        return conf;
    }

}