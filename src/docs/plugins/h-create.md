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
| 3 | cf | 列族,多列族逗号间隔,必要 | 
| 4 | cover | 如果已存在表是否被覆盖，默认不覆盖false，覆盖=true | 


### 案例

下面是一个创建h-create的案例：

```xml
	<script>
	<h-conf>
	<h-conf >
		<h-admin >
			<h-create tname="bbb" cf="M,F,team" cover="true"  />
		</h-admin>
	</h-conf>
</script>
```
