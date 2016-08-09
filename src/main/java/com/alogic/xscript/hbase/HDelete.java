package com.alogic.xscript.hbase;

import java.util.Map;

import org.apache.hadoop.hbase.client.HTable;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;

/**
 * 执行Delete操作
 * 
 * @author duanyy
 *
 */
public class HDelete extends HTableOperation{

	public HDelete(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	protected void onExecute(HTable row, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		// to be define
	}

}
