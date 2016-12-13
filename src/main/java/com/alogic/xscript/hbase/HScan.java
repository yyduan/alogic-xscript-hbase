package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
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

public class HScan extends HTableOperation {
	/**
	 * Filter Builder
	 */
	protected FilterBuilder fb = null;
	/**
	 * 指定查询rowkey的开始点
	 */
	protected String srow = "-1";
	/**
	 * 指定查询rowkey的结束点
	 */
	protected String erow = "-1";

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
	 * 列名(包含列族:列名)
	 */
	protected String col = "";

	public HScan(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	public void configure(Element e, Properties p) {
		Properties props = new XmlElementProperties(e, p);

		Element filter = XmlTools.getFirstElementByPath(e, "filter");
		if (filter != null) {
			FilterBuilder.TheFactory f = new FilterBuilder.TheFactory();
			try {
				fb = f.newInstance(filter, props, "module");
			} catch (Exception ex) {
				log("Can not create instance of FilterBuilder.", "error");
			}
		}
		configure(props);
	}

	@Override
	public void configure(Properties p) {
		super.configure(p);
		srow = p.GetValue("srow", srow, false, true);
		erow = p.GetValue("erow", erow, false, true);

		stime = p.GetValue("stime", stime, false, true);
		etime = p.GetValue("etime", etime, false, true);
		mvers = p.GetValue("version", mvers, false, true);
		col = p.GetValue("col", col, false, true);
	}

	@Override
	protected void onExecute(HTable hTable, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {
		Scan scan = new Scan();
		String startrow = ctx.transform(srow);
		String endrow = ctx.transform(erow);
		Long startTime = Long.parseLong(ctx.transform(stime));
		Long endTime = Long.parseLong(ctx.transform(etime));
		int mversion = Integer.parseInt(ctx.transform(mvers));
		String column = ctx.transform(col);
		byte[][] fcBytes = FColumnUtil.getFamilyAndColumnBytes(column);
		if (fcBytes != null) {
			if (fcBytes[1] != null) {
				scan.addColumn(fcBytes[0], fcBytes[1]);
			} else {
				scan.addFamily(fcBytes[0]);
			}
		}
		try {
			String tagValue = ctx.transform(tag);
			List<Map<String, Object>> rows = new ArrayList<>();
			if (StringUtils.isNotEmpty(tagValue)) {
				if (StringUtils.isNotEmpty(startrow)) {
					scan.setStartRow(Bytes.toBytes(startrow));
				}
				if (StringUtils.isNotEmpty(endrow)) {
					scan.setStopRow(Bytes.toBytes(endrow));
				}
				if (startTime >= 0 && endTime >= 0) {
					scan.setTimeRange(startTime, endTime);
				}
				if (mversion >= 0) {
					scan.setMaxVersions(mversion);
				}
				if (fb != null) {
					Filter f = fb.getFilter(ctx);
					if (f != null) {
						scan.setFilter(f);
					}
				}
				ResultScanner rs = hTable.getScanner(scan);
				String row;
				Map<String, Object> data;
				for (Result r : rs) {
					row = new String(r.getRow());
					data = new HashMap<String, Object>();
					data.put("row", row);
					if (r.size() > 0) {
						String family;
						String qualifier;
						String value;
						for (Cell cell : r.listCells()) {
							family = new String(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength(),
									CHARSET_NAME);
							qualifier = new String(cell.getQualifierArray(), cell.getQualifierOffset(),
									cell.getQualifierLength(), CHARSET_NAME);
							value = new String(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength(),
									CHARSET_NAME);
							if (StringUtils.isNotEmpty(qualifier)) {
								data.put(family + ":" + qualifier, value);
							} else {
								data.put(family, value);
							}
						}
					}
					rows.add(data);
				}
			}
			current.put(tagValue, rows);
		} catch (IOException e) {
			// e.printStackTrace();
			throw new BaseException("core.hbase_scan_error", e.getMessage());
		}
	}

}
