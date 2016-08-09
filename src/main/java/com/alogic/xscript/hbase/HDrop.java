package com.alogic.xscript.hbase;

import java.util.Map;

import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;

/**
 * 在hbase中删除指定的表
 * 
 * @author duanyy
 *
 */
public class HDrop extends HAdminOperation{

	public HDrop(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	protected void onExecute(HBaseAdmin row, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		// to be defined
	}

}
