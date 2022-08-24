package com.ky.ykt.controller;

import com.ky.ykt.entity.*;
import com.ky.ykt.entity.xml.*;
import com.ky.ykt.excle.ExcelStyle;
import com.ky.ykt.excle.ExportExcel;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mapper.*;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.AreasService;
import com.ky.ykt.service.PersonService;
import com.ky.ykt.service.ProjectDetailService;
import com.ky.ykt.service.ProjectService;
import com.ky.ykt.utils.DateUtil;
import com.ky.ykt.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ky.ykt.interceptor.DeleteFileSchedul.delFolder;
import static com.ky.ykt.utils.ForFile.*;
import static com.ky.ykt.utils.P_Sm4Util.decryptEcb;
import static com.ky.ykt.utils.P_Sm4Util.encryptEcb;
import static com.ky.ykt.utils.xmlUtilToBean.convertToXmlService;
import static com.ky.ykt.utils.xmlUtilToBean.xmlToBean;


@RestController
@RequestMapping("/ky-ykt/bankFile")
public class BankFileController {
    private static final Logger logger = LoggerFactory.getLogger(BankFileController.class);
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
    private static String BankNo = "601103";
    //
    private static String BankDep = "350355000001";
    //区县标识
    private static String District = "140725";
    //生成文件路径
    private static String path = "D:\\file\\";
    //文件路径+名称
    private static String filenameTemp;
    @Value("${hexKey}")
    private String hexKey;

    @Autowired
    ProjectService projectService;
    @Autowired
    PersonService personService;
    @Autowired
    ProjectDetailMapper projectDetailMapper;
    @Autowired
    PersonPullMapper personPullMapper;
    @Autowired
    PersonMapper personMapper;
    @Autowired
    ProjectDetailService projectDetailService;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    ProjectMapper projectMapper;

    //进入请求上卡接口
    @SuppressWarnings("rawtypes")
    @Log(description = "银行上卡操作", module = "人员管理")
    @RequestMapping(value = "/pullAllInfo", method = RequestMethod.GET)
    public RestResult pullAllInfo(HttpServletRequest request) {
        logger.info("进入请求上卡接口");
        try {
            Map params = HttpUtils.getParams(request);
            RestResult restResult = (RestResult) personService.queryAll(params);
            List<PersonEntity> data = (List<PersonEntity>) restResult.getData();
            ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(params.get("projectId").toString());
            ProjectEntity projectEntity = projectMapper._get(projectDetailEntity.getProjectId());
            if (!(projectDetailEntity.getTotalAmount().compareTo(projectEntity.getTotalAmount()) == 1)) {
                if (projectDetailEntity.getState() != null && projectDetailEntity.getState() != 5) {
                    String dataPull = getDataPull(request, data);
                    String sb = SocketServer.SoketPull("202.99.212.80", 8167, dataPull);
                    String s = decryptEcb(hexKey.toString(), sb);
                    System.out.println(s);
                    Service service = (Service) xmlToBean(Service.class, s.substring(77, s.length()));
                    System.out.println(service.getHead().getiD());
                    if (service != null && service.getHead().getCallRes() != null && service.getHead().getiD() != null) {
                        projectDetailEntity.setState(5);
                        projectDetailMapper._updateEntity(projectDetailEntity);
                    }
                    return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "成功发送上卡");
                }
            } else {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "数据错误，请联系管理员");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "请稍后再试");
    }


    public String getByteStream(String xml) throws UnsupportedEncodingException {
        byte[] bodyBytes = xml.getBytes("UTF-8");//获得body的字节数组
        int bodyLength = bodyBytes.length;
        //添加8位报文长度（我的博文中也有NumberFormat的用法介绍）
        java.text.DecimalFormat format = new java.text.DecimalFormat("00000000");
        String a = format.format(bodyLength);
        return a;
    }

    //多笔生成
    public String getDataCheckAll(HttpServletRequest request, List<PersonEntity> personEntities) throws Exception {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        Map map = new HashMap();
        map.put("projectId", personEntities.get(0).getProjectId());
        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(personEntities.get(0).getProjectId());
        String fileName = CallUser + "_" + "140725" + "To" + BankNo + "_" + projectDetailEntity.getId();
        filenameTemp = path + fileName + ".txt";
        String fileName1 = CallUser + "_" + BankNo + "To" + "140725" + "_" + projectDetailEntity.getId() + ".txt";
        File filee = new File(path + fileName1 + "//" + fileName1);
        File file1 = new File(path + fileName + ".txt");
        delFolder(path + fileName1);
        File file = new File(filenameTemp);
        file.delete();
        createFile(fileName);
        Head head = new Head();
        head.setiD(checkAllId);
        head.setUUID(projectDetailEntity.getId());
        head.setCallDate(DateUtil.getDay());
        head.setCallTime(DateUtil.getHms());
        head.setCallUser(CallUser);
//        head.setToken("111");
        head.setDistrict("140725");
        head.setBankNo(BankNo);
        Body body = new Body();
        body.setRowCnt(personEntities.size());
//        body.setExtend3("");
        body.setExtend4("");
        body.setExtend3("");
        List<Data> datas = new ArrayList<>();
        java.text.DecimalFormat format = new java.text.DecimalFormat("000000");
        int i = 0;
        String writeResult = "";
//        writeResult=personEntities.size()+"@|$"+""+"@|$"+""+"\n";
        for (PersonEntity personEntity : personEntities
        ) {
            Data data = new Data();
            data.setName(personEntity.getName());
            data.setIdNo(personEntity.getIdCardNo());
            data.setBankAcct(personEntity.getBankCardNo());
            data.setBankDep(BankDep);
            data.setExtend1(personEntity.getProjectId());
            datas.add(data);
            i++;
            writeResult += format.format(i) + "@|$" + personEntity.getName() + "@|$" + personEntity.getIdCardNo()
                    + "@|$" + personEntity.getBankCardNo() + "@|$" + "" + "@|$" + "" + "\n";
        }
        System.out.println(writeResult);
        writeFileContent(filenameTemp, encryptEcb(hexKey, writeResult));
        Service service = new Service();
//        body.setData(datas);
        service.setBody(body);
        service.setHead(head);
        String s1 = convertToXmlService(service, "UTF-8");
        String b = this.getByteStream(s1);
        String c = b + checkAllId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + s1;
        String d = encryptEcb(hexKey, c);
        System.out.println(s1);
        return d;

    }

    //单笔生成
    public String getDataCheckOne(HttpServletRequest request, PersonEntity personEntitie) throws Exception {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        Map map = new HashMap();
        map.put("projectId", personEntitie.getProjectId());
        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(personEntitie.getProjectId());
        projectDetailEntity.setState(4);
        projectDetailMapper._updateEntity(projectDetailEntity);
        Head head = new Head();
        head.setiD(checkId);
        head.setUUID(projectDetailEntity.getId());
        head.setCallDate(DateUtil.getDay());
        head.setCallTime(DateUtil.getHms());
        head.setCallUser(CallUser);
//        head.setToken("111");
        head.setDistrict("140725");
        head.setBankNo(BankNo);
        ServiceCheckOne service = new ServiceCheckOne();
        BodyCheckOne body = new BodyCheckOne();
        body.setName(personEntitie.getName());
        body.setIdNo(personEntitie.getIdCardNo());
        body.setBankAcct(personEntitie.getBankCardNo());
        body.setBankDep(BankDep);
        body.setExtend1("");
        body.setExtend2("");
//        body.setExtend1(personEntitie.getProjectId());
        service.setBody(body);
        service.setHead(head);
        String s1 = convertToXmlService(service, "UTF-8");
        System.out.println(s1);
        String b = this.getByteStream(s1);
        String c = b + checkId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + s1;
        String d = encryptEcb(hexKey, c);
        return d;
    }


    public String getDataPull(HttpServletRequest request, List<PersonEntity> personEntities) throws Exception {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        Map map = new HashMap();
        map.put("projectId", personEntities.get(0).getProjectId());
        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(personEntities.get(0).getProjectId());
        String fileName = CallUser + "_" + "140725" + "To" + BankNo + "_" + projectDetailEntity.getId();
        filenameTemp = path + fileName + ".txt";
        createFile(fileName);
        java.text.DecimalFormat format = new java.text.DecimalFormat("000000");
        Head head = new Head();
        head.setiD(infoId);
        head.setUUID(projectDetailEntity.getId());
        head.setCallDate(DateUtil.getDay());
        head.setCallTime(DateUtil.getHms());
        head.setCallUser(CallUser);
        head.setDistrict("140725");
        head.setBankNo(BankNo);
        BodyPull body = new BodyPull();
        //一卡通测试账户
        //601103010300000250149
        //205103
        //付款人账号
        body.setPayeeAcctNo("601103010300000250149");
        //付款人名称
        body.setPayeeAcctName("晋中市财政局一卡通");
        //付款人开户行
        body.setPayeeAcctDep("601103");
        body.setProjName(projectDetailEntity.getProjectName());
        body.setAmtSum(new BigDecimal(String.valueOf(projectDetailEntity.getPaymentAmount().multiply(BigDecimal.valueOf(100)))).setScale(0, BigDecimal.ROUND_HALF_UP));
        body.setRowCnt(personEntities.size());
        body.setExtend3("");
        body.setExtend4("");
        body.setExtend5("");
        body.setExtend6("");
        int i = 0;
        String writeResult = "";
        List<Data> datas = new ArrayList<>();
        for (PersonEntity personEntity : personEntities
        ) {
            i++;
            BigDecimal num1 = new BigDecimal(personEntity.getGrantAmount());
            BigDecimal num2 = new BigDecimal(100);
            writeResult += format.format(i)
                    + "@|$" + personEntity.getName()
                    + "@|$" + personEntity.getIdCardNo()
                    + "@|$" + new BigDecimal(String.valueOf(num1.multiply(num2))).setScale(0, BigDecimal.ROUND_HALF_UP)
                    + "@|$" + personEntity.getBankCardNo()
                    + "@|$" + "0"
                    + "@|$" + ""
                    + "@|$" + ""
                    + "@|$" + "" + "@|$" + "" + "\n";
            //personEntity.getProjectId()
        }
        writeFileContent(filenameTemp, encryptEcb(hexKey, writeResult));
        ServicePull service = new ServicePull();
        service.setBody(body);
        service.setHead(head);
        String s1 = convertToXmlService(service, "UTF-8");
        String b = this.getByteStream(s1);
        String c = b + infoId + " " + "       " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + s1;
        String d = encryptEcb(hexKey, c);
        System.out.println(s1);
        return d;
    }


    //上卡
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/checkOneInfo", method = RequestMethod.GET)
    public RestResult checkOneInfo(HttpServletRequest request) {
        try {
            Map params = HttpUtils.getParams(request);
            RestResult restResult = (RestResult) personService.queryAll(params);
            List<PersonEntity> data = (List<PersonEntity>) restResult.getData();
            String dataCheckAll = "";
            if (data.size() > 1) {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "请使用多笔校验");
            } else {
                dataCheckAll = getDataCheckOne(request, data.get(0));
                String sb = SocketServer.SoketPull("202.99.212.80", 8167, dataCheckAll);
                String s = decryptEcb(hexKey, sb);
                System.out.println(s);
                ServiceCheckOne service = (ServiceCheckOne) xmlToBean(ServiceCheckOne.class, s.substring(77, s.length()));
                System.out.println(service.getHead().getiD());
                if (service != null && service.getHead().getCallRes() != null && service.getHead().getiD() != null) {
                    System.out.println(service.getBody().getResult());
                    if (service.getBody().getResult().equals("1")) {
                        //成功
                        Map map = new HashMap();
                        map.put("idCardNo", service.getBody().getIdNo());
                        map.put("bankCardNo", service.getBody().getBankAcct());
                        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                        map.put("projectId", projectDetailEntity.getId());
                        PersonEntity personEntity = personMapper._getPerson(map);
                        personEntity.setStatus("5");
                        personService.update(personEntity);
                    } else {
                        //失败
                        Map map = new HashMap();
                        map.put("idCardNo", service.getBody().getIdNo());
                        map.put("bankCardNo", service.getBody().getBankAcct());
                        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                        map.put("projectId", projectDetailEntity.getId());
                        PersonEntity personEntity = personMapper._getPerson(map);
                        personEntity.setStatus("6");
                        personEntity.setFailReason(service.getBody().getErrorMsg());
                        personService.update(personEntity);
                    }
                    Map map = new HashMap();
                    ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                    map.put("projectId", projectDetailEntity.getId());
                    map.put("status", 6);
                    List<PersonEntity> personEntities1 = personMapper._queryAll(map);
                    if (personEntities1.size() > 0) {
                        //成功
                        projectDetailEntity.setState(5);
                    } else {
                        //失败
                        projectDetailEntity.setState(6);
                    }
                    projectDetailMapper._updateEntity(projectDetailEntity);
                    return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "成功发送校验");
                } else {
                    return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "发送校验失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "请稍后再试");
        }
    }


    //多笔校验
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/checkAllInfo", method = RequestMethod.GET)
    public RestResult checkAllInfo(HttpServletRequest request) {
        try {
            Map params = HttpUtils.getParams(request);
            RestResult restResult = (RestResult) personService.queryAll(params);
            List<PersonEntity> data = (List<PersonEntity>) restResult.getData();
            ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(params.get("projectId").toString());
            if (projectDetailEntity.getState() == 4) {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "该项目已发送校验");
            } else {
                String dataCheckAll = "";
                if (data.size() > 1) {
                    dataCheckAll = getDataCheckAll(request, data);
                } else {
                    return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "请使用单笔校验");
                }
                String sb = SocketServer.SoketPull("202.99.212.80", 8167, dataCheckAll);
                String s = decryptEcb(hexKey.toString(), sb);
                Service service = (Service) xmlToBean(Service.class, s.substring(77, s.length()));
                System.out.println(service.getHead().getiD());
                if (service != null && service.getHead().getCallRes() != null && service.getHead().getiD() != null) {
                    projectDetailEntity.setState(4);
                    projectDetailMapper._updateEntity(projectDetailEntity);
                }
                System.out.println(s);
                return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "成功发送校验");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "请稍后再试");
        }
    }






    //http:172.30.32.253:1005/ky-ykt/bankFile/notifyPullAll
    //10.32.48.128.1005
    //34022  14019
    //多笔校验和上卡接口返回
    @RequestMapping(value = "/notifyCheckAll", method = RequestMethod.POST)
    @ResponseBody
    public String notifyCheckAll(HttpServletRequest request) {
        logger.info("进入校验回调");
        String dataCheckAll = "";
        try {
            InputStream inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            String result = new String(outSteam.toByteArray(), "UTF-8");
            String result1 = decryptEcb(hexKey, result);
            outSteam.close();
            inStream.close();
            System.out.println(result1);
            Service service = (Service) xmlToBean(Service.class, result1.substring(77, result1.length()));
            Head head = service.getHead();
            System.out.println(service.getHead().getiD());
            if (head.getiD().equals(notifycheckId)) {
                dataCheckAll = getCheckAllInfo(request, service.getHead().getUUID());
                String fileName1 = CallUser + "_" + BankNo + "To" + "140725" + "_" + service.getHead().getUUID() + ".txt";
                filenameTemp = path + fileName1 + "\\" + fileName1;
                File file = new File(filenameTemp);
                try {
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis, "GBK");//FileInputStream字符流转换成字节流要注意编码
                    BufferedReader br = new BufferedReader(isr);
                    String line;//用来保存读取到的数据
                    while ((line = br.readLine()) != null) {//每次读取一行不为空
                        String s1 = decryptEcb(hexKey, line);
                        System.out.println(s1);
                        String[] s = decryptEcb(hexKey, line).split("\\n");
                        for (int i = 0; i < s.length; i++) {
                            String[] aa = s[i].split("@" + "\\|" + "\\$");
                            if (aa[4].equals("1")) {
                                Map map = new HashMap();
                                map.put("idCardNo", aa[2]);
                                map.put("bankCardNo", aa[3]);
                                ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                                map.put("projectId", projectDetailEntity.getId());
                                PersonEntity personEntity = personMapper._getPerson(map);
                                personEntity.setStatus("5");
                                personService.update(personEntity);
                            } else {
                                Map map = new HashMap();
                                map.put("idCardNo", aa[2]);
                                map.put("bankCardNo", aa[3]);
                                ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                                map.put("projectId", projectDetailEntity.getId());
                                PersonEntity personEntity = personMapper._getPerson(map);
                                personEntity.setStatus("6");
                                personEntity.setFailReason(aa[5]);
                                personService.update(personEntity);
                            }
                        }
                        Map map = new HashMap();
                        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                        map.put("projectId", projectDetailEntity.getId());
                        map.put("status", 6);
                        List<PersonEntity> personEntities1 = personMapper._queryAll(map);
                        if (personEntities1.size() > 0) {
                            projectDetailEntity.setState(5);
                        } else {
                            projectDetailEntity.setState(6);
                            Map map1 = new HashMap();
                            map1.put("projectId", projectDetailEntity.getId());
                            map1.put("status", "5");
                            List<PersonEntity> personEntities = personMapper._queryAll(map1);
                            BigDecimal totalAmount = new BigDecimal("0");
                            for (PersonEntity personEntity : personEntities) {
                                BigDecimal bigDecimal = new BigDecimal(personEntity.getGrantAmount());
                                totalAmount = totalAmount.add(bigDecimal);
                            }
                            projectDetailEntity.setPaymentAmount(totalAmount);
                        }
                        projectDetailMapper._updateEntity(projectDetailEntity);
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
            } else if (head.getiD().equals(notifypullId)) {
                dataCheckAll = getCheckPullInfo(request, service.getHead().getUUID());
                String fileName1 = CallUser + "_" + BankNo + "To" + "140725" + "_" + service.getHead().getUUID() + ".txt";
                filenameTemp = path + fileName1 + "\\" + fileName1;
                File file = new File(filenameTemp);
                try {
                    FileInputStream fis = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(fis, "GBK");//FileInputStream字符流转换成字节流要注意编码
                    BufferedReader br = new BufferedReader(isr);
                    String line;//用来保存读取到的数据
                    while ((line = br.readLine()) != null) {//每次读取一行不为空
                        String s1 = decryptEcb(hexKey, line);
                        System.out.println(s1);
                        String[] s = decryptEcb(hexKey, line).split("\\n");
                        for (int i = 0; i < s.length; i++) {
                            String[] aa = s[i].split("@" + "\\|" + "\\$");
                            if (aa[8].equals("1")) {
                                Map map = new HashMap();
                                map.put("idCardNo", aa[2]);
                                map.put("bankCardNo", aa[4]);
                                ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                                map.put("projectId", projectDetailEntity.getId());
                                List<PersonEntity> personEntities = personMapper._queryAll(map);
                                if (personEntities.size() > 0) {
                                    for (PersonEntity personEntity : personEntities) {
                                        personEntity.setStatus("1");
                                        personService.update(personEntity);
                                    }
                                }
                            } else {
                                Map map = new HashMap();
                                map.put("idCardNo", aa[2]);
                                map.put("bankCardNo", aa[4]);
                                ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                                map.put("projectId", projectDetailEntity.getId());
                                List<PersonEntity> personEntities = personMapper._queryAll(map);
                                if (personEntities.size() > 0) {
                                    for (PersonEntity personEntity : personEntities) {
                                        personEntity.setStatus("2");
                                        personEntity.setFailReason(aa[9]);
                                        personService.update(personEntity);
                                    }
                                }
                            }
                        }
                        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                        projectDetailEntity.setState(3);
                        projectDetailMapper._updateEntity(projectDetailEntity);
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(dataCheckAll);
        return dataCheckAll;
    }

    public String getCheckAllInfo(HttpServletRequest request, String id) throws Exception {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        Head head = new Head();
        head.setiD(notifycheckId);
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
        String b = this.getByteStream(s1);
        String c = b + notifycheckId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + s1;
        String d = encryptEcb(hexKey, c);
        System.out.println(s1);
        return d;
    }

    public String getCheckPullInfo(HttpServletRequest request, String id) throws Exception {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        Head head = new Head();
        head.setiD(notifypullId);
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
        String b = this.getByteStream(s1);
        String c = b + notifypullId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + s1;
        String d = encryptEcb(hexKey, c);
        System.out.println(s1);
        return d;
    }

//
//    @RequestMapping(value = "fileDown", method = RequestMethod.GET)
//    public String downloadFile(String id, HttpServletRequest request, HttpServletResponse response) {
//        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(id);
//        String fileName1 = CallUser + "_" + BankNo + "To" + "140725" + "_" + projectDetailEntity.getDetailNewId() + ".txt";
//        filenameTemp = path + fileName1 + "\\" + fileName1;
//        File file = new File(filenameTemp);
//        DataInputStream in = null;
//        OutputStream out = null;
//        response.reset();//
//        try {
//            response.setCharacterEncoding("UTF-8");
//            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "GBK"));// 设定输出文件头
//            response.setContentType("application/octet-stream");// 定义输出类型
//            //输入流：本地文件路径
//            FileInputStream fis = new FileInputStream(file);
//            InputStreamReader isr = new InputStreamReader(fis, "GBK");//FileInputStream字符流转换成字节流要注意编码
//            BufferedReader br = new BufferedReader(isr);
//            String hexKey = "C7046F00DF1F8583F1AB0A488A5E470E";
//            String line;//用来保存读取到的数据
//            while ((line = br.readLine()) != null) {//每次读取一行不为空
//                String s1 = decryptEcb(hexKey, line);
//                InputStream myIn = new ByteArrayInputStream(s1.getBytes());
//                in = new DataInputStream(myIn);
//                out = response.getOutputStream();
//                //输出文件
//                int bytes = 0;
//                byte[] bufferOut = new byte[1024];
//                while ((bytes = in.read(bufferOut)) != -1) {
//                    out.write(bufferOut, 0, bytes);
//                }
//            }
//            //输出流
//        } catch (Exception e) {
//            logger.error("下载文件出错：", e);
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Log(description = "文件下载操作", module = "人员管理")
    @RequestMapping(value = "fileDown", method = RequestMethod.GET)
    public void fileDown(String id, HttpServletRequest request, HttpServletResponse response) {
        Map map = this.personFileDown(id);
        String[] header = (String[]) map.get("header");
        List<String[]> data = (List<String[]>) map.get("data");
        ExcelStyle style = (ExcelStyle) map.get("style");
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((style.getXlsName() + ".xls").getBytes(), "iso-8859-1"));
            OutputStream out = response.getOutputStream();
            ExportExcel.export(header, data, style, out);
        } catch (Exception e) {
            logger.error("exportExcel error:{}", e);
        }
    }
    @Autowired
    AreasService areasService;

    public Map personFileDown(String id) {
        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(id);
        String fileName1 = CallUser + "_" + BankNo + "To" + "140725" + "_" + projectDetailEntity.getId() + ".txt";
        filenameTemp = path + fileName1 + "\\" + fileName1;
        File file = new File(filenameTemp);
        Map resultMap = new HashMap();
        ExcelStyle style = new ExcelStyle();
        List<String[]> data = new ArrayList();
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
        String tStamp = dfs.format(new Date());
        style.setColumnWidth(25);
        style.setSheetName("导出");
        style.setXlsName("人员信息表_" + tStamp);
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "GBK");//FileInputStream字符流转换成字节流要注意编码
            BufferedReader br = new BufferedReader(isr);
            String hexKey = "C7046F00DF1F8583F1AB0A488A5E470E";
            String line;//用来保存读取到的数据
            while ((line = br.readLine()) != null) {//每次读取一行不为空
                String s1 = decryptEcb(hexKey, line);
                System.out.println(s1);
                String[] s = decryptEcb(hexKey, line).split("\\n");
                for (int i = 0; i < s.length; i++) {
                    String[] aa = s[i].split("@" + "\\|" + "\\$");
                    BigDecimal num1 = new BigDecimal(aa[3]);
                    BigDecimal num2 = new BigDecimal(100);
                    String idCardNo = aa[2];
                    PersonEntity personEntity = personMapper.queryIdCardNo(idCardNo);
                    String county=personEntity.getTown();
                    String village=personEntity.getVillage();
                    AreasEntity areasEntity = areasService.get(county);
                    AreasEntity areasEntity1 = areasService.get(village);
                    data.add(new String[]{
                            aa[1],
                            aa[2],
                            new BigDecimal(String.valueOf(num1.divide(num2))).setScale(2, BigDecimal.ROUND_HALF_UP).toString(),
                            aa[4],
                            aa[8],
                            aa[9],
                            areasEntity.getName(),
                            areasEntity1.getName(),
                    });
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
        resultMap.put("header",
                new String[]{"姓名", "身份证号", "金额", "卡号", "发放结果", "处理结果","乡镇","村名"});
        resultMap.put("data", data);
        resultMap.put("style", style);
        return resultMap;

    }


    public static void main(String[] args) {
        File file = new File("D:\\file\\22222.txt");
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, "GBK");//FileInputStream字符流转换成字节流要注意编码
            BufferedReader br = new BufferedReader(isr);
            String hexKey = "C7046F00DF1F8583F1AB0A488A5E470E";
            String line;//用来保存读取到的数据
            while ((line = br.readLine()) != null) {//每次读取一行不为空
                String s1 = decryptEcb(hexKey, line);
                System.out.println(s1);
                String[] s = decryptEcb(hexKey, line).split("\\n");
                for (int i = 0; i < s.length; i++) {
                    String[] aa = s[i].split("@" + "\\|" + "\\$");
                    BigDecimal num1 = new BigDecimal(aa[3]);
                    BigDecimal num2 = new BigDecimal(100);
                    System.out.println(new BigDecimal(String.valueOf(num1.divide(num2))).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                    for (int j = 0; j < aa.length; j++) {
                        System.out.println("---" + j + "---" + aa[j]);
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
    }


}
