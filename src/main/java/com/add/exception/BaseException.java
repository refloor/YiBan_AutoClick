package com.add.exception;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseException extends RuntimeException {

    //自定义错误码
    private Integer code;

    public BaseException(Integer code, String message) {
        super(message);
        this.code = code;
    }


    public BaseException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

}
