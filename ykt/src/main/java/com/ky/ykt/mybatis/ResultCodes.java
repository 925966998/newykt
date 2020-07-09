package com.ky.ykt.mybatis;

/**
 * @author 170024
 */
public enum ResultCodes {

    /**
     * 成功
     */
    SUCCESS(10000, "成功"),

    /**
     * 参数错误
     */
    BAD_REQUEST(40000, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(40001, "未授权"),

    /**
     * 内部错误
     */
    INTERAL_ERROR(50000, "内部错误");

    private int code;
    private String msg;

    ResultCodes(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
