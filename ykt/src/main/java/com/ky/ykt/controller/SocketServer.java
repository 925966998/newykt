package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.ky.ykt.entity.xml.*;
import com.ky.ykt.utils.Ass;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Ass
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/


/*
 * socket服务类
 */
public class SocketServer {

    public static void SoketPull(String host, int port, String message) throws Exception {

        // 要连接的服务端IP地址和端口
        host = "127.0.0.1";
        port = 8088;
        // 与服务端建立连接
        Socket socket = new Socket(host, port);
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();

        message = getData();
        socket.getOutputStream().write(message.getBytes("UTF-8"));
        //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            sb.append(new String(bytes, 0, len, "UTF-8"));
        }
        System.out.println("get message from server: " + sb);

        inputStream.close();
        outputStream.close();
        socket.close();
    }

    public static String getData() {
        Head head = new Head();
        head.setBankNo("111");
        head.setCallDate("111");
        head.setCallTime("111");
        head.setCallUser("111");
        head.setDistrict("111");
        head.setID("111");
        head.setToken("111");
        head.setUUID("111");
        Body body = new Body();
        body.setRowCnt("222");
        body.setFailRowCnt("222");
        body.setSucRowCnt("222");
        body.setExtend3("222");
        body.setExtend4("222");
        body.setExtend5("222");
        body.setExtend6("222");
        Data data = new Data();
        data.setBankAcct("333");
        data.setBankDep("333");
        data.setErrorMsg("333");
        data.setExtend1("333");
        data.setExtend2("333");
        data.setIdNo("333");
        data.setName("333");
        data.setResult("333");
        Data data1 = new Data();
        data1.setBankAcct("444");
        data1.setBankDep("444");
        data1.setErrorMsg("444");
        data1.setExtend1("444");
        data1.setExtend2("444");
        data1.setIdNo("444");
        data1.setName("444");
        data1.setResult("444");
        List<Data> Data = new ArrayList<>();
        Data.add(data);
        Data.add(data1);
        Service service = new Service();
        body.setData(Data);
        service.setBody(body);
        service.setHead(head);
        XmlPull xmlPull = new XmlPull();
        xmlPull.setService(service);

        String s1 = Ass.jsonToXml(JSON.toJSONString(xmlPull));
        System.out.println(s1);
        return s1;
    }

    public static void main(String[] args) throws Exception {
        SoketPull("",1,"");

    }
}



