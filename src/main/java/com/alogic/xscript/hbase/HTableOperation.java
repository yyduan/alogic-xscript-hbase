package com.alogic.xscript.hbase;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;

import com.alogic.xscript.AbstractLogiclet;
import com.alogic.xscript.ExecuteWatcher;
import com.alogic.xscript.Logiclet;
import com.alogic.xscript.LogicletContext;
import com.anysoft.util.BaseException;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 基于HTable的操作
 * 
 * @author duanyy
 *
 */
public abstract class HTableOperation extends AbstractLogiclet {
    /**
     * hadmin的cid
     */
    private String pid = "$h-table";

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
    /**
     * 数据集
     */
    protected String tag = "data";
    /**
     * 指定查询开始时间戳
     */
    protected Long stime = null;
    /**
     * 指定查询结束时间戳
     */
    protected Long etime = null;
    /**
     * 指定查询rowkey的开始点
     */
    protected String srow = null;
    /**
     * 指定查询rowkey的结束点
     */
    protected String erow = null;
    /**
     * 列出的版本最大数
     */
    protected Integer mvers = null;
    /**
     * 过滤器参数，格式如下 方法名,参数... 例如："ColumnPrefixFilter,hwg"
     */
    protected String filter = null;
    /**
     * 多条件过滤,AND 和 OR 两种关系
     */
    protected String ftype = "OR";

    /**
     * 获取数据的默认编码
     */
    protected static String CHARSET_NAME = "UTF-8";

    /**
     * 返回结果的id
     */
    protected String id;

    public HTableOperation(String tag, Logiclet p) {
        super(tag, p);
    }

    @Override
    public void configure(Properties p) {
        super.configure(p);
        pid = PropertiesConstants.getString(p, "pid", pid, true);
        row = PropertiesConstants.getString(p, "row", row, true);
        col = PropertiesConstants.getString(p, "col", col, true);
        value = PropertiesConstants.getString(p, "value", value, true);
        tag = PropertiesConstants.getString(p, "tag", tag, true);
        id = PropertiesConstants.getString(p, "id", "$" + getXmlTag(), true);
        stime = PropertiesConstants.getLong(p, "stime", -1, true);
        etime = PropertiesConstants.getLong(p, "etime", -1, true);
        srow = PropertiesConstants.getString(p, "srow", srow, true);
        erow = PropertiesConstants.getString(p, "erow", erow, true);
        mvers = PropertiesConstants.getInt(p, "version", -1, true);
        filter = PropertiesConstants.getString(p, "filter", filter, true);
        ftype = PropertiesConstants.getString(p, "ftype", ftype, true);
    }

    @Override
    protected void onExecute(Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher) {
        HTable hTable = ctx.getObject(pid);
        if (hTable == null) {
            throw new BaseException("core.no_htable", "It must be in a h-table context,check your script.");
        }
        if (StringUtils.isNotEmpty(id)) {
            onExecute(hTable, root, current, ctx, watcher);
        }
    }

    protected abstract void onExecute(HTable hTable, Map<String, Object> root, Map<String, Object> current, LogicletContext ctx, ExecuteWatcher watcher);

    /**
     * 通过参数col(列族：列名)拆解分离2个byte[]
     * 
     * @return byte[0]=列族,byte[1]=列名
     */
    protected byte[][] getFamilyAndColumnBytes() {
        if (StringUtils.isNotEmpty(col)) {
            byte[] family = null;
            byte[] column = null;
            if (col.indexOf(":") > -1) {
                String[] vars = col.split(":");
                family = Bytes.toBytes(vars[0]);
                column = Bytes.toBytes(vars[1]);
            } else {
                family = Bytes.toBytes(col);
            }
            return new byte[][] { family, column };
        }
        return null;
    }

    protected FilterList doFilter() {
        if (StringUtils.isEmpty(filter)) {
            return null;
        }
        String[] array = filter.split(";");
        // 综合使用多个过滤器， AND 和 OR 两种关系
        FilterList filterList;
        if ("OR".equals(ftype)) {
            filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        } else {
            filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        }
        for (String f : array) {
            String[] p = f.split(",");
            // 列值模糊查询
            if (("SingleColumnValueFilter").equals(p[0])) {
                SubstringComparator comp = new SubstringComparator(p[3]);
                SingleColumnValueFilter tfilter = new SingleColumnValueFilter(Bytes.toBytes(p[1]), Bytes.toBytes(p[2]), CompareOp.EQUAL, comp);
                tfilter.setFilterIfMissing(true);
                filterList.addFilter(tfilter);
                // 列前缀匹配
            } else if (("ColumnPrefixFilter").equals(p[0])) {
                Filter cpf = new ColumnPrefixFilter(Bytes.toBytes(p[1]));
                filterList.addFilter(cpf);
                // 行前缀匹配
            } else if (("PrefixFilter").equals(p[0])) {
                Filter cpf = new PrefixFilter(Bytes.toBytes(p[1]));
                filterList.addFilter(cpf);
                // 行模糊，正则匹配等
            } else if (("RowFilter").equals(p[0])) {
                Filter cpf = null;
                ByteArrayComparable rowComparator = null;
                if ("BinaryComparator".equals(p[1])) {
                    rowComparator = new BinaryComparator(Bytes.toBytes(p[2]));
                } else if ("RegexStringComparator".equals(p[1])) {
                    rowComparator = new RegexStringComparator(p[2]);
                } else if ("SubstringComparator".equals(p[1])) {
                    rowComparator = new SubstringComparator(p[2]);
                } else if ("BinaryPrefixComparator".equals(p[1])) {
                    rowComparator = new BinaryPrefixComparator(Bytes.toBytes(p[2]));
                }

                if (rowComparator != null) {
                    cpf = new RowFilter(CompareFilter.CompareOp.EQUAL, rowComparator);
                    filterList.addFilter(cpf);
                }
            }

        }
        return filterList;
    }

}