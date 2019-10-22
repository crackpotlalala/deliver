# 代码库模板说明
通过iCode创建代码库后建议进行如下操作：
1. POM文件中修改：
    1. 实际的`groupId`和`artifactId`
    2. 实际的工程描述`description`
    3. 实际的工程版本`version`
    4. 实际的工程依赖，`dependencyManagement`和`dependencies`部分
    5. 实际的编译过程，`build`和`profiles`部分
2. 包名修改：
    1. Java包名不能有"`-`"等特殊字符，建议结合实际情况修改
    2. `src/main/java`和`src/test/java`中都需要修改
3. 编译命令修改：
    1. 修改`ci.yml`中`command`内容
    2. 如需使用特殊的env，请在`command`中加入export命令

**以下为README模板，请参照填写！！！**
# deliver
简要说明

## 快速开始
如何构建、安装、运行

## 测试
如何执行自动化测试

## 如何贡献
贡献patch流程、质量要求

## 讨论
百度Hi讨论群：XXXX
