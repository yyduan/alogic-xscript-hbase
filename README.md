alogic-xscript-hbase
=====================

### Overview

alogic-xscript-hbase是基于xscript2.0的hbase插件，提供了使用hbase所需的相关指令，无缝对接hbase数据库。

### Getting started

按照以下步骤，您可轻松在您的项目中使用alogic-xscript-kvalue.

不过开始之前，我们希望您了解xscript的相关知识。

- [xscript2.0](https://github.com/yyduan/alogic/blob/master/alogic-doc/alogic-common/xscript2.md) - 您可以了解xscript的基本原理及基本编程思路
- [xscript2.0基础插件](https://github.com/yyduan/alogic/blob/master/alogic-doc/alogic-common/xscript2-plugins.md) - 如何使用xscript的基础插件
- [基于xscript的together](https://github.com/yyduan/alogic/blob/master/alogic-doc/alogic-common/xscript2-together.md) - 如何将你的script发布为alogic服务


### Example

下面的案例是对hbase中的表aaabbb进去scan查询数据的操作.

```xml
<script>
	<using xmlTag = "h-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf> 
		<h-conf-set id="hbase.zookeeper.quorum" value="h2a1.ecloud.com,h2m1.ecloud.com,h2m2.ecloud.com" />
		<h-conf-set id="zookeeper.znode.parent" value="/hbase-secure" />
		<h-conf-user path="C:\\ems.app.keytab" user="ems/h2m2.ecloud.com" />
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

为了运行上面的指令，你必须要做下列工作：

windwos下操作：

1. 修改hosts文件，对hbase主机ip映射。例如在C:\Windows\System32\Drivers\etc\HOSTS文件添加一下几行：
   ```
   132.122.1.11    h2a1.ecloud.com
   132.122.1.12    h2m1.ecloud.com
   132.122.1.13    h2m2.ecloud.com
   ```
2. 在settings.xml配置[krb5.ini](src/test/resources/conf/krb5.ini)文件路径,例如：
   ```
   <settings>
		 <parameter id="krb.ini" value="C:\\krb5.ini" final="true"/>
	</settings>
   ```
3. 采用loginUserFromKeytab登录配置时，需要你在132.122.1.13主机/etc/security/keytabs下拷贝自己需要的用户keytab文件，demo的[keytab文件](src/test/resources/conf/ems.app.keytab)。

4. hbase必要配置文件([core.site.xml](src/test/resources/conf/core.site.xml)、[hbase.site.xml](src/test/resources/conf/core.site.xml)等)自行拷贝到自己项目中的resources目录下，会默认加载此文件夹。

linux下操作：

1. linux下也可以采用默认conf.create()配置，如果采用loginUserFromKeytab登录配置时，需要你在132.122.1.13主机/etc/security/keytabs下拷贝自己需要的用户keytab文件，demo的[keytab文件](src/test/resources/conf/ems.app.keytab)。

2. hbase必要配置文件([core.site.xml](src/test/resources/conf/core.site.xml)、[hbase.site.xml](src/test/resources/conf/core.site.xml)等)自行拷贝到自己项目中的resources目录下，会默认加载此文件夹。

之后，可以运行[demo](src/test/java/Demo.java)来测试xscript脚本。


### 指令参考

参见[alogic-xscript-hbase参考](src/docs/reference.md)。

### 版本历史
    - 0.0.1 [20160804 duanyy]
		+ 初次发布
		
