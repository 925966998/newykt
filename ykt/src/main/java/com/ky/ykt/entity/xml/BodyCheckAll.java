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
@XmlType(propOrder = {"RowCnt","Extend1","Extend2"})
@XmlRootElement(name = "xml")
public class BodyCheckAll {
    //账户名
    @XmlElement(name="RowCnt")
    private Integer RowCnt;
    //预留字段1
    @XmlElement(name="Extend1")
    private String Extend1;
    //预留字段2
    @XmlElement(name="Extend2")
    private String Extend2;

    public Integer getRowCnt() {
        return RowCnt;
    }

    public void setRowCnt(Integer rowCnt) {
        RowCnt = rowCnt;
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
