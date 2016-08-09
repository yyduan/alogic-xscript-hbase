package com.alogic.xscript.hbase;

import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

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
	protected String cid = "$kv-conf";
	
	public HConf(String tag, Logiclet p) {
		super(tag, p);
		
		registerModule("h-admin",HAdmin.class);
		registerModule("h-table",HBaseTable.class);
		registerModule("h-create",HCreate.class);
		registerModule("h-drop",HDrop.class);
		registerModule("h-get",HGet.class);
		registerModule("h-put",HPut.class);
		registerModule("h-delete",HDelete.class);
		registerModule("h-scan",HScan.class);
	}

	@Override
	public void configure(Properties p){
		super.configure(p);
		
		cid = PropertiesConstants.getString(p,"cid",cid);
	}
	
	@Override
	protected void onExecute(Map<String, Object> root,
			Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
		//此处获取Configuration的方式待优化
		Configuration conf = HBaseConfiguration.create();
		
		try {
			ctx.setObject(cid, conf);
			super.onExecute(root, current, ctx, watcher);
		}finally{
			ctx.removeObject(cid);
		}
	}	
}
