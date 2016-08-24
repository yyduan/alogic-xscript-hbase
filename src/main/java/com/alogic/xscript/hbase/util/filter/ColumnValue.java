package com.alogic.xscript.hbase.util.filter;

import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.alogic.xscript.hbase.util.FilterBuilder;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

/**
 * 按照指定的ColumnValue过滤
 * 
 * @author duanyy
 *
 */
public class ColumnValue extends FilterBuilder.Abstract {
    protected Filter filter;

    @Override
    public Filter getFilter(Properties p) {
        return filter;
    }

    @Override
    public void configure(Properties p) {
        String family = PropertiesConstants.getString(p, "family", "");
        String qualifier = PropertiesConstants.getString(p, "qualifier", "");
        String value = PropertiesConstants.getString(p, "value", "");

        String op = PropertiesConstants.getString(p, "operator", "EQUAL");

        SingleColumnValueFilter f = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), getCompareOp(op), new BinaryComparator(
                Bytes.toBytes(value)));
        f.setFilterIfMissing(true);
        filter = f;
    }

}
