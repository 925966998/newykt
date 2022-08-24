package com.ky.ykt.entity.xml;

import javax.xml.bind.annotation.*;

/**
 * @ClassName BillCode
 * @Description: TODO
 * @Author czw
 * @Date 2020/6/19
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"BillCode"})
@XmlRootElement(name = "xml")
public class BillCodeData {
    @XmlElement(name="BillCode")
    private String BillCode;

    public String getBillCode() {
        return BillCode;
    }

    public void setBillCode(String billCode) {
        BillCode = billCode;
    }
}
