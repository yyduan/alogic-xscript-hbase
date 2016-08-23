package com.alogic.xscript.hbase.util.filter;

import org.apache.hadoop.hbase.filter.FilterList.Operator;

import com.alogic.xscript.hbase.util.FilterBuilder;

public class Or extends FilterBuilder.Multi{

	@Override
	protected Operator getOperator() {
		return Operator.MUST_PASS_ONE;
	}


}
