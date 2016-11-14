package com.alogic.xscript.hbase;

import java.io.InterruptedIOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.hbase.util.FColumnUtil;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

public class HPut extends HTableOperation {

    /**
     * 行名rowkey
     */
    protected String row = "";
    /**
     * 列名(包含列族:列名)
     */
    protected String col = "";

    /**
     * 列值
     */
    protected String value = "";

    public HPut(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    public void configure(Properties p) {
        super.configure(p);
        //row = PropertiesConstants.getString(p, "row", row, true);
        //col = PropertiesConstants.getString(p, "col", col, true);
        //value = PropertiesConstants.getString(p, "value", value, true);
        row = p.GetValue("row", row, false, true);
        col = p.GetValue("col", col, false, true);
        value = p.GetValue("value", value, false, true);
    }

    @Override
    protected void onExecute(HTable hTable, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        if (StringUtils.isEmpty(row)) {
            throw new BaseException("core.no_row", "It must be in a h-put context,check your script.");
        }
        if (StringUtils.isEmpty(col)) {
            throw new BaseException("core.no_col", "It must be in a h-put context,check your script.");
        }
        if (StringUtils.isEmpty(value)) {
            throw new 
            
            BaseException("core.no_col_value", "It must be in a h-put context,check your script.");
        }
        
        String rowkey = ctx.transform(row);
        String colName = ctx.transform(col);
        String colValue = ctx.transform(value);
        // Put
        // 类的add()方法用于插入数据。它需要表示列族，列限定符（列名称）3字节阵列，并要插入的值。将数据插入HBase表使用add()方法
        Put p = new Put(Bytes.toBytes(rowkey));
        byte[] column = null;
        byte[] family = null;
        byte[][] fcBytes = FColumnUtil.getFamilyAndColumnBytes(colName);
        ;
        if (fcBytes != null) {
            family = fcBytes[0];
            column = fcBytes[1];
        }
        p.add(family, column, Bytes.toBytes(colValue));
        try {
            hTable.put(p);
        } catch (RetriesExhaustedWithDetailsException e) {
            throw new BaseException("core.hbase_put_error", e.getMessage());
        } catch (InterruptedIOException e) {
            throw new BaseException("core.hbase_put_io", e.getMessage());
        }
        log(String.format("put row[%s] success!", rowkey), "info");
    }
}
