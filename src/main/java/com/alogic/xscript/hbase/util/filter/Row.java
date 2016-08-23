package com.alogic.xscript.hbase.util.filter;

import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import com.alogic.xscript.hbase.util.FilterBuilder;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

public class Row extends FilterBuilder.Abstract{
	protected Filter filter;
	
	@Override
	public Filter getFilter(Properties p) {
		return filter;
	}

	@Override
	public void configure(Properties p) {		
		String op = PropertiesConstants.getString(p,"operator","EQUAL");
		String comparator = PropertiesConstants.getString(p,"comparator","Binary");
		String value = PropertiesConstants.getString(p,"value","");
		
		filter = new RowFilter(
				getCompareOp(op),
				getComparable(comparator,value)
				);
	}

}
