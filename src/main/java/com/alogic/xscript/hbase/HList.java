package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.anysoft.util.BaseException;

/**
 * HBase列出所有表
 * 
 * @author heweiguo
 *
 */
public class HList extends HAdminOperation {

    public HList(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    protected void onExecute(HBaseAdmin hBaseAdmin, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        String tagValue = ctx.transform(tag);
        if (StringUtils.isNotEmpty(tagValue)) {
            List<Object> data = new ArrayList<Object>();
            try {
                HTableDescriptor[] tableDescriptor = hBaseAdmin.listTables();
                // printing all the table names.
                for (int i = 0; i < tableDescriptor.length; i++) {
                    data.add(tableDescriptor[i].getNameAsString());
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                // e.printStackTrace();
                log(String.format("list tables error,msg:[%s]", e.toString()), "error");
                throw new BaseException("core.io_exception", e.getMessage());
            }
            current.put(tagValue, data);
        }
    }
}
