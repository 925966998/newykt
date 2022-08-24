package com.ky.ykt.entity.xml;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

/**
 * @ClassName BodyCheckOne
 * @Description: TODO
 * @Author czw
 * @Date 2020/6/22
 **/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"Name","IdNo","BankAcct","Result","BankDep","Amt","ErrorMsg","Extend1","Extend2"})
@XmlRootElement(name = "xml")
public class BodyCheckOne {
    //账户名
    @XmlElement(name="Name")
    private String Name;
    //证件号码
    @XmlElement(name="IdNo")
    private String IdNo;
    //银行账号
    @XmlElement(name="BankAcct")
    private String BankAcct;
    //验证结果
    @XmlElement(name="Result")
    private String Result;
    //开户行
    @XmlElement(name="BankDep")
    private String BankDep;
    //发放金额
    @XmlElement(name="Amt")
    private BigDecimal Amt;
    //错误描述
    @XmlElement(name="ErrorMsg")
    private String ErrorMsg;
    //预留字段1
    @XmlElement(name="Extend1")
    private String Extend1;
    //预留字段2
    @XmlElement(name="Extend2")
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

    public BigDecimal getAmt() {
        return Amt;
    }

    public void setAmt(BigDecimal amt) {
        Amt = amt;
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
