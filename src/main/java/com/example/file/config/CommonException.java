package com.example.file.config;

import lombok.Getter;

public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    @Getter
    private int code;
    public CommonException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public CommonException(String message) {
        super(message);
        this.code = 500;
    }
}