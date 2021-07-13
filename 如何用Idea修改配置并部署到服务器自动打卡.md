# 如何用Idea打包并且部署到服务器自动打卡

## 导入Idea并且修改配置后打包

打开Idea，选择导入

![image-20210706221547440](https://gitee.com/xddadd/cloud-image/raw/master/image-20210706221547440.png)



选择项目的`pom.xml`文件

![image-20210706221721178](https://gitee.com/xddadd/cloud-image/raw/master/image-20210706221721178.png)

open as project

![image-20210706221826924](https://gitee.com/xddadd/cloud-image/raw/master/image-20210706221826924.png)



根据使用步骤配置发送邮件的邮箱、需要打卡的账户

点击刷新按钮，然后等一会，使得自动下载依赖包

![image-20210707092159403](https://gitee.com/xddadd/cloud-image/raw/master/image-20210707092159403.png)

使用Maven进行编译打包

![img](https://gitee.com/xddadd/cloud-image/raw/master/A5E]_QDDZSD]ZTQQ8M4PEF9.png)

找到左边编译的target

![image-20210706222248768](https://gitee.com/xddadd/cloud-image/raw/master/image-20210706222248768.png)

从文件夹打开

![image-20210706222514363](https://gitee.com/xddadd/cloud-image/raw/master/image-20210706222514363.png)

空白处按住`shift`右击鼠标，选择打开powershell窗口。

执行下面命令，表示运行jar包

```shell
java -jar yiban_auto_clock-1.0-SNAPSHOT.jar
```

![image-20210706223021623](https://gitee.com/xddadd/cloud-image/raw/master/image-20210706223021623.png)

code为1，表示打卡成功。

## 如何定时服务器上呢？

基于上面手动用命令行的方式，我们可以设置定时任务来执行该命令。(一定先要有JDK8的环境)

1. windows可以设置定时任务。

2. linux也可以设置定时任务。

**例如Linux：**

1. 将target文件夹传送到服务器上。
2. 在target文件目录下执行下面命令，执行成功则说明当前机器打卡没问题,若失败就先找找问题把，别继续了。

```shell
java -jar yiban_auto_clock-1.0-SNAPSHOT.jar
```

3. 设置定时任务

执行下面指令，表示创建定时任务

```shell
crontab -e
```

然后会弹出一个空文件让你修改，也就是配置定时任务的执行周期和执行的指令

如：

```shell
2 8,12 * * * java -jar /usr/my/target/yiban_auto_clock-1.0-SNAPSHOT.jar
```

上述前面的一部分是设置定时的时间，后面一部分是执行的指令

上面定时的时间表明每天的08：02和12:02执行一次后面的指令。

具体定时规则配置[点击查看](https://www.cnblogs.com/peida/archive/2013/01/08/2850483.html)

4. 保存定时任务的文件即可



