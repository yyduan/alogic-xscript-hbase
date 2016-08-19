过滤器Filer使用方法介绍
========

 支持同时多个过滤条件。
 参数ftype配置为AND 和 OR 两种关系,默认为OR。
 参数格式：
 (方法名,参数1,参数2;方法名2,参数3,参数4...)
 方法名为api类名，参数为该方法下使用到的参数，具体使用到可以查看hbase api。
 
## 参数组合

### 1、 列名列值模糊过滤，用一列的值决定这一行的数据是否被过滤。
 
       范例:SingleColumnValueFilter,M,name,hwg(类名,列族,列名,查询值)
       

### 2、 列前缀匹配。

       范例:ColumnPrefixFilter,hwg(类名,匹配的行值)
       


### 3、行前缀匹配。

       范例:ColumnPrefixFilter,hwg(类名,匹配的列值)
       

   
### 4、行前缀匹配。

       范例:PrefixFilter,hwg(类名,匹配的列值)
       
### 5、行模糊，正则匹配等。

       范例:RowFilter,BinaryComparator,hwg(过滤器类名,比较器名,匹配的值)   
       
        比较器名有：    
        BinaryComparator(值匹配)
 		RegexStringComparator(正则匹配)
  		SubstringComparator(包含值)
 		BinaryPrefixComparator(前缀匹配)
 		
 ## 源代码
 	
```java
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
 ```		
 
### 案例

下面是一个创建h-get的案例：

```xml
	<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf >
		<h-table tname="bbb">
			<!-- <h-scan tag="data" ftype="AND" filter="PrefixFilter,hw;SingleColumnValueFilter,M,money,0" /> -->
			<h-scan tag="data"   filter="RowFilter,SubstringComparator,hw" />
			<!-- <h-scan tag="data"   filter="SingleColumnValueFilter,M,money,0" /> -->

		</h-table>
	</h-conf>
</script>
```
