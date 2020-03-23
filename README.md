**简单文件系统**
windows和linux都可用。
windows文件存放于jar包的上一层级file目录,
linux文件存放于jar上一层xxxfile目录,(兼容问题xxx为上级目录名称),
yml文件中可配置最大文件大小。
第一次运行时自动生成对应表。
运行后调用access/add 接口生成AccessKey 通过数据库查看，在发送接口时通过Header传值。
否则会禁止访问。