Prefix
======

Prefix用于选取指定前缀的Row。

### 实现类

com.alogic.xscript.hbase.util.filter.Prefix

### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | prefix | 指定的前缀 |

### 配置样例

例如：选取以KEY开头的行。

```xml

	<filter module="Prefix" prefix="A"/>
	
```

