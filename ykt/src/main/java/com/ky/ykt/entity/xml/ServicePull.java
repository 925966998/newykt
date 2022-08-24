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
public class ServicePull {
    @XmlElement(name="Head")
    private Head Head;
    @XmlElement(name="Body")
    private BodyPull Body;

    public Head getHead() {
        return Head;
    }

    public void setHead(Head head) {
        this.Head = head;
    }

    public BodyPull getBody() {
        return Body;
    }

    public void setBody(BodyPull body) {
        this.Body = body;
    }
}
