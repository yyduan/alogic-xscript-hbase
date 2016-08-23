Filter
======

Filter主要用于对Hbase的查询结果进行过滤，主要应用于Put,Scan等操作中。

当前支持的Filter包括：

- [And](And.md) --以And条件连接多个子Filter 
- [Or](Or.md) --以Or条件连接多个子Filter
- [ColumnValue](ColumnValue.md) -- 选取匹配指定列的值的行
- [ColumnPrefix](ColumnPrefix.md) -- 选取指定前缀的列
- [Prefix](Prefix.md) -- 选择指定前缀的行
- [Row](Row.md) -- 选取匹配条件的行

