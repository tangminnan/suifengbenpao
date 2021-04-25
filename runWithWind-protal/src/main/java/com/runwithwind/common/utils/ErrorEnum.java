package com.runwithwind.common.utils;

public enum  ErrorEnum {
    /*
     * 错误信息
     * */
    E_0(200, "操作成功"),
    E_301(301,"警告"),
    E_500(500,"错误"),
    ;

    private Integer Code;
    private String Msg;
    ErrorEnum(Integer Code, String Msg) {
        this.Code = Code;
        this.Msg = Msg;
    }
    public Integer getCode() {
        return Code;
    }
    public String getMsg() {
        return Msg;
    }
}
