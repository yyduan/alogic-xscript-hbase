package com.alogic.xscript.hbase;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.alogic.xscript.AbstractLogiclet;
import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * HAdmin操作基类
 * 
 * @author duanyy
 *
 */
public abstract class HAdminOperation extends AbstractLogiclet {
	/**
	 * hadmin的cid
	 */
	private String pid = "$h-admin";

	/**
	 * 返回结果的id
	 */
	protected String id;

	public HAdminOperation(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	public void configure(Properties p) {
		super.configure(p);
		pid = PropertiesConstants.getString(p, "pid", pid, true);
		id = PropertiesConstants.getString(p, "id", "$" + getXmlTag(), true);
	}

	@Override
	protected void onExecute(Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		HBaseAdmin hAdmin = ctx.getObject(pid);
		if (hAdmin == null) {
			throw new BaseException("core.no_hadmin", "It must be in a h-admin context,check your script.");
		}
		if (StringUtils.isNotEmpty(id)) {
			onExecute(hAdmin, root, current, ctx, watcher);
		}
	}

	protected abstract void onExecute(HBaseAdmin hBaseAdmin, Map<String, Object> root, Map<String, Object> current,
			LogicletContext ctx, ExecuteWatcher watcher);

}