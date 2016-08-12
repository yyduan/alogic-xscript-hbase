package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.anysoft.util.BaseException;

public class HGet extends HTableOperation {

    /**
     * 获取数据的默认编码
     */
    protected static String CHARSET_NAME = "UTF-8";

    public HGet(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    protected void onExecute(HTable hTable, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        if (StringUtils.isEmpty(row)) {
            throw new BaseException("core.no_row", "It must be in a h-get context,check your script.");
        }
        Get get = new Get(Bytes.toBytes(row));
        byte[][] fcBytes = getFamilyAndColumnBytes();
        if (fcBytes != null) {
            if (fcBytes[1] != null) {
                get.addColumn(fcBytes[0], fcBytes[1]);
            } else {
                get.addFamily(fcBytes[0]);
            }
        }
        // if (StringUtils.isNotEmpty(col)) {
        // byte[] family = null;
        // byte[] column = null;
        // if (col.indexOf(":") > -1) {
        // String[] vars = col.split(":");
        // family = Bytes.toBytes(vars[0]);
        // column = Bytes.toBytes(vars[1]);
        // get.addColumn(family, column);
        // } else {
        // family = Bytes.toBytes(col);
        // get.addFamily(family);
        // }
        // }
        try {
            String tagValue = ctx.transform(tag);
            Map<Object, Object> data = new HashMap<>();
            if (StringUtils.isNotEmpty(tagValue)) {
                Result result = hTable.get(get);
                String family;
                String qualifier;
                String value;
                if (result.size() > 0) {
                    for (Cell cell : result.listCells()) {
                        family = new String(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength(), CHARSET_NAME);
                        qualifier = new String(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength(), CHARSET_NAME);
                        value = new String(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength(), CHARSET_NAME);
                        if (StringUtils.isNotEmpty(qualifier)) {
                            data.put(family + ":" + qualifier, value);
                        } else {
                            data.put(family, value);
                        }
                    }
                }
            }
            current.put(tagValue, data);
        } catch (IOException e) {
            throw new BaseException("core.hbase_get_error", e.getMessage());
        }
    }

}
