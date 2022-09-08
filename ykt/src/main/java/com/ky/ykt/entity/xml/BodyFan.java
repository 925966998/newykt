package com.ky.ykt.entity.xml;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName Body
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"result","resultReason","subAmt","subRowCnt","sucAmt","sucRowCnt", "extend1","extend2"})
@XmlRootElement(name = "xml")
public class BodyFan {
    //明细包号
    @XmlElement(name="Result")
    private String result;
    //付款人账号
    @XmlElement(name="ResultReason")
    private String resultReason;
    //付款人名称
    @XmlElement(name="SubAmt")
    private BigDecimal subAmt;
    //付款人开户行
    @XmlElement(name="SubRowCnt")
    private BigDecimal subRowCnt;
    //零余额账号
    @XmlElement(name="SucAmt")
    private BigDecimal sucAmt;
    //零余额名称
    @XmlElement(name="SucRowCnt")
    private BigDecimal sucRowCnt;
    //预留字段3
    @XmlElement(name="Extend1")
    private String extend1;
    @XmlElement(name="Extend2")
    private String extend2;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultReason() {
        return resultReason;
    }

    public void setResultReason(String resultReason) {
        this.resultReason = resultReason;
    }

    public BigDecimal getSubAmt() {
        return subAmt;
    }

    public void setSubAmt(BigDecimal subAmt) {
        this.subAmt = subAmt;
    }

    public BigDecimal getSubRowCnt() {
        return subRowCnt;
    }

    public void setSubRowCnt(BigDecimal subRowCnt) {
        this.subRowCnt = subRowCnt;
    }

    public BigDecimal getSucAmt() {
        return sucAmt;
    }

    public void setSucAmt(BigDecimal sucAmt) {
        this.sucAmt = sucAmt;
    }

    public BigDecimal getSucRowCnt() {
        return sucRowCnt;
    }

    public void setSucRowCnt(BigDecimal sucRowCnt) {
        this.sucRowCnt = sucRowCnt;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
    }
}
