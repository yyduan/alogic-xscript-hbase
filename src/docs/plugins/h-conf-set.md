h-conf-set
=======

h-conf-set是初始化h-conf时,设置conf的额外参数。

这个类属于org.apache.hadoop.hbase.client包。

比如设置zookeeper的地址，zookeeper的父目录等参数。


### 实现类

com.alogic.xscript.hbase.HConfSet


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | conf的上下文对象id,缺省为$h-conf |
| 2 | id | 参数名称，必须 | 
| 3 | value | 参数值，必须 | 


### 案例

下面是一个创建h-conf-set的案例：

```xml
	<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf >
		<h-conf-set id="hbase.zookeeper.quorum" value="h2a1.ecloud.com,h2m1.ecloud.com,h2m2.ecloud.com" />
		<h-conf-set id="zookeeper.znode.parent" value="/hbase-secure" />
	</h-conf>
</script>
```
