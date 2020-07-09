package com.ky.ykt.controller;

import com.ky.ykt.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName BankController
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/
@RestController
@RequestMapping("/ky-ykt/bank")
public class BankController {

    private static final Logger logger = LoggerFactory.getLogger(BankController.class);

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/checkAllInfo", method = RequestMethod.GET)
    public void queryByParams(HttpServletRequest request) {
        try {
            SocketServer.SoketPull("", 1, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
