package com.ky.ykt.controller;

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

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ky.ykt.utils.P_Sm4Util.decryptEcb;
import static com.ky.ykt.utils.P_Sm4Util.encryptEcb;
import static com.ky.ykt.utils.xmlUtilToBean.convertToXmlService;
import static com.ky.ykt.utils.xmlUtilToBean.xmlToBean;

public class clientHandler extends Thread {

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
    //生成文件路径
    private static String path = "D:\\file\\";

    private static String hexKey = "C7046F00DF1F8583F1AB0A488A5E470E";

    ProjectDetailMapper projectDetailMapper = (ProjectDetailMapper) SpringUtil.getBean("projectDetailMapper");
    PersonMapper personMapper = (PersonMapper) SpringUtil.getBean("personMapper");
    PersonService personService = (PersonService) SpringUtil.getBean("personService");

    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    public clientHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        super.run();
        System.out.println("新客服端连接:" + socket.getInetAddress() + "P:" + socket.getPort());
        try {
            //得到打印流,用于数据输出;服务器回送数据使用
            //PrintStream printStream = new PrintStream(socket.getOutputStream());
            OutputStream outputStream = socket.getOutputStream();
            //约定的报文头长度
            int headLen = 8;
            char [] xmlLen = new char[headLen];
            int curHeadLength = 0;
            //得到输入流,用于接收数据
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //String s = bufferedReader.readLine();
            while(curHeadLength < headLen){
                int readLen = bufferedReader.read(xmlLen,curHeadLength,headLen-curHeadLength);
                //注意这里的判断不能省略 因为当报文结束时read()方法返回值为-1  此时如果我们读到的结束符 ， readLen 就会被赋值为-1 ，那么循环就会继续，结果就会造成xmlLen数组的下标越界，自然会得到错误的结果
                if(readLen < 0){
                    break;
                }
                curHeadLength += readLen;
            }

            int bodyLength = Integer.parseInt(new String(xmlLen))+69;
            char [] xml = new char[bodyLength];
            int curBodyLength = 0;
            while(curBodyLength < bodyLength){
                int readLen2 = bufferedReader.read(xml,curBodyLength,bodyLength-curBodyLength);
                if(readLen2 < 0){
                    break;
                }
                curBodyLength += readLen2;
            }

            String s = new String(xml).substring(68);

            //String s = bufferedReader.readLine();
            //String s = "00000620BT006                                                                GpatOZTzJCH5BjwaBBcWZ5wdznGe+/NufiWigbBPkoaop4S/Mw7gyAT34NSA84bn9D5XPQDfodXSJQK3J7ZyO0RwPvrxEY23Kbjdn+MfyR/icne3MJZ3Q2WtHtrLawxq9aGO35Ru0CDmZl6nq+HkQtT+ljUjWTgKgmcImh4RvuxIPqTVznURfiQnQSSzz+v5JpfgXJtZl4YvOJW8RiAJuMxJyzGHg5xMjXm5UXuY0Hx2ARnL7LFNlLCNCMnpiJOPVNVQ1tn7YZk0EDq92OIMv5c/eRbRbNnayqO5kR1UR1Lw1wVpsvg9QwfVn9NebA7gGbGvhrSH2cX0K4jD072a0NDgm+9jjxrKZEp0PNppRz/RjGVUE/MS2lrps2SJhKnl2RGUDYmJJiOkgH9JhXo2Gr+TbhA+S249F9BVZoqNIhJiVtKyKvqUHjooRDi4rHXdbSCy3xFDLU4qaNjKwaGnvGpd4QVhWw5d1lBPFWJDIlwTds6SogaZyCyqRGw7zQDU3LrO+kE6Ws8debnGof+lZTtkRooL0TyUrxS1ZYF0EaEEMO3DIs4EYQa78se2s/wc3gUFxcmRV+87hH3m4DHg9dxz5lX527bvJnsWFe+N5zU=";
            //打印到屏幕.并回送数据长度
            System.out.println(s);
            String s1 = checkAllInfo(s.trim());
            //printStream.println("回送"+s.length());
            //printStream.println(s1);
            outputStream.write(s1.getBytes("UTF-8"));
            //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
            outputStream.flush();
            bufferedReader.close();
            //printStream.close();
            outputStream.close();
            //socket.shutdownOutput();
        } catch (Exception e) {
            System.out.println("连接异常断开");
        } finally {
            //连接关闭
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("客户端已退出" + socket.getInetAddress() + "P:" + socket.getPort());
    }

    public String checkAllInfo(String result) throws Exception {
        logger.info("进入校验回调");
        String dataCheckAll = "";
        String result1 = decryptEcb(hexKey, result, "UTF-8");
        logger.info("进入校验回调返回的报文 {}", result1);
        ServiceFan service = (ServiceFan) xmlToBean(ServiceFan.class, result1);
        Head head = service.getHead();
        System.out.println(service.getHead().getID());
        dataCheckAll = getCheckPullInfo(service.getHead().getUUID());
        String fileName = path + BankNo + "To" + "140725";
        String fileName1 = fileName + File.separator + CallUser + "_" + BankNo + "To" + 140725 + "_" + service.getHead().getUUID() + ".txt";

        File file = new File(fileName1);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "GBK");//FileInputStream字符流转换成字节流要注意编码
            BufferedReader br = new BufferedReader(isr);
            String line;//用来保存读取到的数据
            while ((line = br.readLine()) != null) {//每次读取一行不为空
                String s1 = decryptEcb(hexKey, line, "GBK");
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
                        //projectDetailEntity.setState(5);
                        //projectDetailMapper._updateEntity(projectDetailEntity);
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
                        //projectDetailEntity.setState(6);
                        //projectDetailMapper._updateEntity(projectDetailEntity);
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