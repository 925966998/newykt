package com.ky.ykt.entity.xml;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName Data
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/
@XmlRootElement
public class Data {
    //账户名
    private String Name;
    //证件号码
    private String IdNo;
    //银行账号
    private String BankAcct;
    //验证结果
    private String Result;
    //开户行
    private String BankDep;
    //错误描述
    private String ErrorMsg;
    //预留字段1
    private String Extend1;
    //预留字段2
    private String Extend2;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getIdNo() {
        return IdNo;
    }

    public void setIdNo(String idNo) {
        IdNo = idNo;
    }

    public String getBankAcct() {
        return BankAcct;
    }

    public void setBankAcct(String bankAcct) {
        BankAcct = bankAcct;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getBankDep() {
        return BankDep;
    }

    public void setBankDep(String bankDep) {
        BankDep = bankDep;
    }

    public String getErrorMsg() {
        return ErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        ErrorMsg = errorMsg;
    }

    public String getExtend1() {
        return Extend1;
    }

    public void setExtend1(String extend1) {
        Extend1 = extend1;
    }

    public String getExtend2() {
        return Extend2;
    }

    public void setExtend2(String extend2) {
        Extend2 = extend2;
    }
}
