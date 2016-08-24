h-conf
======

h-conf用于获取一个hbase连接。
h-conf是一个segment，支持子语句，所有子语句将可以通过上下文对象使用该连接，该hbase连接也只在子语句范围内有效。
暂时单一配置，以后会根据pool连接池管理。


### 实现类

com.alogic.xscript.hbase.HConf


### 配置参数

windows支持下列参数：

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | cid | conf的上下文对象id,缺省为$h-conf | 
| 2 | zkQuorum | hbase使用zookeeper的地址,多个逗号间隔,必须 | 
| 3 | zkParent | zookeeper的父目录,必须 | 
| 4 | loginUser | 登录用户,例如：ems/h2m2.ecloud.com,可配在settings.xml中 |
| 5 | keytabPath | keytab公钥文件路径 ,可配在settings.xml中|
| 6 | krb.ini | krb5文件路径 ,可配在settings.xml中|
| 7 | core.site.xml | hbase的核心配置文件路径,可配在settings.xml中 ，可选|
| 8 | hbase.site.xml | hbase的核心配置文件路径,可配在settings.xml中 ，可选| 

linux支持下列参数：

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 1 | cid | conf的上下文对象id,缺省为$h-conf | 
| 2 | zkQuorum | hbase使用zookeeper的地址,多个逗号间隔,必须 | 
| 3 | zkParent | zookeeper的父目录,必须 | 
| 4 | loginUser | 登录用户,例如：ems/h2m2.ecloud.com,可配在settings.xml中 |
| 5 | keytabPath | keytab公钥文件路径 ,可配在settings.xml中|

注意：

1.hbase必要配置文件(core.site.xml、hbase.site.xml等)自行拷贝到自己项目中的resources目录下，会默认加载此文件夹。

2.windows下连接hbase要自行修改hosts文件，对hbase主机ip映射。


### 案例

下面是一个创建h-conf的案例：

```xml
	<script>
	<h-conf zkQuorum="h2a1.ecloud.com,h2m1.ecloud.com,h2m2.ecloud.com" zkParent="/hbase-secure">
		 <!--下面是对该conf的操作-->
		
	</h-conf>
</script>
```
settings.xml下的案例：

```xml
	<settings>
		<parameter id="krb.ini" value="C:\\krb5.ini" final="true"/>
		<parameter id="loginUser" value="ems/h2m2.ecloud.com" final="true"/>
		<parameter id="keytabPath" value="C:\\ems.app.keytab" final="true"/> 
</settings>
```
