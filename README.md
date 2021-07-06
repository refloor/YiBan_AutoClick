# SUST易班自动打卡

本项目是基于[SUST易班打卡](https://github.com/N0I0C0K/SUST_autocheck_public)进行重构的。本项目是使用Java语言。

# 项目功能

1. 易班晨午检自动打卡。
2. 自定义配置用户（无需账号密码），支持多个用户打卡
3. 配置问题/打卡失败/打卡成功都会发送邮箱(前提是邮箱不能填错)
4. 记录日志到click.log，方便排查问题
5. 支持用Idea打成jar包后命令行直接运行(可以部署在服务器上定时任务)

# 如何导入到Idea


# 使用步骤

1. 配置src\main\resources\config\mail.setting。配置发送邮件的邮箱。
2. 在main\resources\data.json配置用户，支持多个用户
3. 执行src\main\java\com\add\Main.java的main方法进行打卡。

# 配置文件data.json说明

如：

```json
{
    "users": [
        {
            "url_generate_date": "2021-05-22", //url产生的日期,一个url有一定的有效期
            "email": "123456789@qq.com", //接收邮箱地址
            "name": "tom", //name，发送邮件需要，跟打卡无关
            "id": "1", //id，日志/邮件需要，跟打卡无关
            "url": "http://yiban.sust.edu.cn/v4/public/index.php?key=Em7/z2oL422315QKqT8pGGgcnsyhgyNhBOsIQZWwPoKB9MOSBCyqRxsaphLn8Yr7LY2KdnXnONwu6K7TTcBF_f8bdGhiE=", //url，从易班复制
            "location": "陕西省+西安市+未央区+111县道+111县+靠近北城驾校+&", //打卡地址，易班后台是根据地址进行区分地区，所以地区中间需要'+',末尾需要'&'，删掉+和&也行
            "morning_click": true, //是否需要晨检
            "noon_click": true, //是否需要午检
            "send_email":true //是否发送邮件
        }
    ]
}
```

## URL从哪里复制？

1. 打开易班->信息上报
2. ![image-20210530142045825](https://gitee.com/xddadd/cloud-image/raw/master/image-20210530142045825.png)

## 如何配置多个用户?

修改data.json文件即可。

users是一个数组，只需要模仿着，copy一份即可。如下是两个用户的。

```json
{
    "users": [
        {
            "url_generate_date": "2021-05-22",
            "email": "123456789@qq.com",
            "name": "tom",
            "id": "1",
            "url": "http://yiban.sust.edu.cn/v4/public/index.php?key=Em7/z2oL422315QKqT8pGGgcnsyhgyNhBOsIQZWwPoKB9MOSBCyqRxsaphLn8Yr7LY2KdnXnONwu6K7TTcBF_f8bdGhiE=",
            "location": "陕西省+西安市+未央区+111县道+111县+靠近北城驾校+&",
            "morning_click": true,
            "noon_click": true,
            "send_email":true
        },
        {
            "url_generate_date": "2021-05-22",
            "email": "123456789@qq.com",
            "name": "jerry",
            "id": "1",
            "url": "http://yiban.sust.edu.cn/v4/public/index.php?key=Em7/z2oL422315QKqT8pGGgcnsyhgyNhBOsIQZWwPoKB9MOSBCyqRxsaphLn8Yr7LY2KdnXnONwu6K7TTcBF_f8bdGhiE=",
            "location": "陕西省+西安市+未央区+111县道+111县+靠近北城驾校+&",
            "morning_click": true,
            "noon_click": true,
            "send_email":true
        }
    ]
}
```

