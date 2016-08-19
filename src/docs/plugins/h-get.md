h-get
========

h-get用于根据rowkey、过滤器等条件查询单行数据。
必须在h-table语句内使用。


### 实现类

com.alogic.xscript.hbase.HGet


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | table的上下文对象id,缺省为$h-table | 
| 2 | tag | 返回数据集名称,可选 | 
| 3 | row | 行名,也为rowkey,必要 | 
| 4 | col | 列族列名(列族:列名),可选 | 
| 5 | stime | 指定查询开始时间戳,可选 | 
| 6 | etime | 指定查询结束时间戳,可选 | 
| 7 | srow | 指定查询rowkey的开始点,字母先后顺序的,可选 | 
| 8 | erow | 指定查询rowkey的结束点,字母先后顺序的,可选 | 
| 9 | mvers | 列出的版本最大数,可选 | 
| 10 | ftype | 多条件过滤,AND 和 OR 两种关系,默认为OR,可选 | 
| 11 | filter | 过滤器,参数格式(方法名,参数...)例如："ColumnPrefixFilter,hwg",可选 | 

备注：
[过滤器Filer使用方法介绍](h-filter.md)


### 案例

下面是一个创建h-get的案例：

```xml
<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf >
		<h-table tname="bbb">
			<h-get tag="data" row="hwg" col="F" />
			
		</h-table>
	</h-conf>
</script>
```

返回值格式：

{“列族:列名”：“值”,...}

样例：
```json
	{"data":{"F:sex":"man","M:age":"30","M:money":"100"}}
```
