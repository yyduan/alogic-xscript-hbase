h-put
========

h-put用于插入表数据。
必须在h-table语句内使用。


### 实现类

com.alogic.xscript.hbase.HPut


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | table的上下文对象id,缺省为$h-table | 
| 2 | row | 行名,也为rowkey,必要 | 
| 3 | col | 列族列名(列族:列名),必要 | 
| 4 | value | 指定查询开始时间戳,必要 | 



### 案例

下面是一个创建h-put的案例：

```xml
<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf> 
		<h-conf-set id="hbase.zookeeper.quorum" value="h2a1.ecloud.com,h2m1.ecloud.com,h2m2.ecloud.com" />
		<h-conf-set id="zookeeper.znode.parent" value="/hbase-secure" />
		<h-conf-user path="C:\\ems.app.keytab" user="ems/h2m2.ecloud.com" />
		<h-table tname="bbb">
			<h-get row="hwg" col="team" value="team1" />
			<h-put row="hwg" col="M:age" value="30" />
			<h-put row="hwg" col="M:money" value="100" />
			<h-put row="hwg" col="F:sex" value="man" />
			<h-put row="hwk" col="F:sex" value="girl" />
			<h-put row="wzf" col="M:money" value="50" />
		</h-table>
	</h-conf>
</script>
```


