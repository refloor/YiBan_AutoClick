package com.add.domain;

import lombok.Getter;

@Getter
public class ExceptionStatus {

    //不需要发邮件
    public static int NOT_CONSUME = 0;//无需消费

    //需要发邮件
    public static int CLICK_PART_FAIL = -2;//部分失败
    public static int CLICK_ALL_FAIL = -3;//全部失败
    public static int CONNECT_FAIL = -1;//连接失败
    public static int UNKNOWN_ERROR = -999;



}
