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
public class ServiceCheckOne {
    @XmlElement(name="Head")
    private Head Head;
    @XmlElement(name="Body")
    private BodyCheckOne Body;

    public Head getHead() {
        return Head;
    }

    public void setHead(Head head) {
        this.Head = head;
    }

    public BodyCheckOne getBody() {
        return Body;
    }

    public void setBody(BodyCheckOne Body) {
        this.Body = Body;
    }
}
