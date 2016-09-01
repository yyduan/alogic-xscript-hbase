h-scan
========

h-scan用于扫描多行查询。
必须在h-table语句内使用。


### 实现类

com.alogic.xscript.hbase.HGet


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | table的上下文对象id,缺省为$h-table | 
| 2 | tag | 返回数据集名称,可选 | 
| 4 | col | 列族列名(列族:列名),可选 | 
| 5 | stime | 指定查询开始时间戳,可选 | 
| 6 | etime | 指定查询结束时间戳,可选 | 
| 7 | srow | 指定查询rowkey的开始点,字母先后顺序的,可选 | 
| 8 | erow | 指定查询rowkey的结束点,字母先后顺序的,可选 | 
| 9 | mvers | 列出的版本最大数,可选 | 

备注：
[过滤器Filer使用方法介绍](filters/Filter.md)


### 案例

下面是一个创建h-scan的案例：

```xml
<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf> 
		<h-conf-set id="hbase.zookeeper.quorum" value="h2a1.ecloud.com,h2m1.ecloud.com,h2m2.ecloud.com" />
		<h-conf-set id="zookeeper.znode.parent" value="/hbase-secure" />
		<h-conf-user path="C:\\ems.app.keytab" user="ems/h2m2.ecloud.com" />
		<h-table tname="aaabbb">
			<!-- <h-scan tag="data"  col="M" srow="hwg" erow="hwi" /> -->
			 <h-scan tag="data"  >
			 <filter module="And">
		       <!--  <filter module="Prefix"  prefix="hw" /> -->
		        <!-- <filter module="Row" comparator="Substring" value="hw" operator="EQUAL"/> -->
		        <!-- <filter module="ColumnPrefix" prefix="M" /> -->
		        <filter module="ColumnValue" family="M" qualifier="money" value="50" operator="EQUAL"/>
		    </filter>
			 </h-scan> 
		</h-table>
	</h-conf>
</script>
```

返回值格式：

{{"row":"rowkey值","列族:列名"："值",...},...}

样例：
```json
	{"data":[{"row":"wzf","M:money":"50"}]}
```
