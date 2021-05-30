package com.add.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Email {
    private String email;
    private String title;
    private String content;


    public Email(String email, String title, String content) {
        this.email = email;
        this.title = title;
        this.content = content;
    }
}
