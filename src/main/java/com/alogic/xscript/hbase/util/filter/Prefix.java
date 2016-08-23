package com.alogic.xscript.hbase.util.filter;

import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.alogic.xscript.hbase.util.FilterBuilder;
import com.anysoft.util.Properties;
import com.anysoft.util.PropertiesConstants;

public class Prefix extends FilterBuilder.Abstract{
	protected Filter filter;
	
	@Override
	public Filter getFilter(Properties p) {
		return filter;
	}

	@Override
	public void configure(Properties p) {
		String prefix = PropertiesConstants.getString(p,"prefix","");
		filter = new PrefixFilter(
					Bytes.toBytes(prefix)
				);
	}

}
