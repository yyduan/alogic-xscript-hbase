package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.plugins.Segment;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 采用loginUserFromKeytab登录配置
 * 
 * 需要user和path个参数
 * 
 * @author hwg
 *
 */
public class HConfUser extends Segment {
	protected String pid = "$h-conf";
	protected String path = "";
	protected String user = "";

	public HConfUser(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	public void configure(Properties p) {
		super.configure(p);
		pid = PropertiesConstants.getString(p, "pid", pid, true);
		path = PropertiesConstants.getString(p, "path", path, true);
		user = PropertiesConstants.getString(p, "user", user, true);
	}

	@Override
	protected void onExecute(Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		// 此处获取Configuration的方式待优化
		Configuration conf = ctx.getObject(pid);
		if (conf == null) {
			throw new BaseException("core.no_conf", "It must be in a h-conf context,check your script.");
		}
		if (StringUtils.isEmpty(path)) {
			throw new BaseException("core.no_path", "It must be in a h-conf-user context,check your script.");
		}
		if (StringUtils.isEmpty(user)) {
			throw new BaseException("core.no_value", "It must be in a h-conf-user context,check your script.");
		}
		UserGroupInformation.setConfiguration(conf);
		try {
			UserGroupInformation.loginUserFromKeytab(user, path);
		} catch (IOException e) {
			log("loginUserFromKeytab fail,e:" + e.toString(), "error");
		}

	}
}
