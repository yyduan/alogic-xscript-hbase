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
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 在hbase中创建表
 * 
 * @author duanyy
 *
 */
public class HCreate extends HAdminOperation {

    /**
     * 列族：多个用逗号间隔
     */
    protected String cf = "";

    /**
     * 表名
     */
    protected String tname = "";

    /**
     * 如果已存在表是否被覆盖，默认不覆盖false，覆盖=true
     */
    protected String cover = "false";

    public HCreate(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    public void configure(Properties p) {
        super.configure(p);

        //cf = PropertiesConstants.getString(p, "cf", cf, true);
        cf = p.GetValue("cf", cf, false, true);
        //tname = PropertiesConstants.getString(p, "tname", tname, true);
        tname = p.GetValue("tname", tname, false, true);
        //cover = PropertiesConstants.getBoolean(p, "cover", cover, true);
        cover = p.GetValue("cover", cover, false, true);

    }

    @Override
    protected void onExecute(HBaseAdmin hBaseAdmin, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
    	System.out.println(tname);
        if (StringUtils.isEmpty(tname)) {
            throw new BaseException("core.no_tname", "It must be in a h-create context,check your script.");
        }
        if (StringUtils.isEmpty(cf)) {
            throw new BaseException("core.no_cf", "It must be in a h-create context,check your script.");
        }
        tname = ctx.transform(tname);
        cf = ctx.transform(cf);
        System.out.println(tname);

        try {
            if (hBaseAdmin.tableExists(tname)) {
                if (Boolean.parseBoolean(ctx.transform(cover))) {
                    hBaseAdmin.disableTable(tname);
                    hBaseAdmin.deleteTable(tname);
                    log(String.format("[%s] is exist,detele....", tname), "warn");
                } else {
                    throw new BaseException("core.tableExists", "create table '" + tname + "' exist,check your script.");
                }
            }
            // 新建一个students表的描述
            HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tname));
            String[] columnFamilys = cf.split(",");
            // 在描述里添加列族
            for (String columnFamily : columnFamilys) {
                tableDesc.addFamily(new HColumnDescriptor(columnFamily));
            }
            // 根据配置好的描述建表
            hBaseAdmin.createTable(tableDesc);
            System.out.println(cf);
            log(String.format("create [%s] success!", tname), "info");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            log(String.format("create [%s] error,msg:[%s]", tname, e.toString()), "error");
            throw new BaseException("core.io_exception", e.getMessage());
        }
    }

}
