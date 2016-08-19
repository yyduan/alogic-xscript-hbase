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
| 1 | callType | 取值为windwos和linux,表示不同系统下连接到hbase，默认为linux,可配在settings.xml中 |
| 2 | krb.ini | krb5文件路径 ,可配在settings.xml中|
| 3 | core.site.xml | hbase的核心配置文件路径,可配在settings.xml中 |
| 4 | hbase.site.xml | hbase的核心配置文件路径,可配在settings.xml中 | 
| 5 | loginUser | 登录用户,例如：ems/h2m2.ecloud.com,可配在settings.xml中 |
| 6 | keytabPath | keytab公钥文件路径 ,可配在settings.xml中|

linux支持下列参数：

| 1 | callType | 取值为windwos和linux,表示不同系统下连接到hbase，默认为linux ,可配在settings.xml中|
| 2 | core.site.xml | hbase的核心配置文件路径,可配在settings.xml中 |
| 3 | hbase.site.xml | hbase的核心配置文件路径,可配在settings.xml中 | 

### 案例

下面是一个创建h-conf的案例：

```xml
	<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf >
		 <!--下面是对该conf的操作-->
		
	</h-conf>
</script>
```
settings.xml下的案例：
```xml
	<settings>
		<parameter id="callType" value="windows" final="true"/>
		<parameter id="krb.ini" value="C:\\krb5.ini" final="true"/>
		<parameter id="core.site.xml" value="C:\\workspace2\\eops-center\\webapp\\eops-center-web\\src\\main\\resources\\conf\\hbase\\test\\core-site.xml" final="true"/>
		<parameter id="hbase.site.xml" value="C:\\workspace2\\eops-center\\webapp\\eops-center-web\\src\\main\\resources\\conf\\hbase\\test\\hbase-site.xml" final="true"/>
		<parameter id="loginUser" value="ems/h2m2.ecloud.com" final="true"/>
		<parameter id="keytabPath" value="C:\\hwg\\ems.app.keytab" final="true"/>
</settings>
```
