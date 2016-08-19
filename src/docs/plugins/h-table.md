h-table
=======

h-table表示HBase表中HBase的内部类。
它用于实现单个HBase表进行通信。
这个类属于org.apache.hadoop.hbase.client类。
可以创建一个对象来访问HBase表，查询、删除列、列值、扫描等操作。




### 实现类

com.alogic.xscript.hbase.HBaseTable


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | conf的上下文对象id,缺省为$h-conf |
| 2 | cid | table的上下文对象id,缺省为$h-table | 
| 3 | tname | 表名，必须 | 


### 案例

下面是一个创建h-table的案例：

```xml
	<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf >
		<h-table tname="bbb">
			<!--执行table下的操作 -->
		</h-table>
	</h-conf>
</script>
```
