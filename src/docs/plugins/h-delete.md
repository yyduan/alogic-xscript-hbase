h-delete
========

h-delete用于根据rowkey、列等条件删除数据。
必须在h-table语句内使用。


### 实现类

com.alogic.xscript.hbase.HDelete


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | table的上下文对象id,缺省为$h-table | 
| 3 | row | 行名,也为rowkey,必要 | 
| 4 | col | 列族列名(列族:列名),可选 |


### 案例

下面是一个创建h-delete的案例：

```xml
<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf zkQuorum="h2a1.ecloud.com,h2m1.ecloud.com,h2m2.ecloud.com" zkParent="/hbase-secure">
		<h-table tname="bbb">
			<h-delete tag="data" row="hwg" col="F" />
			
		</h-table>
	</h-conf>
</script>
```
