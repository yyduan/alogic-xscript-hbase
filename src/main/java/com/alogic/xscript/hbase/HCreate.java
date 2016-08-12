package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.anysoft.util.BaseException;

/**
 * 在hbase中创建表
 * 
 * @author duanyy
 *
 */
public class HCreate extends HAdminOperation {

    public HCreate(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    protected void onExecute(HBaseAdmin hBaseAdmin, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        if (StringUtils.isEmpty(tname)) {
            throw new BaseException("core.no_tname", "It must be in a h-create context,check your script.");
        }
        if (StringUtils.isEmpty(cfy)) {
            throw new BaseException("core.no_cfy", "It must be in a h-create context,check your script.");
        }

        try {
            if (hBaseAdmin.tableExists(tname)) {
                hBaseAdmin.disableTable(tname);
                hBaseAdmin.deleteTable(tname);
                log(String.format("[%s] is exist,detele....", tname), "warn");
                // throw new BaseException("core.tableExists",
                // "create table '"+tname+"' exist,check your script.");
            }
            // 新建一个students表的描述
            HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tname));
            String[] columnFamilys = cfy.split(",");
            // 在描述里添加列族
            for (String columnFamily : columnFamilys) {
                tableDesc.addFamily(new HColumnDescriptor(columnFamily));
            }
            // 根据配置好的描述建表
            hBaseAdmin.createTable(tableDesc);
            log(String.format("create [%s] success!", tname), "info");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            log(String.format("create [%s] error,msg:[%s]", tname, e.toString()), "error");
            throw new BaseException("core.io_exception", e.getMessage());
        }
    }

}
