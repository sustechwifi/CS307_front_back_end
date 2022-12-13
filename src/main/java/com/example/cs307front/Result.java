package com.example.cs307front;

public class Result<T> {
    private T data;
    private String message;
    private Integer code;

    public Result(T data, String meg, Integer code) {
        this.data = data;
        this.message = meg;
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static <T> Result<T> ok(T data){
        return new Result<T>(data,"success",0);
    }

    public static <T> Result<T> ok(T data,String message){
        return new Result<>(data, message, 0);
    }

    public static  Result<String> error(String message){
        return new Result<String>(message,message,-1);
    }
}
