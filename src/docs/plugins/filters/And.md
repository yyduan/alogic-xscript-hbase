And
===

And用于连接多个Filter，必须每个Filter通过才算通过。

And支持子Filter，子Filter可以是当前支持的一个或多个filter。

### 实现类

com.alogic.xscript.hbase.util.filter.And

### 配置参数

无

### 配置样例

例如：选取RowKey大于10000,小于20000的Row

```xml

	<filter module="And">
		<filter module="Row" value="20000" operator="LESS"/>
		<filter module="Row" value="10000" operator="GREATER"/>
	</filter>
	
```
