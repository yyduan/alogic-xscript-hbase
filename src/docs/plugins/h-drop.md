h-drop
========

h-drop用于执行删除表，分2部先禁止表然后再删除操作。
必须在h-admin语句内使用。


### 实现类

com.alogic.xscript.hbase.HDrop


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | admin的上下文对象id,缺省为$h-admin | 
| 2 | tname | 表名,必要 | 


### 案例

下面是一个创建h-drop的案例：

```xml
	<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf id="hbaseConf" >
		<h-admin id="HBaseAdmin">
			<h-drop tname="bbb" />
		</h-admin>
	</h-conf>
</script>
```
