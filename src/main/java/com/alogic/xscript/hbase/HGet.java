package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;
import org.w3c.dom.Element;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.hbase.util.FColumnUtil;
import com.alogic.xscript.hbase.util.FilterBuilder;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.XmlElementProperties;
import com.anysoft.util.XmlTools;

public class HGet extends HTableOperation {
	/**
	 * Filter Builder
	 */
	protected FilterBuilder fb = null;
    /**
     * 指定查询开始时间戳
     */
    protected String stime = "-1";
    /**
     * 指定查询结束时间戳
     */
    protected String etime = "-1";
    
    /**
     * 列出的版本最大数
     */
    protected String mvers = "-1";

    /**
     * 行名rowkey
     */
    protected String row = "";
    /**
     * 列名(包含列族:列名)
     */
    protected String col = "";

    public HGet(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    public void configure(Properties p){
    	super.configure(p);
        // stime = PropertiesConstants.getLong(p, "stime", -1, true);
        // etime = PropertiesConstants.getLong(p, "etime", -1, true);
        // mvers = PropertiesConstants.getInt(p, "version", -1, true);
        // row = PropertiesConstants.getString(p, "row", row, true);
        // col = PropertiesConstants.getString(p, "col", col, true);
        stime = p.GetValue("stime", stime, false, true);
        etime = p.GetValue("etime", etime, false, true);
        mvers = p.GetValue("version", mvers, false, true);
        row = p.GetValue("row", row, false, true);
        col = p.GetValue("col", col, false, true);
    }
    
    @Override
    public void configure(Element e,Properties p){
    	Properties props = new XmlElementProperties(e,p);
    	
    	Element filter = XmlTools.getFirstElementByPath(e, "filter");
    	if (filter != null){
	    	FilterBuilder.TheFactory f = new FilterBuilder.TheFactory();
	    	try {
	    		fb = f.newInstance(filter, props, "module");
	    	}catch (Exception ex){
	    		log("Can not create instance of FilterBuilder.","error");
	    	}
    	}
    	configure(props);
    }    
    
    @Override
    protected void onExecute(HTable hTable, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        Long startTime = Long.parseLong(ctx.transform(stime));
        Long endTime = Long.parseLong(ctx.transform(etime));
        int mversion = Integer.parseInt(ctx.transform(mvers));
        row = ctx.transform(row);
        col = ctx.transform(col);
        // System.err.println("==========transform==row:" + row);
        // System.err.println("==========transform==col:" + col);
        if (StringUtils.isEmpty(row)) {
            throw new BaseException("core.no_row", "It must be in a h-get context,check your script.");
        }
        Get get = new Get(Bytes.toBytes(row));
        byte[][] fcBytes = FColumnUtil.getFamilyAndColumnBytes(col);
        if (fcBytes != null) {
            if (fcBytes[1] != null) {
                get.addColumn(fcBytes[0], fcBytes[1]);
            } else {
                get.addFamily(fcBytes[0]);
            }
        }
        try {
            String tagValue = ctx.transform(tag);
            Map<String, List<Object>> data = new HashMap<String, List<Object>>();
            if (StringUtils.isNotEmpty(tagValue)) {
                if (startTime >= 0 && endTime >= 0) {
                    get.setTimeRange(startTime, endTime);
                }
                if (mversion >= 0) {
                    get.setMaxVersions(mversion);
                }
                if (fb != null){
                	Filter f = fb.getFilter(ctx);
                	if (f != null){
                		get.setFilter(f);
                	}
                }
                Result result = hTable.get(get);
                String family;
                String qualifier;
                String value;
                if (result.size() > 0) {
                    String key;
                    List<Object> list;
                    System.err.println("=========result:" + result.size());
                    for (Cell cell : result.listCells()) {
                        family = new String(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength(), CHARSET_NAME);
                        qualifier = new String(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength(), CHARSET_NAME);
                        value = new String(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength(), CHARSET_NAME);
                        if (StringUtils.isNotEmpty(qualifier)) {
                            key = family + ":" + qualifier;
                        } else {
                            key = family;
                        }
                        list = data.get(key);
                        if (list == null) {
                            list = new ArrayList<Object>();
                            data.put(key, list);
                        }
                        list.add(value);
                    }
                }
            }
            current.put(tagValue, data);
        } catch (IOException e) {
            throw new BaseException("core.hbase_get_error", e.getMessage());
        }
    }


}
