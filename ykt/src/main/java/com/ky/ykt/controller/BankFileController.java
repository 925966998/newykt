package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
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
import org.apache.commons.beanutils.BeanUtils;
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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
    private static String BankNo = "402161002352";
    //
    private static String BankDep = "601103";
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
    @Autowired
    PersonUploadMapper personUploadMapper;
    @Autowired
    PersonReplacementMapper personReplacementMapper;

    //进入请求上卡接口
    @SuppressWarnings("rawtypes")
    @Log(description = "银行上卡操作", module = "人员管理")
    @RequestMapping(value = "/pullAllInfo", method = RequestMethod.GET)
    public RestResult pullAllInfo(HttpServletRequest request) {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
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
                    String sb = SocketServer.SoketPull("202.99.212.79", 8212, dataPull);
                    System.out.println("sb=" + sb);
                    String s = decryptEcb(hexKey.toString(), sb.substring(76, sb.length()));
                    System.out.println("s=" + s);
                    ServiceOne service = (ServiceOne) xmlToBean(ServiceOne.class, s);
                    System.out.println(service.getHead().getID());
                    BigDecimal bigDecimal = personUploadMapper.queryPaymentAmount(data.get(0).getProjectId());
                    if (service.getHead().getCallRes().equals("1")) {
                        updatePerson(data, user, "成功");
                        projectDetailEntity.setState(5);
                        projectDetailEntity.setPaymentAmount(bigDecimal);
                        projectDetailEntity.setSurplusAmount(projectDetailEntity.getTotalAmount().subtract(bigDecimal));
                        projectDetailMapper._updateEntity(projectDetailEntity);
                        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "成功发送上卡");
                    } else {
                        updatePerson(data, user, "失败");
                        projectDetailEntity.setState(6);
                        projectDetailMapper._updateEntity(projectDetailEntity);
                        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, service.getHead().getError());
                    }
                }
            } else {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "数据异常，请联系管理员");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "系统异常，请稍后再试");
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
        head.setID(checkAllId);
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
                    + "@|$" + personEntity.getBankCardNo() + "@|$" + "" + "@|$" + "";
        }
        System.out.println("wr=" + writeResult);

        writeFileContent(filenameTemp, encryptEcb(hexKey, writeResult));
        Service service = new Service();
//        body.setData(datas);
        service.setBody(body);
        service.setHead(head);
        String s1 = convertToXmlService(service, "UTF-8");
        String b = this.getByteStream(s1);
        String c = b + checkAllId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + s1;
        String d = encryptEcb(hexKey, c, "UTF-8");
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
        head.setID(checkId);
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
        logger.info("into BankFileController getDataPull method params personEntities is {}", JSON.toJSONString(personEntities));
        Map map = new HashMap();
        map.put("projectId", personEntities.get(0).getProjectId());
        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(personEntities.get(0).getProjectId());
        List<ProjectEntity> projectEntities = projectMapper.queryProject(map);
        logger.info("获得补贴项目 {}", JSON.toJSONString(projectEntities));
        String fileName = path + "140725" + "To" + BankNo;
        File file = new File(fileName);
        if (!file.exists()) {
            file.mkdir();
        }
        //createFile(fileName);
        String fileName1 = fileName + File.separator + CallUser + "_" + 140725 + "To" + BankNo + "_" + projectDetailEntity.getId() + ".txt";
        File file1 = new File(fileName1);
        logger.info("文件路径 {}", file1);
        if (!file1.exists()) {
            file1.createNewFile();
        } else {
            file1.delete();
            file1.createNewFile();
        }

        java.text.DecimalFormat format = new java.text.DecimalFormat("000000");
        Head head = new Head();
        head.setID(infoId);
        head.setUUID(projectDetailEntity.getId());
        head.setCallDate(DateUtil.getDay());
        head.setCallTime(DateUtil.getHms());
        head.setCallUser(CallUser);
        head.setDistrict("140725");
        head.setBankNo(BankNo);
        logger.info("报文head {}", JSON.toJSONString(head));
        BodyPull body = new BodyPull();
        //一卡通测试账户
        //付款人账号
        body.setPayeeAcctNo("601103010300000207631");
        //付款人名称
        body.setPayeeAcctName("晋中快云");
        //付款人开户行
        //body.setPayeeAcctDep("402161002352");
        body.setPayeeAcctDep("601103");
        body.setProjName(projectEntities.get(0).getProjectTypeName());
        BigDecimal bigDecimal = personUploadMapper.queryPaymentAmount(personEntities.get(0).getProjectId());
        body.setAmtSum(bigDecimal.multiply(BigDecimal.valueOf(100)).setScale(0, BigDecimal.ROUND_HALF_UP));
        body.setRowCnt(personEntities.size());
        body.setExtend3("");
        body.setExtend4("");
        body.setExtend5("");
        body.setExtend6("");
        logger.info("报文body {}", JSON.toJSONString(body));
        int i = 0;
        String writeResult = "";

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
                    + "@|$" + ""
                    + "@|$" + ""
                    + "\n";
            //personEntity.getProjectId()
        }
        logger.info("文件内容 {}", writeResult);
/*

        FileOutputStream fileOutputStream = new FileOutputStream(fileName1);
        String wwe = encryptEcb(hexKey,writeResult,"GB18030");
        logger.info("加密之后的文件内容 {}",wwe);

        fileOutputStream.write(wwe.getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
        logger.info("文件输出完毕 {}",fileName1);
*/

        writeFileContent(fileName1, writeResult);
        ServicePull service = new ServicePull();
        service.setBody(body);
        service.setHead(head);
        String s1 = convertToXmlService(service, "UTF-8");
        logger.info("convertToXmlService result is {}", s1);
        String d = encryptEcb(hexKey, s1, "UTF-8");
        String b = getByteStream(d);
        String c = b + infoId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + d;
        logger.info("加密后的xml {},长度 {},拼装好的报文 {}", d, b, c);
        return c;
    }


    //上卡
    @SuppressWarnings("rawtypes")
    //@RequestMapping(value = "/checkOneInfo", method = RequestMethod.GET)
    public RestResult checkOneInfo1(HttpServletRequest request) {
        try {
            Map params = HttpUtils.getParams(request);
            RestResult restResult = (RestResult) personService.queryAll(params);
            List<PersonEntity> data = (List<PersonEntity>) restResult.getData();
            String dataCheckAll = "";
            if (data.size() > 1) {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "请使用多笔校验");
            } else {
                dataCheckAll = getDataCheckOne(request, data.get(0));
                String sb = SocketServer.SoketPull("202.99.212.79", 8212, dataCheckAll);
                String s = decryptEcb(hexKey, sb);
                System.out.println(s);
                ServiceCheckOne service = (ServiceCheckOne) xmlToBean(ServiceCheckOne.class, s.substring(76, s.length()));
                System.out.println(service.getHead().getID());
                if (service != null && service.getHead().getCallRes() != null && service.getHead().getID() != null) {
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

    //上卡
    @SuppressWarnings("rawtypes")
    //BT009
    @RequestMapping(value = "/checkOneInfo", method = RequestMethod.POST)
    public String checkOneInfo(HttpServletRequest request) {
        logger.info("进入校验回调");
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
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
            logger.info("进入校验回调返回的报文 {}", result1);
            Service service = (Service) xmlToBean(Service.class, result1.substring(76));
            Head head = service.getHead();
            System.out.println(service.getHead().getID());
            dataCheckAll = getCheckAllInfo(service.getHead().getUUID());
            String fileName = path + "140725" + "To" + BankNo;
            String fileName1 = fileName + File.separator + CallUser + "_" + 140725 + "To" + BankNo + service.getHead().getUUID() + ".txt";

            File file = new File(fileName1);
            try {
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis, "GBK");//FileInputStream字符流转换成字节流要注意编码
                BufferedReader br = new BufferedReader(isr);
                String line;//用来保存读取到的数据
                while ((line = br.readLine()) != null) {//每次读取一行不为空
                    String s1 = decryptEcb(hexKey, line);
                    logger.info("解密gbk {}", s1);
                    String[] s = decryptEcb(hexKey, line).split("\\n");
                    logger.info("解密gbk去掉换行 {}", s);
                    BigDecimal bigDecimal = BigDecimal.ZERO;
                    ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(service.getHead().getUUID());
                    ProjectEntity projectEntity = projectMapper._get(projectDetailEntity.getId());
                    for (int i = 0; i < s.length; i++) {
                        String[] aa = s[i].split("@" + "\\|" + "\\$");
                        if (aa[4].equals("1")) {
                            Map map = new HashMap();
                            map.put("idCardNo", aa[2]);
                            map.put("bankCardNo", aa[3]);
                            map.put("projectId", projectDetailEntity.getId());
                            projectDetailEntity.setState(3);
                            PersonEntity personEntity = personMapper._getPerson(map);
                            PersonUploadEntity personUploadEntity1 = personUploadMapper._queryPersonId(personEntity.getId());
                            PersonReplacementEntity personReplacementEntity = new PersonReplacementEntity();
                            bigDecimal = bigDecimal.add(new BigDecimal(personEntity.getGrantAmount()));
                            if (personUploadEntity1 != null) {
                                BeanUtils.copyProperties(personEntity, personUploadEntity1);
                                personUploadEntity1.setProjectId(service.getHead().getUUID());
                                personUploadEntity1.setProjectType(projectEntity.getProjectType());
                                personUploadMapper._updateEntity(personUploadEntity1);
                            } else {
                                PersonUploadEntity personUploadEntity2 = personUploadMapper.queryPerson(map);
                                if (personUploadEntity2 == null) {
                                    PersonUploadEntity personUploadEntity = new PersonUploadEntity();
                                    BeanUtils.copyProperties(personEntity, personUploadEntity);
                                    personUploadEntity.setId(UUID.randomUUID().toString());
                                    personUploadEntity.setProjectType("0");
                                    personUploadMapper._addEntity(personUploadEntity);
                                }
                                PersonUploadEntity personUploadEntity = new PersonUploadEntity();
                                BeanUtils.copyProperties(personEntity, personUploadEntity);
                                personUploadEntity.setPersonId(personEntity.getId());
                                personUploadEntity.setProjectId(service.getHead().getUUID());
                                personUploadEntity.setProjectType(projectEntity.getProjectType());
                                personUploadMapper._addEntity(personUploadEntity);
                            }
                            personReplacementEntity.setId(UUID.randomUUID().toString());
                            personReplacementEntity.setPersonId(personEntity.getId());
                            personReplacementEntity.setReplacementAmount(personEntity.getGrantAmount());
                            personReplacementEntity.setDepartmentId(personEntity.getDepartmentId());
                            personReplacementEntity.setUserId(user.getId());
                            personReplacementEntity.setStatus("4");
                            personReplacementEntity.setProjectId(service.getHead().getUUID());
                            personReplacementMapper._addEntity(personReplacementEntity);
                            personEntity.setStatus("4");//已提交
                            personEntity.setProjectId(service.getHead().getUUID());
                            personMapper._updateEntity(personEntity);
                            personEntity.setStatus("7");
                            personService.update(personEntity);
                        } else {
                            Map map = new HashMap();
                            map.put("idCardNo", aa[2]);
                            map.put("bankCardNo", aa[3]);
                            map.put("projectId", projectDetailEntity.getId());
                            PersonEntity personEntity = personMapper._getPerson(map);
                            personEntity.setStatus("6");
                            personEntity.setFailReason(aa[5]);
                            projectDetailEntity.setState(2);
                            personService.update(personEntity);
                        }
                    }
                    projectDetailEntity.setPaymentAmount(bigDecimal);
                    projectDetailEntity.setSurplusAmount(projectDetailEntity.getTotalAmount().subtract(bigDecimal));
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("dataCheckAll {}", dataCheckAll);
        return dataCheckAll;
    }


    //多笔校验
    @SuppressWarnings("rawtypes")
    //@RequestMapping(value = "/checkAllInfo", method = RequestMethod.GET)
    public RestResult checkAllInfo1(HttpServletRequest request) {
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
                String sb = SocketServer.SoketPull("202.99.212.79", 8212, dataCheckAll);
                String s = decryptEcb(hexKey.toString(), sb);
                Service service = (Service) xmlToBean(Service.class, s.substring(76, s.length()));
                System.out.println(service.getHead().getID());
                if (service != null && service.getHead().getCallRes() != null && service.getHead().getID() != null) {
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

    //3.5	BT006
    @RequestMapping(value = "/checkAllInfo", method = RequestMethod.POST)
    public String checkAllInfo(HttpServletRequest request) throws Exception {
        logger.info("进入校验回调");
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        String dataCheckAll = "";
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
        logger.info("进入校验回调返回的报文 {}", result1);
        Service service = (Service) xmlToBean(Service.class, result1.substring(76));
        Head head = service.getHead();
        System.out.println(service.getHead().getID());
        dataCheckAll = getCheckPullInfo(request, service.getHead().getUUID());
        String fileName = path + "140725" + "To" + BankNo;
        String fileName1 = fileName + File.separator + CallUser + "_" + 140725 + "To" + BankNo + service.getHead().getUUID() + ".txt";

        File file = new File(fileName1);
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
        logger.info("dataCheckAll {}", dataCheckAll);
        return dataCheckAll;
    }


    //http:47.93.246.103:7011/ky-ykt/bankFile/notifyPullAll
    //202.99.212.79
    //8212/8168
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
            Service service = (Service) xmlToBean(Service.class, result1.substring(76, result1.length()));
            Head head = service.getHead();
            System.out.println(service.getHead().getID());
            if (head.getID().equals(notifycheckId)) {
                dataCheckAll = getCheckAllInfo(service.getHead().getUUID());
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
            } else if (head.getID().equals(notifypullId)) {
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

    public String getCheckAllInfo(String id) throws Exception {
        Head head = new Head();
        head.setID(notifycheckId);
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
        String d = encryptEcb(hexKey, s1, "UTF-8");
        String b = this.getByteStream(d);
        String c = b + notifycheckId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + d;
        System.out.println(c);
        return c;
    }

    public String getCheckPullInfo(HttpServletRequest request, String id) throws Exception {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
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
                    String county = personEntity.getTown();
                    String village = personEntity.getVillage();
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
                new String[]{"姓名", "身份证号", "金额", "卡号", "发放结果", "处理结果", "乡镇", "村名"});
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

    public void updatePerson(List<PersonEntity> personEntityList, SysUserEntity user, String s) {
        Map map = new HashMap<>();
        for (PersonEntity personEntity : personEntityList) {
            map.put("projectId", personEntity.getProjectId());
            map.put("idCardNo", personEntity.getIdCardNo());
            map.put("departmentId", user.getDepartmentId());
            PersonEntity personEntity1 = personMapper._queryAll(map).get(0);
            map.put("personId", personEntity1.getId());
            PersonReplacementEntity personReplacementEntity = personReplacementMapper.queryPersonId(map);
            if (personReplacementEntity == null) {
                PersonReplacementEntity pentity = personReplacementMapper.queryPersonIdtwo(map);
                if (s.contains("成功")) {
                    personEntity1.setStatus("1");
                    personEntity1.setFailReason(" ");
                    pentity.setStatus("1");
                } else if (s.contains("失败")) {
                    personEntity1.setStatus("2");
                    personEntity1.setFailReason(personEntity.getFailReason());
                    pentity.setStatus("2");
                }
                personMapper._updateEntity(personEntity1);
                personReplacementMapper._updateEntity(pentity);
            } else {
                if (s.contains("成功")) {
                    personEntity1.setStatus("1");
                    personEntity1.setFailReason(" ");
                    personReplacementEntity.setStatus("1");
                } else if (s.contains("失败")) {
                    personEntity1.setStatus("2");
                    personEntity1.setFailReason(personEntity.getFailReason());
                    personReplacementEntity.setStatus("2");
                }
                personMapper._updateEntity(personEntity1);
                personReplacementMapper._updateEntity(personReplacementEntity);
            }
        }
    }
}
