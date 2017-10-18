package com.alogic.xscript.hbase;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.doc.XsObject;
import com.alogic.xscript.plugins.Segment;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 设置conf的额外参数
 * 
 * @author hwg
 *
 */
public class HConfSet extends Segment {
	protected String pid = "$h-conf";
	protected String id = "";
	protected String value = "";

	public HConfSet(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	public void configure(Properties p) {
		super.configure(p);
		pid = PropertiesConstants.getString(p, "pid", pid, true);
		id = PropertiesConstants.getString(p, "id", id, true);
		value = PropertiesConstants.getString(p, "value", value, true);
	}

	@Override
	protected void onExecute(XsObject root, XsObject current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		// 此处获取Configuration的方式待优化
		Configuration conf = ctx.getObject(pid);
		if (conf == null) {
			throw new BaseException("core.no_conf", "It must be in a h-conf context,check your script.");
		}
		if (StringUtils.isEmpty(id)) {
			throw new BaseException("core.no_id", "It must be in a h-conf-set context,check your script.");
		}
		if (StringUtils.isEmpty(value)) {
			throw new BaseException("core.no_value", "It must be in a h-conf-set context,check your script.");
		}
		conf.set(id, value);
		// ctx.setObject(pid, conf);
		/*
		 * try { super.onExecute(root, current, ctx, watcher); } finally { //
		 * ctx.removeObject(pid); }
		 */

	}
}
