package com.ky.ykt.entity.xml;

import javax.xml.bind.annotation.*;

/**
 * @ClassName Service
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Service")
public class ServiceOne {
    @XmlElement(name="Head")
    private Head head;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

}
