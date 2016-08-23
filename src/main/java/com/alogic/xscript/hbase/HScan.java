package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.anysoft.util.BaseException;

public class HScan extends HTableOperation {

    public HScan(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    protected void onExecute(HTable hTable, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        Scan scan = new Scan();
        byte[][] fcBytes = getFamilyAndColumnBytes();
        if (fcBytes != null) {
            if (fcBytes[1] != null) {
                scan.addColumn(fcBytes[0], fcBytes[1]);
            } else {
                scan.addFamily(fcBytes[0]);
            }
        }
        try {
            String tagValue = ctx.transform(tag);
            Map<String, Object> rows = new HashMap<String, Object>();
            if (StringUtils.isNotEmpty(tagValue)) {
                if (StringUtils.isNotEmpty(srow)) {
                    scan.setStartRow(Bytes.toBytes(srow));
                }
                if (StringUtils.isNotEmpty(erow)) {
                    scan.setStopRow(Bytes.toBytes(erow));
                }
                if (stime >= 0 && etime >= 0) {
                    scan.setTimeRange(stime, etime);
                }
                if (mvers >= 0) {
                    scan.setMaxVersions(mvers);
                }
                FilterList flist = doFilter();
                if (flist != null) {
                    scan.setFilter(flist);
                }
                ResultScanner rs = hTable.getScanner(scan);
                String row;
                Map<String, Object> data;
                for (Result r : rs) {
                    row = new String(r.getRow());
                    data = new HashMap<String, Object>();
                    if (r.size() > 0) {
                        String family;
                        String qualifier;
                        String value;
                        for (Cell cell : r.listCells()) {
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
                    rows.put(row, data);
                }
            }
            current.put(tagValue, rows);
        } catch (IOException e) {
            // e.printStackTrace();
            throw new BaseException("core.hbase_scan_error", e.getMessage());
        }
    }

}
