h-conf
======

h-conf用于获取一个hbase连接。
h-conf是一个segment，支持子语句，所有子语句将可以通过上下文对象使用该连接，该hbase连接也只在子语句范围内有效。
暂时单一配置，以后会根据pool连接池管理。


### 实现类

com.alogic.xscript.hbase.HConf


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | cid | conf的上下文对象id,缺省为$h-conf | 
| 2 | krb | krb5文件路径 ,可配在settings.xml中,window下必须配置|


注意：

  可配合[h-conf-set](h-conf-set.md)和[h-conf-user](h-conf-user.md)使用。


### 案例

下面是一个创建h-conf的案例：

```xml
	<script>
	<h-conf>
	<h-conf-set id="hbase.zookeeper.quorum" value="h2a1.ecloud.com,h2m1.ecloud.com,h2m2.ecloud.com" />
		<h-conf-set id="zookeeper.znode.parent" value="/hbase-secure" />
		<h-conf-user path="C:\\ems.app.keytab" user="ems/h2m2.ecloud.com" />
		 <!--下面是对该conf的操作-->
		
	</h-conf>
</script>
```
