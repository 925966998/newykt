package com.ky.ykt.entity.xml;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @ClassName XmlPull
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/
@XmlRootElement
public class XmlPull {
    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
