ColumnPrefix
===========

ColumnPrefix用于选取指定前缀的列。

### 实现类

com.alogic.xscript.hbase.util.filter.ColumnPrefix

### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | prefix | 指定的前缀 |

### 配置样例

例如：选取以A开头的列。

```xml

	<filter module="ColumnPrefix" prefix="A"/>
	
```

