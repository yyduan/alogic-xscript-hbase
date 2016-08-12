package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.anysoft.util.BaseException;

/**
 * 在hbase中删除指定的表
 * 
 * @author duanyy
 *
 */
public class HDrop extends HAdminOperation {

    public HDrop(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    protected void onExecute(HBaseAdmin hBaseAdmin, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        if (StringUtils.isEmpty(tname)) {
            throw new BaseException("core.no_tname", "It must be in a h-drop context,check your script.");
        }
        try {
            // 第2步
            // 使用HBaseAdmin类的disableTable()方法禁止表。
            hBaseAdmin.disableTable(tname);
            // 第3步
            // 现在使用HBaseAdmin类的deleteTable()方法删除表。
            hBaseAdmin.deleteTable(tname);
            log(String.format("drop [%s] success!", tname), "info");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            log(String.format("drop [%s] error,msg:[%s]", tname, e.toString()), "error");
            throw new BaseException("core.io_exception", e.getMessage());
        }
    }

}
