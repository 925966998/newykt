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
@XmlType(propOrder = {"SubPackNo","PayeeAcctNo","PayeeAcctName","PayeeAcctDep","ZBANo","ZBAName",
        "ZBADep","ProjName","AmtSum","RowCnt","BillCodeData","Extend1","Extend2","Extend3","Extend4","Extend5","Extend6","Data"})
@XmlRootElement(name = "xml")
public class BodyPull {
    //明细包号
    @XmlElement(name="SubPackNo")
    private String SubPackNo;
    //付款人账号
    @XmlElement(name="PayeeAcctNo")
    private String PayeeAcctNo;
    //付款人名称
    @XmlElement(name="PayeeAcctName")
    private String PayeeAcctName;
    //付款人开户行
    @XmlElement(name="PayeeAcctDep")
    private String PayeeAcctDep;
    //零余额账号
    @XmlElement(name="ZBANo")
    private String ZBANo;
    //零余额名称
    @XmlElement(name="ZBAName")
    private String ZBAName;
    //零余额开户行
    @XmlElement(name="ZBADep")
    private String ZBADep;
    //项目名称
    @XmlElement(name="ProjName")
    private String ProjName;
    //总金额
    @XmlElement(name="AmtSum")
    private BigDecimal AmtSum;
    //总笔数
    @XmlElement(name="RowCnt")
    private long RowCnt;
    //支付凭证号
    @XmlElement(name="BillCodeData")
    private List<BillCodeData> BillCodeData;
    //预留字段3
    @XmlElement(name="Extend1")
    private String Extend1;
    @XmlElement(name="Extend2")
    private String Extend2;
    @XmlElement(name="Extend3")
    private String Extend3;
    //预留字段4
    @XmlElement(name="Extend4")
    private String Extend4;
    //预留字段5
    @XmlElement(name="Extend5")
    private String Extend5;
    //预留字段6
    @XmlElement(name="Extend6")
    private String Extend6;
    //明细数据
    @XmlElement(name="Data")
    private List<Data> Data;

    public String getSubPackNo() {
        return SubPackNo;
    }

    public void setSubPackNo(String subPackNo) {
        SubPackNo = subPackNo;
    }

    public String getPayeeAcctNo() {
        return PayeeAcctNo;
    }

    public void setPayeeAcctNo(String payeeAcctNo) {
        PayeeAcctNo = payeeAcctNo;
    }

    public String getPayeeAcctName() {
        return PayeeAcctName;
    }

    public void setPayeeAcctName(String payeeAcctName) {
        PayeeAcctName = payeeAcctName;
    }

    public String getPayeeAcctDep() {
        return PayeeAcctDep;
    }

    public void setPayeeAcctDep(String payeeAcctDep) {
        PayeeAcctDep = payeeAcctDep;
    }

    public String getZBANo() {
        return ZBANo;
    }

    public void setZBANo(String ZBANo) {
        this.ZBANo = ZBANo;
    }

    public String getZBAName() {
        return ZBAName;
    }

    public void setZBAName(String ZBAName) {
        this.ZBAName = ZBAName;
    }

    public String getZBADep() {
        return ZBADep;
    }

    public void setZBADep(String ZBADep) {
        this.ZBADep = ZBADep;
    }

    public String getProjName() {
        return ProjName;
    }

    public void setProjName(String projName) {
        ProjName = projName;
    }

    public BigDecimal getAmtSum() {
        return AmtSum;
    }

    public void setAmtSum(BigDecimal amtSum) {
        AmtSum = amtSum;
    }

    public long getRowCnt() {
        return RowCnt;
    }

    public void setRowCnt(long rowCnt) {
        RowCnt = rowCnt;
    }

//    public long getPageIndex() {
//        return PageIndex;
//    }
//
//    public void setPageIndex(long pageIndex) {
//        PageIndex = pageIndex;
//    }
//
//    public long getPageSize() {
//        return PageSize;
//    }
//
//    public void setPageSize(long pageSize) {
//        PageSize = pageSize;
//    }
//
//    public long getPageRowCnt() {
//        return PageRowCnt;
//    }
//
//    public void setPageRowCnt(long pageRowCnt) {
//        PageRowCnt = pageRowCnt;
//    }

    public List<com.ky.ykt.entity.xml.BillCodeData> getBillCodeData() {
        return BillCodeData;
    }

    public void setBillCodeData(List<com.ky.ykt.entity.xml.BillCodeData> billCodeData) {
        BillCodeData = billCodeData;
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

    public String getExtend3() {
        return Extend3;
    }

    public void setExtend3(String extend3) {
        Extend3 = extend3;
    }

    public String getExtend4() {
        return Extend4;
    }

    public void setExtend4(String extend4) {
        Extend4 = extend4;
    }

    public String getExtend5() {
        return Extend5;
    }

    public void setExtend5(String extend5) {
        Extend5 = extend5;
    }

    public String getExtend6() {
        return Extend6;
    }

    public void setExtend6(String extend6) {
        Extend6 = extend6;
    }

    public List<com.ky.ykt.entity.xml.Data> getData() {
        return Data;
    }

    public void setData(List<com.ky.ykt.entity.xml.Data> data) {
        Data = data;
    }
}
