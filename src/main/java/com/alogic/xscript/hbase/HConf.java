package com.alogic.xscript.hbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.security.UserGroupInformation;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.plugins.Segment;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 创建一个HBaseConfiguration环境
 * 
 * @author duanyy
 *
 */
public class HConf extends Segment {
    protected String cid = "$h-conf";

    protected String krb = null;
    protected String callType = "linux";
    protected String core_site_xml = null;
    protected String hbase_site_xml = null;
    protected String loginUser = null;
    protected String keytabPath = null;

    public HConf(String tag, Logiclet p) {
        super(tag, p);

        registerModule("h-admin", HAdmin.class);
        registerModule("h-table", HBaseTable.class);
        registerModule("h-create", HCreate.class);
        registerModule("h-drop", HDrop.class);
        registerModule("h-list", HList.class);
        registerModule("h-get", HGet.class);
        registerModule("h-put", HPut.class);
        registerModule("h-delete", HDelete.class);
        registerModule("h-scan", HScan.class);
    }

    @Override
    public void configure(Properties p) {
        super.configure(p);

        cid = PropertiesConstants.getString(p, "cid", cid);
        krb = PropertiesConstants.getString(p, "krb.ini", krb);
        core_site_xml = PropertiesConstants.getString(p, "core.site.xml", core_site_xml);
        callType = PropertiesConstants.getString(p, "callType", callType);
        hbase_site_xml = PropertiesConstants.getString(p, "hbase.site.xml", hbase_site_xml);
        loginUser = PropertiesConstants.getString(p, "loginUser", loginUser);
        keytabPath = PropertiesConstants.getString(p, "keytabPath", keytabPath);
    }

    @Override
    protected void onExecute(Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        // 此处获取Configuration的方式待优化

        // 1.加入krb5.ini文件，用于window请求
        if ("windows".equals(callType)) {
            if (krb == null) {
                throw new BaseException("core.no_krb", "windows must has krb.int file,check your script.");
            }
            System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
            System.setProperty("java.security.krb5.conf", krb);
        }
        Configuration conf = HBaseConfiguration.create();
        // 载入core-size.xml和hbase-site.xml必要文件
        try {
            if (core_site_xml != null) {
                conf.addResource(new FileInputStream(new File(core_site_xml)));
            }
        } catch (FileNotFoundException e) {
            log(String.format("Can not find the file[%s]", core_site_xml), "error");
        }
        try {
            if (hbase_site_xml != null) {
                conf.addResource(new FileInputStream(new File(hbase_site_xml)));
            }
        } catch (FileNotFoundException e) {
            log(String.format("Can not find the file[%s]", hbase_site_xml), "error");
        }

        UserGroupInformation.setConfiguration(conf);
        // 3.window下，采用loginUserFromKeytab登录，需要user和keytab
        if (loginUser != null && keytabPath != null) {
            try {
                UserGroupInformation.loginUserFromKeytab(loginUser, keytabPath);
            } catch (IOException e) {
                // e.printStackTrace();
                log("loginUserFromKeytab fail,e:" + e.toString(), "error");
            }
        }
        // System.err.println("===================conf:" + conf);
        // HTable table;
        // try {
        // table = new HTable(conf, "ems:archive_data");
        // Scan scan = new Scan();
        // ResultScanner results;
        // results = table.getScanner(scan);
        // for (Result result : results) {
        // for (Cell cell : result.rawCells()) {
        // System.err.println("RowName:" + new String(CellUtil.cloneRow(cell)) +
        // " ");
        // System.err.println("Timetamp:" + cell.getTimestamp() + " ");
        // System.err.println("column Family:" + new
        // String(CellUtil.cloneFamily(cell)) + " ");
        // System.err.println("row Name:" + new
        // String(CellUtil.cloneQualifier(cell)) + " ");
        // System.err.println("value:" + new String(CellUtil.cloneValue(cell)) +
        // " ");
        // }
        // }
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        try {
            ctx.setObject(cid, conf);
            super.onExecute(root, current, ctx, watcher);
        } finally {
            ctx.removeObject(cid);
        }
    }
}
