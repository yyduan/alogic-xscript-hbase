package com.alogic.xscript.hbase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import com.alogic.pool.Pool;
import com.alogic.pool.PoolNaming;
import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.plugins.Segment;
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

	/**
	 * krb文件
	 */
	protected String krb = null;

	protected String core_site_xml = null;
	protected String hbase_site_xml = null;

	protected String poolId = null;

	public HConf(String tag, Logiclet p) {
		super(tag, p);

		registerModule("h-conf-set", HConfSet.class);
		registerModule("h-conf-user", HConfUser.class);
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
		hbase_site_xml = PropertiesConstants.getString(p, "hbase.site.xml", hbase_site_xml);
		poolId = PropertiesConstants.getString(p, "poolId", poolId);
	}

	@Override
	protected void onExecute(Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		// String callType = System.getProperty("os.name");
		// 1.加入krb5.ini文件
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
				"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		if (krb != null) {
			System.setProperty("java.security.krb5.conf", krb);
		}
		// 此处获取Configuration的方式待优化
		// Configuration conf = HBaseConfiguration.create();
		// 载入core-size.xml和hbase-site.xml必要文件
		PoolNaming naming = PoolNaming.get();
		if (poolId == null) {
			poolId = "default";
		}
		Pool pool = naming.lookup(poolId);

		Configuration conf = null;
		try {
			conf = pool.borrowObject(0, 0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			conf = null;
		}
		log(String.format("@@@@=====get hbase pool conf=", conf), "info");
		if (conf == null) {
			conf = HBaseConfiguration.create();
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

			// // 不同hbase集群下不同配置zookeeper
			// if (hbaseZookeeperQuorum != null) {
			// conf.set("hbase.zookeeper.quorum", hbaseZookeeperQuorum);
			// }
			// if (zookeeperZnodeParent != null) {
			// conf.set("zookeeper.znode.parent", zookeeperZnodeParent);
			// }

			// loginUser = "ems/h2m2.ecloud.com";
			// keytabPath = "C:\\ems.app.keytab";
			Iterator<Entry<String, String>> iter = conf.iterator();
			Entry<String, String> entry;
			while (iter.hasNext()) {
				entry = iter.next();
				System.err.println("====" + entry.getKey() + "=" + entry.getValue());
			}
			// System.err.println("====:" + JSON.toString(conf));
			// UserGroupInformation.setConfiguration(conf);
			// // 3.window下，采用loginUserFromKeytab登录，需要user和keytab
			// if (loginUser != null && keytabPath != null) {
			// try {
			// UserGroupInformation.loginUserFromKeytab(loginUser, keytabPath);
			// } catch (IOException e) {
			// // e.printStackTrace();
			// log("loginUserFromKeytab fail,e:" + e.toString(), "error");
			// }
			// }
		}

		try {
			ctx.setObject(cid, conf);
			super.onExecute(root, current, ctx, watcher);
		} finally {
			ctx.removeObject(cid);
		}
	}
}
