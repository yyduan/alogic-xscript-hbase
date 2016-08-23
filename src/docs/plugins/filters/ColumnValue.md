ColumnValue
===========

ColumnValue用于匹配指定列的值。

### 实现类

com.alogic.xscript.hbase.util.filter.ColumnValue

### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | family | hbase列族的family |
| 2 | qualifier | hbase列族的qualifier |
| 3 | value | 待匹配的列值 |
| 4 | operator | 操作符,支持EQUAL,LESS,GREATER,NOT_EQUAL,GREATER_OR_EQUAL,LESS_OR_EQUAL,缺省为EQUAL | 

### 配置样例

例如：选取A:Name中值为alogic的数据

```xml

	<filter module="ColumnValue" family="A" qualifier="Name" value="alogic" operator="EQUAL"/>
	
```

例如：选取A:Name中不为alogic的数据

```xml

	<filter module="ColumnValue" family="A" qualifier="Name" value="alogic" operator="NOT_EQUAL"/>
	
```
