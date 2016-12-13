package com.alogic.xscript.hbase;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.util.Bytes;

import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.alogic.xscript.hbase.util.FColumnUtil;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;

/**
 * 执行Delete操作
 * 
 * @author duanyy
 *
 */
public class HDelete extends HTableOperation {

	/**
	 * 行名rowkey
	 */
	protected String row = "";
	/**
	 * 列名(包含列族:列名)
	 */
	protected String col = "";

	public HDelete(String tag, Logiclet p) {
		super(tag, p);
	}

	@Override
	public void configure(Properties p) {
		super.configure(p);
		// row = PropertiesConstants.getString(p, "row", row, true);
		// col = PropertiesConstants.getString(p, "col", col, true);
		row = p.GetValue("row", row, false, true);
		col = p.GetValue("col", col, false, true);
	}

	@Override
	protected void onExecute(HTable hTable, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx,
			ExecuteWatcher watcher) {

		String rowKey = ctx.transform(row);
		String colomn = ctx.transform(col);

		if (StringUtils.isEmpty(rowKey)) {
			throw new BaseException("core.no_row", "It must be in a h-delete context,check your script.");
		}
		Delete delete = new Delete(Bytes.toBytes(rowKey));
		byte[][] fcBytes = FColumnUtil.getFamilyAndColumnBytes(colomn);
		if (fcBytes != null) {
			if (fcBytes[1] != null) {
				delete.deleteColumn(fcBytes[0], fcBytes[1]);
			} else {
				delete.deleteFamily(fcBytes[0]);
			}
		}
		try {
			hTable.delete(delete);
			log(String.format("delete row [%s] success!", row), "info");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log(String.format("delete row [%s] error,msg:[%s]", row, e.toString()), "error");
			throw new BaseException("core.io_exception", e.getMessage());
		}
	}

}
