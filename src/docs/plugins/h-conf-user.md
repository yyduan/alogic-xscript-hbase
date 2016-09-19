h-conf-user
=======

h-conf-user是初始化h-conf时,采用loginUserFromKeytab登录配置。

这个类属于org.apache.hadoop.hbase.client包。

比如设置user、path参数。


### 实现类

com.alogic.xscript.hbase.HConfUser


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | pid | conf的上下文对象id,缺省为$h-conf |
| 2 | user | 用户登录名(user the principal name to load from the keytab),必须 | 
| 3 | path | keytab路径(path the path to the keytab file),必须 | 

### 案例

下面是一个创建h-conf-user的案例：

```xml
	<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf >
		<h-conf-user path="C:\\ems.app.keytab" user="ems/h2m2.ecloud.com" />
	</h-conf>
</script>
```
