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
public abstract class HAdminOperation extends AbstractLogiclet{
	/**
	 * hadmin的cid
	 */
    private String pid = "$h-admin";
	
    /**
     * 列族：多个用逗号间隔
     */
    protected String cfy = "";

    /**
     * 表名
     */
    protected String tname = "";

    /**
     * 数据集
     */
    protected String tag = "data";
	/**
	 * 返回结果的id
	 */
	protected String id;
	
	public HAdminOperation(String tag, Logiclet p) {
		super(tag, p);
	}
	
	@Override
    public void configure(Properties p){
		super.configure(p);
		pid = PropertiesConstants.getString(p,"pid", pid,true);
		id = PropertiesConstants.getString(p,"id", "$" + getXmlTag(),true);
        cfy = PropertiesConstants.getString(p, "cfy", cfy, true);
        tname = PropertiesConstants.getString(p, "tname", tname, true);
        tag = PropertiesConstants.getString(p, "tag", tag, true);
	}

	@Override
	protected void onExecute(Map<String, Object> root,
			Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		HBaseAdmin hAdmin = ctx.getObject(pid);
		if (hAdmin == null){
			throw new BaseException("core.no_hadmin","It must be in a h-admin context,check your script.");
		}
		if (StringUtils.isNotEmpty(id)){
			onExecute(hAdmin,root,current,ctx,watcher);
		}
	}

    protected abstract void onExecute(HBaseAdmin hBaseAdmin, Map<String, Object> root,
			Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher);
		
}