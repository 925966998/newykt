package com.ky.ykt.controller;

/**
 * @ClassName Ass
 * @Description: TODO
 * @Author czw
 * @Date 2020/5/25
 **/

import com.ky.ykt.entity.PersonEntity;
import com.ky.ykt.entity.ProjectDetailEntity;
import com.ky.ykt.entity.xml.Head;
import com.ky.ykt.entity.xml.ServiceFan;
import com.ky.ykt.entity.xml.ServicePull;
import com.ky.ykt.mapper.PersonMapper;
import com.ky.ykt.mapper.ProjectDetailMapper;
import com.ky.ykt.service.PersonService;
import com.ky.ykt.utils.DateUtil;
import com.ky.ykt.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ky.ykt.utils.P_Sm4Util.decryptEcb;
import static com.ky.ykt.utils.P_Sm4Util.encryptEcb;
import static com.ky.ykt.utils.xmlUtilToBean.convertToXmlService;
import static com.ky.ykt.utils.xmlUtilToBean.xmlToBean;

/**
 * nio socket服务端
 */
public class SocketServerD {

    private static final Logger logger = LoggerFactory.getLogger(SocketServerD.class);

    //补贴明细接口
    private static String infoId = "YH005";
    //单笔银行账号验证接口
    private static String checkId = "YH008";
    //批量银行账号验证接口
    private static String checkAllId = "YH010";
    //银行账号验证结果接口
    private static String notifycheckId = "BT009";
    //上卡结果反馈接口
    private static String notifypullId = "BT006";
    //调用人
    private static String CallUser = "350355000001";
    //银行代码
    private static String BankNo = "402161002352";
    //
    private static String BankDep = "601103";
    //区县标识
    private static String District = "140725";

    //解码buffer
    private Charset cs = Charset.forName("UTF-8");
    //接受数据缓冲区
    private static ByteBuffer sBuffer = ByteBuffer.allocate(1024);
    //发送数据缓冲区
    private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    //选择器（叫监听器更准确些吧应该）
    private static Selector selector;

    //生成文件路径
    private static String path = "D:\\file\\";

    private static String hexKey = "4A65463855397748464F4D6673325938";
/*

    @Autowired
    ProjectDetailMapper projectDetailMapper;

    @Autowired
    PersonMapper personMapper;

    @Autowired
    PersonService personService;
*/

    ProjectDetailMapper projectDetailMapper = (ProjectDetailMapper) SpringUtil.getBean("projectDetailMapper");
    PersonMapper personMapper = (PersonMapper) SpringUtil.getBean("personMapper");
    PersonService personService = (PersonService) SpringUtil.getBean("personService");

    /**
     * 启动socket服务，开启监听
     *
     * @param port
     * @throws IOException
     */
    public void startSocketServer(int port) {
        try {
            //打开通信信道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //获取套接字
            ServerSocket serverSocket = serverSocketChannel.socket();
            //绑定端口号
            serverSocket.bind(new InetSocketAddress(port));
            //打开监听器
            selector = Selector.open();
            //将通信信道注册到监听器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //监听器会一直监听，如果客户端有请求就会进入相应的事件处理
            while (true) {
                selector.select();//select方法会一直阻塞直到有相关事件发生或超时
                Set<SelectionKey> selectionKeys = selector.selectedKeys();//监听到的事件
                for (SelectionKey key : selectionKeys) {
                    handle(key);
                }
                selectionKeys.clear();//清除处理过的事件
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理不同的事件
     *
     * @param selectionKey
     * @throws IOException
     */
    private void handle(SelectionKey selectionKey) throws Exception {
        ServerSocketChannel serverSocketChannel = null;
        SocketChannel socketChannel = null;
        String requestMsg = "";
        int count = 0;
        if (selectionKey.isAcceptable()) {
            //每有客户端连接，即注册通信信道为可读
            serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (selectionKey.isReadable()) {
            socketChannel = (SocketChannel) selectionKey.channel();
            rBuffer.clear();
            count = socketChannel.read(rBuffer);
            //读取数据
            if (count > 0) {
                rBuffer.flip();
                requestMsg = String.valueOf(cs.decode(rBuffer).array());
            }
            String responseMsg = "已收到客户端的消息:" + requestMsg;
            System.out.println(responseMsg);
            String s = checkAllInfo(responseMsg);
            //返回数据
            sBuffer = ByteBuffer.allocate(s.getBytes("UTF-8").length);
            sBuffer.put(s.getBytes("UTF-8"));
            sBuffer.flip();
            socketChannel.write(sBuffer);
            socketChannel.close();
        }
    }

    public String checkAllInfo(String result) throws Exception {
        logger.info("进入校验回调");
        String dataCheckAll = "";
        /*
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String result = new String(outSteam.toByteArray(), "UTF-8");
        outSteam.close();
        inStream.close();
        */
        String result1 = decryptEcb(hexKey, result.substring(76));
        logger.info("进入校验回调返回的报文 {}", result1);
        ServiceFan service = (ServiceFan) xmlToBean(ServiceFan.class, result1);
        Head head = service.getHead();
        System.out.println(service.getHead().getID());
        dataCheckAll = getCheckPullInfo(service.getHead().getUUID());
        String fileName = path + "140725" + "To" + BankNo;
        String fileName1 = fileName + File.separator + CallUser + "_" + 140725 + "To" + BankNo + "_" + service.getHead().getUUID() + ".txt";

        File file = new File(fileName1);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "GBK");//FileInputStream字符流转换成字节流要注意编码
            BufferedReader br = new BufferedReader(isr);
            String line;//用来保存读取到的数据
            while ((line = br.readLine()) != null) {//每次读取一行不为空
                String s1 = decryptEcb(hexKey, line,"GBK");
                System.out.println(s1);
                String[] s = s1.split("\\n");
                for (int i = 0; i < s.length; i++) {
                    String[] aa = s[i].split("@");
                    if (aa[8].substring(2).equals("1")) {
                        Map map = new HashMap();
                        map.put("idCardNo", aa[2].substring(2));
                        map.put("bankCardNo", aa[4].substring(2));
                        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                        map.put("projectId", projectDetailEntity.getId());
                        List<PersonEntity> personEntities = personMapper._queryAll(map);
                        if (personEntities.size() > 0) {
                            for (PersonEntity personEntity : personEntities) {
                                personEntity.setStatus("1");
                                personService.update(personEntity);
                            }
                        }
                        projectDetailEntity.setState(5);
                        projectDetailMapper._updateEntity(projectDetailEntity);
                    } else {
                        Map map = new HashMap();
                        map.put("idCardNo", aa[2].substring(2));
                        map.put("bankCardNo", aa[4].substring(2));
                        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                        map.put("projectId", projectDetailEntity.getId());
                        List<PersonEntity> personEntities = personMapper._queryAll(map);
                        if (personEntities.size() > 0) {
                            for (PersonEntity personEntity : personEntities) {
                                personEntity.setStatus("2");
                                personEntity.setFailReason(aa[9].substring(2));
                                personService.update(personEntity);
                            }
                        }
                        projectDetailEntity.setState(6);
                        projectDetailMapper._updateEntity(projectDetailEntity);
                    }
                }
            }
            br.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("dataCheckAll {}", dataCheckAll);
        return dataCheckAll;
    }

    public String getCheckPullInfo(String id) throws Exception {
        Head head = new Head();
        head.setID(notifypullId);
        head.setUUID(id);
        head.setCallDate(DateUtil.getDay());
        head.setCallTime(DateUtil.getHms());
        head.setCallUser(CallUser);
        head.setCallRes("1");
        head.setError("");
        head.setDistrict("140725");
        head.setBankNo(BankNo);
        ServicePull service = new ServicePull();
        service.setHead(head);
        String s1 = convertToXmlService(service, "UTF-8");
        String d = encryptEcb(hexKey, s1);
        String b = this.getByteStream(d);
        String c = b + notifypullId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + d;
        return c;
    }

    public String getByteStream(String xml) throws UnsupportedEncodingException {
        byte[] bodyBytes = xml.getBytes("UTF-8");//获得body的字节数组
        int bodyLength = bodyBytes.length;
        //添加8位报文长度（我的博文中也有NumberFormat的用法介绍）
        java.text.DecimalFormat format = new java.text.DecimalFormat("00000000");
        String a = format.format(bodyLength);
        return a;
    }

}