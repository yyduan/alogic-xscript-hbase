alogic-xscript-hbase
=====================

alogic-xscript-hbase是基于xscript2.0的hbase插件，提供了使用hbase所需的相关指令，无缝对接hbase数据库。

### 案例

```xml
<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf zkQuorum="h2a1.ecloud.com,h2m1.ecloud.com,h2m2.ecloud.com" zkParent="/hbase-secure">
		<h-table tname="aaabbb">
			 <h-scan tag="data"  >
			 <filter module="And">
		        <filter module="ColumnValue" family="M" qualifier="money" value="50" operator="EQUAL"/>
		    </filter>
			 </h-scan> 
		</h-table>
	</h-conf>
</script>
```

### 如何开始？

windwos下操作：

1. 修改hosts文件，对hbase主机ip映射。
2. 在settings.xml配置krb5.ini文件路径。
3. 采用loginUserFromKeytab登录，需要user和keytab,配置在settings.xml
4. hbase必要配置文件(core.site.xml、hbase.site.xml等)自行拷贝到自己项目中的resources目录下，会默认加载此文件夹。

linux下操作：

1. 采用loginUserFromKeytab登录，需要user和keytab,配置在settings.xml
2. hbase必要配置文件(core.site.xml、hbase.site.xml等)自行拷贝到自己项目中的resources目录下，会默认加载此文件夹。

之后，可以运行[demo](src/test/java/Demo.java)来测试xscript脚本。

settings.xml下的案例：

```xml
	<settings>
		<parameter id="krb.ini" value="C:\\krb5.ini" final="true"/>
		<parameter id="loginUser" value="ems/h2m2.ecloud.com" final="true"/>
		<parameter id="keytabPath" value="C:\\ems.app.keytab" final="true"/> 
</settings>
```

### 指令参考

参见[alogic-xscript-hbase参考](src/docs/reference.md)。

### 版本历史
    - 0.0.1 [20160804 duanyy]
		+ 初次发布
