h-list
========

h-list列出所有表名。
必须在h-admin语句内使用。


### 实现类

com.alogic.xscript.hbase.HList


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | admin的上下文对象id,缺省为$h-admin | 



### 案例

下面是一个创建h-list的案例：

```xml
	<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf id="hbaseConf" >
		<h-admin id="HBaseAdmin">
			<h-list tag="data"  />
		</h-admin>
	</h-conf>
</script>
```
