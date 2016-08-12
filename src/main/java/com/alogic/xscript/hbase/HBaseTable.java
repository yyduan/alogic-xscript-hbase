package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HTable;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.plugins.Segment;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 创建一个HTable环境
 * 
 * @author duanyy
 *
 */
public class HBaseTable extends Segment {
    protected String pid = "$kv-conf";
    protected String cid = "$kv-table";
    protected String tname = "";

    public HBaseTable(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    public void configure(Properties p) {
        super.configure(p);

        cid = PropertiesConstants.getString(p, "cid", cid, true);
        pid = PropertiesConstants.getString(p, "pid", pid, true);
        tname = PropertiesConstants.getString(p, "tname", tname, true);
    }

    @Override
    protected void onExecute(Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        // 此处获取Configuration的方式待优化
        Configuration conf = ctx.getObject(pid);
        if (conf == null) {
            throw new BaseException("core.no_conf", "It must be in a h-conf context,check your script.");
        }
        try {
            String tableName = ctx.transform(tname);
            if (StringUtils.isNotEmpty(tname)) {
                HTable t = new HTable(conf, tableName);
                ctx.setObject(cid, t);
                super.onExecute(root, current, ctx, watcher);
            }
        } catch (MasterNotRunningException e) {
            throw new BaseException("core.master_not_running", e.getMessage());
        } catch (ZooKeeperConnectionException e) {
            throw new BaseException("core.zk_connect", e.getMessage());
        } catch (IOException e) {
            throw new BaseException("core.io_exception", e.getMessage());
        } finally {
            ctx.removeObject(cid);
        }
    }
}
