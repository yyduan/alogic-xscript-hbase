package com.alogic.xscript.hbase;

import java.util.Map;

import org.apache.hadoop.hbase.client.HTable;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;

public class HScan extends HTableOperation {

	public HScan(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	protected void onExecute(HTable row, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		// TODO Auto-generated method stub

	}

}
