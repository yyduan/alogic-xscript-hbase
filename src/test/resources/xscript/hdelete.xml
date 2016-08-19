h-create
========

h-create用于执行创建表。
必须在h-admin语句内使用。


### 实现类

com.alogic.xscript.hbase.HCreate


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | admin的上下文对象id,缺省为$h-admin | 
| 2 | tname | 表名,必要 | 
| 2 | cfy | 列族,多列族逗号间隔,必要 | 


### 案例

下面是一个创建h-create的案例：

```xml
	<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf id="hbaseConf" >
		<h-admin id="HBaseAdmin">
			<h-create tname="bbb" cfy="M,F,team"  />
		</h-admin>
	</h-conf>
</script>
```
