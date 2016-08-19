h-admin
=======

h-admin是一个类(HBaseAdmin)表示管理。
这个类属于org.apache.hadoop.hbase.client包。
使用这个类，可以执行管理员任务。
创建表、删除表、列举表等操作。




### 实现类

com.alogic.xscript.hbase.HAdmin


### 配置参数

| 编号 | 代码 | 说明 |
| ---- | ---- | ---- |
| 3 | pid | conf的上下文对象id,缺省为$h-conf |
| 4 | cid | admin的上下文对象id,缺省为$h-admin | 


### 案例

下面是一个创建h-conf的案例：

```xml
	<script>
	<using xmlTag = "kv-conf" module="com.alogic.xscript.hbase.HConf"/>
	<h-conf id="hbaseConf" >
		<h-admin id="HBaseAdmin">
			
		</h-admin>
	</h-conf>
</script>
```
