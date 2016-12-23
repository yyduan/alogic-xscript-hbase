package com.alogic.xscript.hbase.util.filter;

import org.apache.hadoop.hbase.filter.ByteArrayComparable;
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
        String comp = PropertiesConstants.getString(p, "comparable", "");

        // 比较器：
        // BinaryComparator 按字节索引顺序比较指定字节数组，采用Bytes.compareTo(byte[])
        // BinaryPrefixComparator 跟前面相同，只是比较左端的数据是否相同
        // NullComparator 判断给定的是否为空
        // BitComparator 按位比较 a BitwiseOp class 做异或，与，并操作
        // RegexStringComparator 提供一个正则的比较器，仅支持 EQUAL 和非EQUAL
        // SubstringComparator 判断提供的子串是否出现在table的value中。
        ByteArrayComparable comparable = getComparable(comp, value);
        // if ("SUB".equals(comp)) {
        // comparable = new SubstringComparator(value);
        // } else if ("REG".equals(comp)) {
        // comparable = new RegexStringComparator(value);
        // } else if ("PRE".equals(comp)) {
        // comparable = new BinaryPrefixComparator(Bytes.toBytes(value));
        // } else {
        // comparable = new BinaryComparator(Bytes.toBytes(value));
        // }

        SingleColumnValueFilter f = new SingleColumnValueFilter(Bytes.toBytes(family), Bytes.toBytes(qualifier), getCompareOp(op), comparable);
        f.setFilterIfMissing(true);
        filter = f;
    }

}
