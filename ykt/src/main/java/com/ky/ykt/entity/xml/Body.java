package com.ky.ykt.entity.xml;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @ClassName Body
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/
@XmlRootElement
public class Body {
    //总行数
    private String RowCnt;
    //验证通过行数
    private String SucRowCnt;
    //验证失败行数
    private String FailRowCnt;
    //预留字段3
    private String Extend3;
    //预留字段4
    private String Extend4;
    //预留字段5
    private String Extend5;
    //预留字段6
    private String Extend6;
    //明细数据
    private List<Data> Data;

    public List<com.ky.ykt.entity.xml.Data> getData() {
        return Data;
    }

    public void setData(List<com.ky.ykt.entity.xml.Data> data) {
        Data = data;
    }

    public String getRowCnt() {
        return RowCnt;
    }

    public void setRowCnt(String rowCnt) {
        RowCnt = rowCnt;
    }

    public String getSucRowCnt() {
        return SucRowCnt;
    }

    public void setSucRowCnt(String sucRowCnt) {
        SucRowCnt = sucRowCnt;
    }

    public String getFailRowCnt() {
        return FailRowCnt;
    }

    public void setFailRowCnt(String failRowCnt) {
        FailRowCnt = failRowCnt;
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
}
