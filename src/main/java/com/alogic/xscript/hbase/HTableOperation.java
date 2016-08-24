package com.alogic.xscript.hbase;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.HTable;

import com.alogic.xscript.AbstractLogiclet;
import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 基于HTable的操作
 * 
 * @author duanyy
 *
 */
public abstract class HTableOperation extends AbstractLogiclet {
    /**
     * hadmin的cid
     */
    private String pid = "$h-table";
    /**
     * 数据集
     */
    protected String tag = "data";

    /**
     * 获取数据的默认编码
     */
    protected static String CHARSET_NAME = "UTF-8";

    /**
     * 返回结果的id
     */
    protected String id;

    public HTableOperation(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    public void configure(Properties p) {
        super.configure(p);
        pid = PropertiesConstants.getString(p, "pid", pid, true);
        tag = PropertiesConstants.getString(p, "tag", tag, true);
        id = PropertiesConstants.getString(p, "id", "$" + getXmlTag(), true);

    }

    @Override
    protected void onExecute(Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        HTable hTable = ctx.getObject(pid);
        if (hTable == null) {
            throw new BaseException("core.no_htable", "It must be in a h-table context,check your script.");
        }
        if (StringUtils.isNotEmpty(id)) {
            onExecute(hTable, root, current, ctx, watcher);
        }
    }

    protected abstract void onExecute(HTable hTable, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher);

}