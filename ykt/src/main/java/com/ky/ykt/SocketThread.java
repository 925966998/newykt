package com.ky.ykt;

import com.ky.ykt.controller.SocketServerD;
import com.ky.ykt.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;


/**
 * @author yaoweijie
 */
@Controller
public class SocketThread extends Thread implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SocketThread.class);

    /*SocketServerD socketServerD = (SocketServerD) SpringUtil.getBean("socketServerD");*/
    @Autowired
    SocketServerD socketServerD;

    @Override
    public void run() {
        logger.info("当前线程名：{}", Thread.currentThread().getName());
        logger.info("由当前线程开始启动Socket服务...");
        try {
            socketServerD.startSocketServer(7011);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }
}