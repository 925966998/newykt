package com.ky.ykt.entity.xml;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName Head
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/
@XmlRootElement
public class Head {
    //接口ID:按章节5中定义执行。
    private String ID;
    //UUID是：长度不超过50字节。每一次接口调用的唯一标识，当异步返回结果时，UUID为调用请求报文中的UUID；
    private String UUID;
    //CallDate取调用接口的当前日期及时间。
    private String CallDate;
    //CallTime：取调用接口的当前日期及时间。
    private String CallTime;
    //CallUser调用人：长度不超过100字节，无统一要求，按各调用接口方自行定义，方便接口出问题后定位调用日志，排查问题。
    private String CallUser;
    //Token身份验证标识：长度不超过50字节。按每个单位访问系统生成一个唯一固定标识。调用补贴系统接口，要求必须传入补贴系统的唯一授权标识；其他系统接口调用根据各方系统要求。
    private String Token;
    //区县标识是District,用于唯一区分接口调用/返回到区县。详见《附件：行政区划清单》
    private String District;
    //银行代码BankNo，用于唯一区分银行。
    private String BankNo;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getCallDate() {
        return CallDate;
    }

    public void setCallDate(String callDate) {
        CallDate = callDate;
    }

    public String getCallTime() {
        return CallTime;
    }

    public void setCallTime(String callTime) {
        CallTime = callTime;
    }

    public String getCallUser() {
        return CallUser;
    }

    public void setCallUser(String callUser) {
        CallUser = callUser;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getBankNo() {
        return BankNo;
    }

    public void setBankNo(String bankNo) {
        BankNo = bankNo;
    }
}
