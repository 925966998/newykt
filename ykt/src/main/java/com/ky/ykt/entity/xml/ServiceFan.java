package com.ky.ykt.entity.xml;

import javax.xml.bind.annotation.*;

/**
 * @ClassName Service
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"Head","Body"})
@XmlRootElement(name = "Service")
public class ServiceFan {
    @XmlElement(name="Head")
    private Head Head;
    @XmlElement(name="Body")
    private BodyFan Body;

    public Head getHead() {
        return Head;
    }

    public void setHead(Head head) {
        this.Head = head;
    }

    public BodyFan getBody() {
        return Body;
    }

    public void setBody(BodyFan body) {
        this.Body = body;
    }
}
