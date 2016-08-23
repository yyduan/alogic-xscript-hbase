Or
==

Or用于连接多个Filter，只需通过一个子Filter就算通过

Or支持子Filter，子Filter可以是当前支持的一个或多个filter。

### 实现类

com.alogic.xscript.hbase.util.filter.Or

### 配置参数

无

### 配置样例

例如：选取RowKey小于10000或大于20000的Row

```xml

	<filter module="Or">
		<filter module="Row" value="20000" operator="GREATER"/>
		<filter module="Row" value="10000" operator="LESS"/>
	</filter>
	
```
