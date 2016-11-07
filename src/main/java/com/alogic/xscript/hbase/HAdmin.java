package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.plugins.Segment;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 创建一个HBaseAdmin环境
 * 
 * @author duanyy
 *
 */
public class HAdmin extends Segment {
    protected String pid = "$h-conf";
    protected String cid = "$h-admin";

    public HAdmin(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    public void configure(Properties p) {
        super.configure(p);

        cid = PropertiesConstants.getString(p, "cid", cid, true);
        pid = PropertiesConstants.getString(p, "pid", pid, true);
    }

    @Override
    protected void onExecute(Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        // 此处获取Configuration的方式待优化
        Configuration conf = ctx.getObject(pid);
        if (conf == null) {
            throw new BaseException("core.no_conf", "It must be in a h-conf context,check your script.");
        }
        //System.out.println(conf.get("hbase.zookeeper.quorum"));
        //System.out.println(conf.get("zookeeper.znode.parent"));
        try {
            HBaseAdmin admin = new HBaseAdmin(conf);
            ctx.setObject(cid, admin);
            super.onExecute(root, current, ctx, watcher);
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
