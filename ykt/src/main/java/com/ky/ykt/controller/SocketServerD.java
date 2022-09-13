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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
@Component
public class SocketServerD implements Serializable{

    private static final Logger logger = LoggerFactory.getLogger(SocketServerD.class);

    //解码buffer
    private Charset cs = Charset.forName("UTF-8");
    //接受数据缓冲区
    private static ByteBuffer sBuffer = ByteBuffer.allocate(1024);
    //发送数据缓冲区
    private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    //选择器（叫监听器更准确些吧应该）
    private static Selector selector;

    /**
     * 启动socket服务，开启监听
     *
     * @param port
     * @throws IOException
     */
    public void startSocketServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);

        System.out.println("服务器准备就绪");
        System.out.println("服务器信息"+serverSocket.getInetAddress()+"P:"+serverSocket.getLocalPort());

        //等待客户端连接
        for(;;){
            //得到客户端
            Socket accept = serverSocket.accept();
            //客户端构建异步线程
            clientHandler clientHandler = new clientHandler(accept);
            //启动线程
            clientHandler.start();
        }
        /*
        try {
            ServerSocket ss = new ServerSocket(port);
            logger.info("启动服务器....");
            Socket s = ss.accept();
            logger.info("客户端:"+ InetAddress.getLocalHost()+"已连接到服务器");
            s.setSoTimeout(6000);
            InputStream inputStream = s.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len, "UTF-8"));
                logger.info("接收数据：" + sb);
                //写数据发送
                //outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                //outputStream.flush();
            }
            logger.info("接收数据============" + sb);
            //inputStream.close();
            String res = sb.toString();
            String s1 = checkAllInfo(res);
            s.shutdownInput();
            OutputStream outputStream = s.getOutputStream();
            outputStream.write(s1.getBytes());
            outputStream.flush();
            s.shutdownOutput();
            //s.close();
            //ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        */
    }


}