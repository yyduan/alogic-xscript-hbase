package com.alogic.xscript.hbase.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.anysoft.util.Configurable;
import com.anysoft.util.Factory;
import com.anysoft.util.Properties;
import com.anysoft.util.XMLConfigurable;
import com.anysoft.util.XmlElementProperties;
import com.anysoft.util.XmlTools;

/**
 * Filter Builder
 * 
 * @author duanyy
 *
 */
public interface FilterBuilder extends XMLConfigurable,Configurable{
	
	/**
	 * 获取Filter实例
	 * 
	 * @return Filter实例
	 */
	public Filter getFilter(Properties p);
	
	/**
	 * 虚基类
	 * 
	 * @author duanyy
	 *
	 */
	public abstract static class Abstract implements FilterBuilder{
		/**
		 * a logger of log4j
		 */
		protected static final Logger LOG = LogManager.getLogger(FilterBuilder.class);
		
		@Override
		public void configure(Element e, Properties p) {
			Properties props = new XmlElementProperties(e,p);
			configure(e,props);
		}
		
		protected CompareOp getCompareOp(String name){
			if (name.equalsIgnoreCase("EQUAL")){
				return CompareOp.EQUAL;
			}
			
			if (name.equalsIgnoreCase("LESS")){
				return CompareOp.LESS;
			}
			
			if (name.equalsIgnoreCase("GREATER")){
				return CompareOp.GREATER;
			}			
			
			if (name.equalsIgnoreCase("GREATER_OR_EQUAL")){
				return CompareOp.GREATER_OR_EQUAL;
			}
			
			if (name.equalsIgnoreCase("LESS_OR_EQUAL")){
				return CompareOp.LESS_OR_EQUAL;
			}	
			
			if (name.equalsIgnoreCase("NOT_EQUAL")){
				return CompareOp.NOT_EQUAL;
			}			
			
			return CompareOp.EQUAL;
		}
		
		protected ByteArrayComparable getComparable(String name,String value){
			if (name.equals("Binary")){
				return new BinaryComparator(Bytes.toBytes(value));
			}
			
			if (name.equals("Regex")){
				return new RegexStringComparator(value);
			}
			
			if (name.equals("Substring")){
				return new SubstringComparator(value);
			}
			
			if (name.equals("BinaryPrefix")){
				return new BinaryPrefixComparator(Bytes.toBytes(value));
			}
			
			return new BinaryComparator(Bytes.toBytes(value));
		}
	}
	
	/**
	 * Filter列表实现
	 * 
	 * @author duanyy
	 *
	 */
	public abstract static class Multi extends Abstract{
		/**
		 * 子FilterBuidler
		 */
		protected List<FilterBuilder> children = new ArrayList<FilterBuilder>();
		
		protected abstract FilterList.Operator getOperator();
		
		@Override
		public Filter getFilter(Properties p) {
			FilterList filterList = new FilterList(getOperator());
			
			for (FilterBuilder fb:children){
				Filter filter = fb.getFilter(p);
				if (filter != null){
					filterList.addFilter(filter);
				}
			}
			
			return filterList;
		}

		@Override
		public void configure(Properties p) {

		}
		
		@Override
		public void configure(Element e, Properties p) {
			Properties props = new XmlElementProperties(e,p);
			
			NodeList nodeList = XmlTools.getNodeListByPath(e, "filter");
			
			TheFactory factory = new TheFactory();
			
			for (int i = 0 ;i < nodeList.getLength() ; i ++){
				Node n = nodeList.item(i);
				if (n.getNodeType() != Node.ELEMENT_NODE){
					continue;
				}
				
				Element elem = (Element)n;
				
				try {
					FilterBuilder fb = factory.newInstance(elem, p, "module");
					if (fb != null){
						children.add(fb);
					}
				}catch (Exception ex){
					LOG.error("Can not create instance of FilterBuilder,xml:" + XmlTools.node2String(elem));
				}
			}
			
			configure(e,props);
		}		
	}
	
	/**
	 * 工厂类
	 * 
	 * @author duanyy
	 *
	 */
	public static class TheFactory extends Factory<FilterBuilder>{
		public String getClassName(String module){
			if (module.indexOf(".") < 0){
				return "com.alogic.xscript.hbase.util.filter." + module;
			}
			return module;
		}		
	}
}
