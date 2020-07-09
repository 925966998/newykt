package com.ky.ykt.mybatis;

/**
 * Created by 170181 on 2017/1/10.
 */
public class RestResult {
    public final static String SUCCESS_MSG = ResultCodes.SUCCESS.getMsg();
    public final static int SUCCESS_CODE = ResultCodes.SUCCESS.getCode();
    public final static String ERROR_MSG = ResultCodes.INTERAL_ERROR.getMsg();
    public final static int ERROR_CODE = ResultCodes.INTERAL_ERROR.getCode();

    public RestResult() {
        this.code = ResultCodes.SUCCESS.getCode();
        this.msg = ResultCodes.SUCCESS.getMsg();
    }

    private int code = 0;
    private String msg = null;
    private long total = 0;
    private Object rows = null;
    private Object data = null;

    public RestResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public RestResult(long total, Object rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Object getRows() {
        return rows;
    }

    public void setRows(Object rows) {
        this.rows = rows;
    }

    public RestResult(int code, String msg) {
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
