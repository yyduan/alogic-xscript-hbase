Row
===

Row用于按指定规则选择行。

> 根据hbase的API相关文档，Row主要是对RowKey进行匹配。

### 实现类

com.alogic.xscript.hbase.util.filter.Row

### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | value | 待匹配的值 |
| 2 | operator | 操作符,支持EQUAL,LESS,GREATER,NOT_EQUAL,GREATER_OR_EQUAL,LESS_OR_EQUAL,缺省为EQUAL | 
| 3 | comparator | 比较方法,支持Binary,Substring,Regex,BinaryPrefix，缺省为Binary |

### 配置样例

例如，选择RowKey小于A0000000的记录。

```xml

	<filter module="Row" value="A0000000" operator="LESS"/>
	
```