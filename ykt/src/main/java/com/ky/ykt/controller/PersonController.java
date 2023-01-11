package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.*;
import com.ky.ykt.entity.xml.*;
import com.ky.ykt.excle.ExcelHead;
import com.ky.ykt.excle.ExcelStyle;
import com.ky.ykt.excle.ExcelUtils;
import com.ky.ykt.excle.ExportExcel;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mapper.*;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.PersonService;
import com.ky.ykt.service.PersonUploadService;
import com.ky.ykt.utils.DateUtil;
import com.ky.ykt.utils.HttpUtils;
import com.ky.ykt.utils.xmlUtilToBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.ky.ykt.utils.ForFile.writeFileContent;
import static com.ky.ykt.utils.P_Sm4Util.decryptEcb;
import static com.ky.ykt.utils.P_Sm4Util.encryptEcb;
import static com.ky.ykt.utils.xmlUtilToBean.convertToXmlService;

/**
 *
 */
@RestController
@RequestMapping("/ky-ykt/person")
public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonService personService;
    @Autowired
    PersonMapper personMapper;
    @Autowired
    ProjectDetailMapper projectDetailMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    PersonUploadMapper personUploadMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    PersonUploadService personUploadService;
    @Autowired
    AreasMapper areasMapper;
    @Autowired
    PersonReplacementMapper personReplacementMapper;
    @Autowired
    UserProjectTypeMapper userProjectTypeMapper;
    @Autowired
    ProjectTypeMapper projectTypeMapper;

    private static String CallUser = "350355000001";
    private static String checkId = "YH008";
    private static String checkAllId = "YH010";
    private static String BankNo = "402161002352";
    private static String BankDep = "601103";
    @Value("${hexKey}")
    private String hexKey;
    //生成文件路径
    private static String path = "D:\\file\\";

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET)
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params = setDepartmentIdForMap(request, params);
        logger.info("The PersonController queryByParams method params are {}", params);
        return personService.queryAll(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController queryByParams method params are {}", params);
        return personService.queryByAll(params);
    }

    @RequestMapping(value = "getSessionRoleCode", method = RequestMethod.GET)
    public Object getSessionRoleCode(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController getSessionRoleCode method params are {}", params);
        Object roleCode = request.getSession().getAttribute("roleCode");
        return roleCode;
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "人员管理新增，修改操作", module = "人员管理")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(@RequestBody String body, HttpServletRequest request) throws Exception {
        logger.info("The PersonController saveOrUpdate method params are {}", body);
        PersonEntity personEntity = JSONObject.parseObject(body, PersonEntity.class);
        PersonEntity personEntity1 = new PersonEntity();
        personEntity1.setName(personEntity.getName());
        personEntity1.setBankCardNo(personEntity.getBankCardNo());
        personEntity1.setIdCardNo(personEntity.getIdCardNo());
        String s = this.getDataCheckOne(personEntity1);
        ServiceCheckOne service1 = (ServiceCheckOne) xmlUtilToBean.xmlToBean(ServiceCheckOne.class, s);
        if (service1 != null && service1.getHead().getCallRes() != null && service1.getHead().getID() != null) {
            if (!service1.getBody().getResult().equals("1")) {
                personEntity.setFailReason(service1.getBody().getErrorMsg());
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, personEntity.getName() + service1.getBody().getErrorMsg());
            } else {
                if (personEntity.getId() != null && personEntity.getId().length() > 0) {
                    //ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(personEntity.getProjectId());
                    //ProjectEntity projectEntity = projectMapper._get(projectDetailEntity.getProjectId());
                    PersonUploadEntity personUploadEntity = personUploadMapper.querypersonId(personEntity.getId());
                    if (personUploadEntity != null) {
                        //删除默认人员信息
                        personUploadMapper.deleteIdcardNo(personUploadEntity.getIdCardNo());
                        personUploadEntity.setPhone(personEntity.getPhone());
                        personUploadEntity.setName(personEntity.getName());
                        personUploadEntity.setCounty(personEntity.getCounty());
                        personUploadEntity.setTown(personEntity.getTown());
                        personUploadEntity.setVillage(personEntity.getVillage());
                        personUploadEntity.setAddress(personEntity.getAddress());
                        personUploadEntity.setOpeningBank(personEntity.getOpeningBank());
                        personUploadEntity.setBankCardNo(personEntity.getBankCardNo());
                        personUploadEntity.setGrantAmount(personEntity.getGrantAmount());
                        personUploadEntity.setIdCardNo(personEntity.getIdCardNo());
                        personUploadEntity.setProjectId(personEntity.getProjectId());
                        personUploadMapper._updateEntity(personUploadEntity);
                        //新增正确默认人员信息
                        personUploadEntity.setId(UUID.randomUUID().toString());
                        personUploadEntity.setProjectId("");
                        personUploadEntity.setProjectType("0");
                        personUploadEntity.setPersonId(" ");
                        personUploadMapper._addEntity(personUploadEntity);
                    }
                    return personService.update(personEntity);


                } else {
                    SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
                    //单个添加
                    personEntity.setId(UUID.randomUUID().toString());
                    personEntity.setStatus("3");
                    //personEntity.setProjectId(user.getDepartmentId());
                    //获取当前操作人信息
                    personEntity.setDepartmentId(user.getDepartmentId());
                    personEntity.setUserId(user.getId());
                    personEntity.setIssuingUnit(user.getDepartmentId());
                    return personService.add(personEntity);
                }
            }
        }
        return new RestResult();
    }

    /**
     * 逻辑删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "人员管理逻辑删除操作", module = "人员管理")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController delete method params is {}", params);
        PersonUploadEntity personUploadEntity = personUploadMapper._queryPersonId(params.get("id").toString());
        String personUploadEntityId = personUploadEntity.getId();
        personUploadService.delete(personUploadEntityId);
        return personService.delete(params.get("id").toString());
    }

    /**
     * 物理删除
     */
    @Log(description = "人员管理物理删除操作", module = "人员管理")
    @RequestMapping(value = "deleteForce", method = RequestMethod.GET)
    public Object deleteForce(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                personService._deleteForce(split[i]);
            }
        } else {
            personService._deleteForce(params.get("id").toString());
        }
        return new RestResult();
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        logger.info("The PersonController queryPage method params are {}", params);
        params = setDepartmentIdForMap(request, params);
        /*
        List<ProjectDetailEntity> projectDetailEntities = projectDetailMapper._queryPage(params);
        if (projectDetailEntities.size() > 0) {
            params.put("projectId", projectDetailEntities.get(0).getId());
        }
        */
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if (user.getRoleId().equals("426f5a25-c237-472c-975f-9a08e93622c7")) {
            List<DepartmentEntity> departmentEntities = departmentMapper.queryByParentId(user.getDepartmentId());
            List<String> departmentIdList = new ArrayList<String>();
            if (departmentEntities != null && departmentEntities.size() > 0) {
                for (DepartmentEntity departmentEntity : departmentEntities
                ) {
                    departmentIdList.add(departmentEntity.getId());
                }
                departmentIdList.add(user.getDepartmentId());
                params.put("departmentIdList", departmentIdList);
                params.put("departmentIdListFlag", "departmentIdListFlag");
                params.put("userProjectType", "userProjectType");
            }
            if (params.get("status") != null) {
                if (params.get("status").equals("2")) {
                    DepartmentEntity departmentEntity = departmentMapper._get(user.getDepartmentId());
                    AreasEntity areasEntity = areasMapper._get(departmentEntity.getAreaId());
                    params.put("level", areasEntity.getLevel());
                    params.put("areaId", departmentEntity.getAreaId());
                    params.put("userId", user.getId());
                }
            }
        } else if (user.getRoleId().equals("c4d895ca-9dd7-4c58-b686-d078d65422ac")) {
            params.put("issuingUnit", user.getDepartmentId());
            if (params.get("status") != null) {
                if (params.get("status").equals("2")) {
                    List<ProjectTypeEntity> projectTypeEntities = departmentMapper.queryProjectType(user.getDepartmentId());
                    List<String> projectTypeList = new ArrayList<String>();
                    if (projectTypeEntities != null && projectTypeEntities.size() > 0) {
                        for (int i = 0; i < projectTypeEntities.size(); i++) {
                            ProjectTypeEntity projectTypeEntity = projectTypeEntities.get(i);
                            List<ProjectEntity> projectEntities = projectMapper.queryProjectType(projectTypeEntity.getId());
                            if (projectEntities != null && projectEntities.size() > 0) {
                                for (int j = 0; j < projectEntities.size(); j++) {
                                    ProjectEntity projectEntity = projectEntities.get(j);
                                    projectTypeList.add(projectEntity.getId());
                                }
                            } else {
                                params.put("statusTwo", "");
                            }
                        }
                        params.put("statusTwo", projectTypeList);
                    } else {
                        params.put("statusTwo", "");

                    }
                    params.put("userId", user.getId());
                    List<String> stringss = userProjectTypeMapper.queryByprojectTypeId(user.getId());
                    List<String> strings = new ArrayList<>();
                    for (String o : stringss) {
                        List<ProjectEntity> projectEntities = projectMapper.queryProjectType(o);
                        for (ProjectEntity projectEntity : projectEntities) {
                            strings.add(projectEntity.getId());
                        }

                    }
                    params.put("stringList", strings);
                }
            }

        }
        return personService.queryPage(params);
        //return personUploadService.queryPage(params);
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/reloadPull", method = RequestMethod.GET)
    public Object reloadPull(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        return personService.reloadPull(params);
    }

    @Log(description = "银行回执提交业务部门修复数据", module = "银行管理")
    @RequestMapping(value = "/submitToBuss", method = RequestMethod.POST)
    public Object submitToBuss() {
        Map map = new HashMap();
        map.put("status", "2");
        List<PersonEntity> personEntities = personMapper._queryAll(map);
        for (PersonEntity personEntity :
                personEntities) {
            personService.submitToBuss(personEntity.getId());
        }
        return new RestResult();
    }

    @Log(description = "人员管理再次提交审核操作", module = "人员管理")
    @RequestMapping(value = "/doSubmitAudit", method = RequestMethod.GET)
    @Transactional
    public Object doSubmitAudit(HttpServletRequest request) throws Exception {
        Map params = HttpUtils.getParams(request);
        params.put("status", "2");
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        params.put("userId", user.getId());
        String projectTypeId = request.getParameter("projectTypeId");
        params.put("projectTypeId", projectTypeId);
        if (user.getRoleId().equals("426f5a25-c237-472c-975f-9a08e93622c7")) {
            List<DepartmentEntity> departmentEntities = departmentMapper.queryByParentId(user.getDepartmentId());
            List<String> departmentIdList = new ArrayList<String>();
            if (departmentEntities != null && departmentEntities.size() > 0) {
                for (DepartmentEntity departmentEntity : departmentEntities
                ) {
                    departmentIdList.add(departmentEntity.getId());
                }
                departmentIdList.add(user.getDepartmentId());
                params.put("departmentIdList", departmentIdList);
                params.put("departmentIdListFlag", "departmentIdListFlag");
                params.put("userProjectType", "userProjectType");
            }
            if (params.get("status") != null) {
                if (params.get("status").equals("2")) {
                    DepartmentEntity departmentEntity = departmentMapper._get(user.getDepartmentId());
                    AreasEntity areasEntity = areasMapper._get(departmentEntity.getAreaId());
                    params.put("level", areasEntity.getLevel());
                    params.put("areaId", departmentEntity.getAreaId());
                    params.put("userId", user.getId());
                }
            }
        } else if (user.getRoleId().equals("c4d895ca-9dd7-4c58-b686-d078d65422ac")) {
            params.put("issuingUnit", user.getDepartmentId());
            if (params.get("status") != null) {
                if (params.get("status").equals("2")) {
                    ProjectTypeEntity projectTypeEntity = departmentMapper.queryProjectTypeOne(user.getDepartmentId(), projectTypeId);
                    List<String> projectTypeList = new ArrayList<String>();
                    if (projectTypeEntity != null) {
                        List<ProjectEntity> projectEntities = projectMapper.queryProjectType(projectTypeEntity.getId());
                        if (projectEntities != null && projectEntities.size() > 0) {
                            for (int j = 0; j < projectEntities.size(); j++) {
                                ProjectEntity projectEntity = projectEntities.get(j);
                                projectTypeList.add(projectEntity.getId());
                            }
                        } else {
                            params.put("statusTwo", "");
                        }
                        params.put("statusTwo", projectTypeList);
                    } else {
                        params.put("statusTwo", "");
                    }
                    params.put("userId", user.getId());
                    List<String> stringss = userProjectTypeMapper.queryByprojectTypeId(user.getId());
                    List<String> strings = new ArrayList<>();
                    for (String o : stringss) {
                        List<ProjectEntity> projectEntities = projectMapper.queryProjectType(o);
                        for (ProjectEntity projectEntity : projectEntities) {
                            strings.add(projectEntity.getId());
                        }

                    }
                    params.put("stringList", strings);
                }
            }
        }
        List<ProjectTypeEntity> projectTypeEntities = projectTypeMapper.queryByProjectTypeId(projectTypeId);
        List<PersonEntity> personEntities1 = personMapper._queryAll(params);
        BigDecimal amount = new BigDecimal("0");
        Map<String, BigDecimal> map = new HashMap<>();
        List<Map<String, BigDecimal>> mapArrayList = new ArrayList<Map<String, BigDecimal>>();
        HashMap<String, BigDecimal> map1 = new HashMap<>();
        if (!projectTypeEntities.isEmpty() && projectTypeEntities != null) {
            for (PersonEntity personEntity : personEntities1) {
                map.put(personEntity.getProjectId(), BigDecimal.ZERO);
            }
            mapArrayList.add(map);
        }
        if (!projectTypeEntities.isEmpty() && projectTypeEntities != null) {
            for (PersonEntity personEntity : personEntities1) {
                //对人员信息校验
                //发送单个校验

                String s = this.getDataCheckOne(personEntity);
                logger.info("人员信息 {}", s);
                if (StringUtils.isNotEmpty(s)) {
                    ServiceCheckOne service1 = (ServiceCheckOne) xmlUtilToBean.xmlToBean(ServiceCheckOne.class, s);
                    if (service1 != null && service1.getHead().getCallRes() != null && service1.getHead().getID() != null) {
                        if (!service1.getBody().getResult().equals("1")) {
                            personEntity.setFailReason(service1.getBody().getErrorMsg());
                            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, personEntity.getName() + "人员信息有误，请及时修改");
                        }

                        //amount = amount.add(new BigDecimal(personEntity.getGrantAmount()).setScale(2, BigDecimal.ROUND_HALF_UP));

                        personService.doSubmitAudit(personEntity.getProjectId() + "sy", personEntity.getId());
                        params.put("personId", personEntity.getId());
                        params.put("projectId", personEntity.getProjectId());
                        PersonReplacementEntity personReplacementEntity = personReplacementMapper.queryPersonIdtwo(params);
                        personReplacementEntity.setProjectId(personEntity.getProjectId() + "sy");
                        personReplacementEntity.setStatus("4");
                        personReplacementMapper._updateEntity(personReplacementEntity);
                        if (mapArrayList.isEmpty() && mapArrayList.size() == 0) {
                            map1.put(personEntity.getProjectId(), new BigDecimal(personEntity.getGrantAmount()));
                            mapArrayList.add(map1);
                        } else {
                            for (Map<String, BigDecimal> map2 : mapArrayList) {
                                for (Map.Entry<String, BigDecimal> entry : map2.entrySet()) {
                                    if (personEntity.getProjectId().equals(entry.getKey())) {
                                        map2.put(personEntity.getProjectId(), entry.getValue().add(new BigDecimal(personEntity.getGrantAmount())));
                                    }
                                }
                            }
                        }
                    } else {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "人员信息有误，请重新发送");
                    }
                } else {
                    return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "服务器异常，请稍后重试");
                }
            }
        }

        logger.info("The PersonController doSubmitAudit success");
        for (Map<String, BigDecimal> map3 : mapArrayList) {
            for (Map.Entry<String, BigDecimal> entry : map3.entrySet()) {
                System.out.println("key=" + entry.getKey() + "  value=" + entry.getValue());
                ProjectDetailEntity projectDetailEntity1 = projectDetailMapper._get(entry.getKey());
                //projectDetailEntity1.setPaymentAmount(entry.getValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                //projectDetailMapper._updateEntity(projectDetailEntity1);
                logger.info("update parent project PaymentAmount success {}", entry.getValue().setScale(2, BigDecimal.ROUND_HALF_UP));
                ProjectDetailEntity projectDetailEntity = new ProjectDetailEntity();
                projectDetailEntity.setId(entry.getKey() + "sy");
                projectDetailEntity.setParentId(projectDetailEntity1.getId());
                projectDetailEntity.setTotalAmount(entry.getValue());
                projectDetailEntity.setPaymentAmount(entry.getValue());
                projectDetailEntity.setProjectId(projectDetailEntity1.getProjectId());
                projectDetailEntity.setProjectName(projectDetailEntity1.getProjectName());
                projectDetailEntity.setStartTime(new Date());
                projectDetailEntity.setOperUser(user.getId());
                projectDetailEntity.setOperDepartment(user.getDepartmentId());
                projectDetailEntity.setPaymentDepartment(user.getDepartmentId());
                projectDetailEntity.setSurplusAmount(BigDecimal.ZERO);
                projectDetailEntity.setState(0);
                projectDetailMapper._addEntity(projectDetailEntity);
                logger.info("Create new projectDetailEntity success {}", JSON.toJSONString(projectDetailEntity));
            }
        }


        return new RestResult();
    }

    @Log(description = "人员管理审核操作", module = "人员管理")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public Object audit(@RequestBody String body) {
        logger.info("The PersonController audit method params are {}", body);
        Map params = (Map) JSON.parse(body);
        return personService.audit(params);
    }

    @Log(description = "人员管理推送银行进行发放资金操作", module = "人员管理")
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public Object push(@RequestBody String id) {
        logger.info("The push push method params are {}", id);
        if (id.contains(",")) {
            if (id.contains(",")) {
                String[] split = id.split(",");
                for (int i = 0; i < split.length; i++) {
                    personService.push(split[i]);
                }
            } else {
                personService.push(id);
            }
        }
        return null;
    }


    @RequestMapping(value = "/export", method = RequestMethod.GET)
    protected void export(HttpServletRequest request, HttpServletResponse response) {
        Map params = HttpUtils.getParams(request);
        Map map = this.fieldExport(params);
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


    public Map fieldExport(Map params) {
        Map resultMap = new HashMap();
        ExcelStyle style = new ExcelStyle();
        List<String[]> data = new ArrayList();
        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(params.get("id").toString());
        ProjectEntity projectEntity = projectMapper._get(projectDetailEntity.getProjectId());
        Map map = new HashMap();
        map.put("projectId", projectDetailEntity.getId());
        List<PersonEntity> entities = personMapper._queryAll(map);
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
        String tStamp = dfs.format(new Date());
        style.setColumnWidth(25);
        style.setSheetName("导出");
        style.setXlsName("发放人员信息表_" + tStamp);
        for (PersonEntity entity : entities) {
            data.add(new String[]{
                    entity.getName(),
                    entity.getPhone(),
                    entity.getIdCardNo(),
                    entity.getBankCardNo(),
                    projectEntity.getProjectName(),
                    entity.getGrantAmount(),
                    entity.getCounty(),
                    entity.getTown(),
                    entity.getAddress(),
                    projectDetailEntity.getId()
            });
        }
        resultMap.put("header",
                new String[]{"姓名", "手机号", "身份证号", "银行卡号", "项目资金名称", "发放金额", "所属区县", "所属乡镇", "详细地址", "流水号", "回执状态"});
        resultMap.put("data", data);
        resultMap.put("style", style);
        return resultMap;


    }

    //@Log(description = "发放人员信息表导入操作", module = "人员管理")
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @Transactional
    public RestResult importExcel(@RequestParam MultipartFile file, HttpServletRequest request) {
        logger.info("The file is {}", file);
        String projectId = request.getParameter("projectId");
        if (file == null || file.getName().equals("") || file.getSize() <= 0) {
            return new RestResult(40000, RestResult.ERROR_MSG, "文件不合法,请检查文件是否为Excel文件");
        }
        if (projectId == null || projectId.equals("")) {
            return new RestResult(40000, RestResult.ERROR_MSG, "没有选择补贴项目资金，请重新选择");
        }
        String fileName = file.getOriginalFilename();
        try {
            ExcelUtils.checkFile(fileName);
        } catch (Exception e) {
            return new RestResult(40000, RestResult.ERROR_MSG, "文件不合法,请检查文件是否为Excel文件");
        }
        //String filePath = request.getSession().getServletContext().getRealPath("upload/");
        String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        //对文件名进行修改
        String[] split = fileName.split("\\.");
        fileName = UUID.randomUUID().toString() + "." + split[1];
        String path = filePath + fileName;
        File uploadFile = new File(path);
        List<ExcelHead> headList = personMapper._queryColumnAndComment();
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        List<PersonEntity> personEntityList = new ArrayList<>();
        //stringError
        StringBuffer stringBufferError = new StringBuffer();
        try {
            file.transferTo(uploadFile);
            InputStream inputStream = new FileInputStream(uploadFile);
            List<PersonEntity> personEntities = ExcelUtils.readExcelToEntity(PersonEntity.class, inputStream, uploadFile.getName(), headList);

            //校验录入的表的字段是否合格
            //EXCAL表身份证号校验
            //String idCardNoRegex = "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)|(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)";
            String idCardNoRegex = "(^\\w{11}|\\w{15}|\\w{18}$)";
            //EXCAL手机号校验
            //String phoneRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
            int i = 1;
            for (PersonEntity personEntity : personEntities) {
                i++;
                if (StringUtils.isEmpty(personEntity.getName())
                        || StringUtils.isEmpty(personEntity.getGrantAmount())
                        || StringUtils.isEmpty(personEntity.getCounty())
                        || StringUtils.isEmpty(personEntity.getAddress())
                        || StringUtils.isEmpty(personEntity.getCounty())
                        || StringUtils.isEmpty(personEntity.getTown())
                        || StringUtils.isEmpty(personEntity.getVillage())
                        || StringUtils.isEmpty(personEntity.getBankCardNo())
                        || StringUtils.isEmpty(personEntity.getOpeningBank())
                        || StringUtils.isEmpty(personEntity.getIdCardNo())) {
                    stringBufferError.append("第" + i + "行，" + personEntity.getName() + "有数据为空，请检查后录取<br>");
                }
                boolean idCardMatches = personEntity.getIdCardNo().matches(idCardNoRegex);
                if (personEntity.getIdCardNo() == null
                        || personEntity.getIdCardNo() == ""
                        || idCardMatches == false) {
                    stringBufferError.append("第" + i + "行，" + personEntity.getName() + "的身份证号有误，请重新录入<br>");
                }
                // 身份账号+银行卡号+发放部门+资金项目 需要唯一
                PersonEntity personEntity2 =
                        personMapper.queryByIdCardNoProject(personEntity.getIdCardNo(), projectId);
                if (personEntity2 != null) {
                    stringBufferError.append("第" + i + "行，" + personEntity.getName() + "信息已经录过，请检查后再重新录入<br>");
                }
                // 本次录入检查唯一
                if (personEntityList != null && personEntityList.size() > 0) {
                    for (int j = 0; j < personEntityList.size(); j++) {
                        PersonEntity personEntity1 = personEntityList.get(j);
                        if (personEntity1.getIdCardNo().equals(personEntity.getIdCardNo())) {
                            stringBufferError.append("第" + i + "行，" + personEntity.getName() + "的身份证号已录过，请检查后再重新录入<br>");
                        }
                        if (personEntity1.getBankCardNo().equals(personEntity.getBankCardNo())) {
                            stringBufferError.append("第" + i + "行，" + personEntity.getName() + "的银行卡号已录过，请检查后再重新录入<br>");
                        }
                    }
                }
                String personId = UUID.randomUUID().toString();

                List<AreasEntity> townEntities = new ArrayList<>();
                List<AreasEntity> villageEntities = new ArrayList<>();

                AreasEntity countyEntity = areasMapper.queryByIdByName(personEntity.getCounty(), 2);
                if (countyEntity == null) {
                    stringBufferError.append("第" + i + "行，" + personEntity.getName() + "所属区县在系统不存在<br>");
                } else {
                    townEntities = areasMapper.queryByAreasPid(personEntity.getTown(), countyEntity.getId());
                    if (townEntities == null || townEntities.size() == 0) {
                        stringBufferError.append("第" + i + "行，" + personEntity.getName() + "所属乡镇在系统不存在<br>");
                    } else {
                        for (AreasEntity areasEntity :
                                townEntities) {
                            villageEntities.addAll(areasMapper.queryByAreasPid(personEntity.getVillage(), areasEntity.getId()));
                        }
                        if (villageEntities == null || villageEntities.size() == 0) {
                            stringBufferError.append("第" + i + "行，" + personEntity.getName() + "所属村组在系统不存在<br>");
                        } else {
                            //乡镇
                            List<AreasEntity> collect = villageEntities.stream()
                                    .filter(AreasEntity -> AreasEntity.getName().equals(personEntity.getVillage()))
                                    .collect(Collectors.toList());
                        }
                    }
                }
                //发送单个校验

                String s = this.getDataCheckOne(personEntity);
                logger.info("人员信息 {}", s);
                if (StringUtils.isNotEmpty(s)) {
                    ServiceCheckOne service1 = (ServiceCheckOne) xmlUtilToBean.xmlToBean(ServiceCheckOne.class, s);
                    if (service1 != null && service1.getHead().getCallRes() != null && service1.getHead().getID() != null) {
                        if (!service1.getBody().getResult().equals("1")) {
                            personEntity.setFailReason(service1.getBody().getErrorMsg());
                            stringBufferError.append("该表中第" + i + "行" + service1.getBody().getName() + "的" + service1.getBody().getErrorMsg() + "</br>");
                        }
                        // 查询人员档案
                        List<PersonUploadEntity> personUploadEntityList =
                                personUploadMapper.queryByIdCardNo(personEntity.getIdCardNo());
                        if (personUploadEntityList.isEmpty() || personUploadEntityList.size() == 0) {
                            ProjectEntity projectEntity = projectMapper._get(projectId);
                            personEntity.setCounty(countyEntity.getId());
                            personEntity.setTown(townEntities.get(0).getId());
                            personEntity.setVillage(villageEntities.get(0).getId());
                            personEntity.setAddress(personEntity.getAddress());
                            personEntity.setPhone(personEntity.getPhone());
                            personEntity.setOpeningBank(personEntity.getOpeningBank());
                            personEntity.setBankCardNo(personEntity.getBankCardNo());

                            personEntity.setId(personId);
                            personEntity.setItemId(projectId);
                            personEntity.setStatus("3"); // 新增状态是未提交 3
                            personEntity.setDepartmentId(user.getDepartmentId());
                            personEntity.setUserId(user.getId());
                            personEntity.setIssuingUnit(projectEntity.getPaymentDepartment());
                            // personMapper._addEntity(personEntity);
                            // 去除名字之间的空格
                            personEntity.setName(personEntity.getName().replaceAll(" ", ""));
                            personEntityList.add(personEntity);
                        } else if (personUploadEntityList.size() == 1) {
                            if (!personEntity.getIdCardNo().equals(personUploadEntityList.get(0).getIdCardNo()) || !personEntity.getName().equals(personUploadEntityList.get(0).getName())
                                    || !personEntity.getBankCardNo().equals(personUploadEntityList.get(0).getBankCardNo())) {
                                stringBufferError.append("第" + i + "行的信息中,姓名/身份证/银行卡与档案中数据不匹配<br>");
                            }
                            ProjectEntity projectEntity = projectMapper._get(projectId);
                            personEntity.setCounty(personUploadEntityList.get(0).getCounty());
                            personEntity.setTown(personUploadEntityList.get(0).getTown());
                            personEntity.setVillage(personUploadEntityList.get(0).getVillage());
                            personEntity.setAddress(personEntity.getAddress());
                            personEntity.setPhone(personEntity.getPhone());
                            personEntity.setOpeningBank(personEntity.getOpeningBank());
                            personEntity.setBankCardNo(personEntity.getBankCardNo());
                            personEntity.setId(personId);
                            personEntity.setItemId(projectId);
                            personEntity.setStatus("3"); // 新增状态是未提交 3
                            personEntity.setDepartmentId(user.getDepartmentId());
                            personEntity.setUserId(user.getId());
                            personEntity.setIssuingUnit(projectEntity.getPaymentDepartment());
                            // 去除名字之间的空格
                            personEntity.setName(personEntity.getName().replaceAll(" ", ""));
                            personEntityList.add(personEntity);
                        } else {
                            stringBufferError.append("第" + i + "行的信息中,姓名/身份证/银行卡与档案中数据不匹配<br>");
                        }
                    } else {
                        stringBufferError.append("第" + i + "行的信息中,人员信息有误<br>");
                    }
                } else {
                    stringBufferError.append("与省联交互出现问题，请联系管理员进行处理<br>");
                }
            }
            logger.info("execute success {}", personEntities.size());
        } catch (Exception e) {
            logger.error("{}", e);
            personEntityList.clear();
            uploadFile.delete();
            if (StringUtils.isEmpty(stringBufferError)) {
                return new RestResult(
                        RestResult.ERROR_CODE,
                        RestResult.ERROR_MSG,
                        "上传文件有误，请修改后在上传");
            } else {
                return new RestResult(
                        RestResult.ERROR_CODE,
                        RestResult.ERROR_MSG,
                        stringBufferError);
            }

        } finally {
            uploadFile.delete();
        }
        if (StringUtils.isEmpty(stringBufferError)) {
            if (personEntityList != null && personEntityList.size() > 0) {
                for (PersonEntity personEntity : personEntityList
                ) {
                    personMapper._addEntity(personEntity);
                }
            }
        } else {
            return new RestResult(
                    RestResult.ERROR_CODE,
                    RestResult.ERROR_MSG,
                    stringBufferError);
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "上传成功");
    }

    /**
     * 银行回执
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping(value = "/compareExcel", method = RequestMethod.POST)
    @Transactional
    public RestResult compareExcel(@RequestParam MultipartFile file, HttpServletRequest request) {
        logger.info("The file is {}", file);
        if (file == null) {
            return new RestResult(40000, RestResult.ERROR_MSG, "文件不合法,请检查文件是否为Excel文件");
        }
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        String fileName = file.getOriginalFilename();
        //String filePath = request.getSession().getServletContext().getRealPath("upload/");
        String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

        //对文件名进行修改
        String[] split = fileName.split("\\.");
        fileName = UUID.randomUUID().toString() + "." + split[1];
        String path = filePath + fileName;

        File uploadFile = new File(path);
        List<ExcelHead> headList = personMapper._queryColumnAndComment();
        try {
            file.transferTo(uploadFile);
            InputStream inputStream = new FileInputStream(uploadFile);
            List<PersonEntity> personEntities = ExcelUtils.readExcelToEntity(PersonEntity.class, inputStream, uploadFile.getName(), headList);
            for (PersonEntity personEntity : personEntities) {
                if (personEntity.getName() != null || personEntity.getBankCardNo() != null
                        || personEntity.getIdCardNo() != null
                        || personEntity.getGrantAmount() != null) {

                    if (StringUtils.isEmpty(personEntity.getProjectId()) || StringUtils.isEmpty(personEntity.getName()) || StringUtils.isEmpty(personEntity.getBankCardNo()) || StringUtils.isEmpty(personEntity.getGrantAmount())
                            || StringUtils.isEmpty(personEntity.getIdCardNo())) {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "流水号/姓名/银行卡号/身份证号/发放金额/回执状态均不能为空");
                    }
                    Map map = new HashMap();
                    map.put("projectId", personEntity.getProjectId());
                    map.put("idCardNo", personEntity.getIdCardNo());
                    map.put("departmentId", user.getDepartmentId());
                    PersonEntity personEntity1 = personMapper._queryAll(map).get(0);
                    if (personEntity1 != null) {
                        map.put("personId", personEntity1.getId());
                        PersonReplacementEntity personReplacementEntity = personReplacementMapper.queryPersonId(map);
                        if (personReplacementEntity == null) {
                            PersonReplacementEntity pentity = personReplacementMapper.queryPersonIdtwo(map);
                            if (pentity != null) {
                                if (personEntity.getStatus().contains("成功")) {
                                    personEntity1.setStatus("1");
                                    personEntity1.setFailReason(" ");
                                    pentity.setStatus("1");
                                } else if (personEntity.getStatus().contains("失败")) {
                                    personEntity1.setStatus("2");
                                    personEntity1.setFailReason(personEntity.getFailReason());
                                    pentity.setStatus("2");
                                }
                                personMapper._updateEntity(personEntity1);
                                personReplacementMapper._updateEntity(pentity);
                            }
                        } else {
                            if (personEntity.getStatus().contains("成功")) {
                                personEntity1.setStatus("1");
                                personEntity1.setFailReason(" ");
                                personReplacementEntity.setStatus("1");
                            } else if (personEntity.getStatus().contains("失败")) {
                                personEntity1.setStatus("2");
                                personEntity1.setFailReason(personEntity.getFailReason());
                                personReplacementEntity.setStatus("2");
                            }
                            personMapper._updateEntity(personEntity1);
                            personReplacementMapper._updateEntity(personReplacementEntity);
                        }
                    } else {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "文件有错误，请修改后在上传");
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            uploadFile.delete();
        } finally {
            uploadFile.delete();
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "上传成功");
    }


    public static Map setDepartmentIdForMap(HttpServletRequest request, Map params) {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        params.put("departmentId", user.getDepartmentId());
        return params;

    }

    /**
     * 查询身份证
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "checkIdCard", method = RequestMethod.GET)
    public Boolean checkIdCard(String idCardNo, String projectId) {
        logger.info("The PersonController queryByParams method params are {}", idCardNo, projectId);
        PersonEntity personEntity = personMapper.queryByIdCardNo(idCardNo, projectId);
        if (personEntity != null) {
            return true;
        }
        return false;
    }


    @Log(description = "发放录入提交操作", module = "人员管理")
    @RequestMapping(value = "/doSubmit", method = RequestMethod.POST)
    @Transactional
    public Object doSubmit(HttpSession session) throws Exception {
        SysUserEntity user = (SysUserEntity) session.getAttribute("user");
        HashMap params = new HashMap();
        Object roleCodeSession = session.getAttribute("roleCode");
        String roleCode = "";
        if (roleCodeSession != null) {
            roleCode = roleCodeSession.toString();
        }
        if (roleCode == "4") {
            params.put("userId", user.getId());
        } else {
            if (user.getRoleId().equals("426f5a25-c237-472c-975f-9a08e93622c7")) {
                params.put("flag", "2");
                List<DepartmentEntity> departmentEntities = departmentMapper.queryByParentId(user.getDepartmentId());
                List<String> departmentIdList = new ArrayList<String>();
                if (departmentEntities != null && departmentEntities.size() > 0) {
                    for (DepartmentEntity departmentEntity : departmentEntities
                    ) {
                        departmentIdList.add(departmentEntity.getId());
                    }
                    departmentIdList.add(user.getDepartmentId());
                    params.put("departmentIdList", departmentIdList);
                    params.put("departmentIdListFlag", "departmentIdListFlag");
                }
            }
        }
        params.put("status", "3");//未提交
        String itemId = null;
        List<PersonEntity> personEntities = personMapper._queryAll(params);
        //ProjectEntity projectEntity = projectMapper._get(personEntities.get(0).getItemId());
        PersonReplacementEntity personReplacementEntity = new PersonReplacementEntity();
        String projectDetailId = UUID.randomUUID().toString();
        /*
        //批量校验
        String s = getDataCheckAll(personEntities, projectDetailId);
        String sb = SocketServer.SoketPull("202.99.212.79", 8212, s);
        System.out.println("sb=" + sb);
        String s1 = decryptEcb(hexKey, sb.substring(76));
        System.out.println("s1=" + s1);
        ServiceOne service = (ServiceOne) xmlToBean(ServiceOne.class, s1);

        if (service.getHead().getCallRes().equals("1")) {
            */

        if (personEntities.size() > 0) {
            itemId = personEntities.get(0).getItemId();
            ProjectEntity projectEntity = projectMapper._get(itemId);
            BigDecimal totalAmount = new BigDecimal("0");
            for (PersonEntity personEntity : personEntities) {
                BigDecimal bigDecimal = new BigDecimal(personEntity.getGrantAmount());
                totalAmount = totalAmount.add(bigDecimal);
                PersonUploadEntity personUploadEntity1 = personUploadMapper._queryPersonId(personEntity.getId());
                if (personUploadEntity1 != null) {
                    BeanUtils.copyProperties(personEntity, personUploadEntity1);
                    personUploadEntity1.setProjectId(projectDetailId);
                    personUploadEntity1.setProjectType(projectEntity.getProjectType());
                    personUploadMapper._updateEntity(personUploadEntity1);
                } else {
                    params.put("idCardNo", personEntity.getIdCardNo());
                    params.put("bankCardNo", personEntity.getBankCardNo());
                    PersonUploadEntity personUploadEntity2 = personUploadMapper.queryPerson(params);
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
                    personUploadEntity.setProjectId(projectDetailId);
                    personUploadEntity.setProjectType(projectEntity.getProjectType());
                    personUploadMapper._addEntity(personUploadEntity);
                }

                personReplacementEntity.setId(UUID.randomUUID().toString());
                personReplacementEntity.setPersonId(personEntity.getId());
                personReplacementEntity.setReplacementAmount(personEntity.getGrantAmount());
                personReplacementEntity.setDepartmentId(personEntity.getDepartmentId());
                personReplacementEntity.setUserId(user.getId());
                personReplacementEntity.setStatus("4");
                personReplacementEntity.setProjectId(projectDetailId);
                personReplacementMapper._addEntity(personReplacementEntity);
                personEntity.setStatus("4");//已提交
                personEntity.setProjectId(projectDetailId);
                personMapper._updateEntity(personEntity);
            }

            ProjectDetailEntity projectDetailEntity = new ProjectDetailEntity();
            projectDetailEntity.setId(projectDetailId);
            //BigDecimal totalAmount1 = projectEntity.getSurplusAmount();

            projectDetailEntity.setTotalAmount(projectEntity.getTotalAmount());
            projectDetailEntity.setPaymentAmount(projectEntity.getPaymentAmount());
            //发放剩余金额
            projectDetailEntity.setSurplusAmount(projectEntity.getSurplusAmount());
            projectDetailEntity.setProjectId(itemId);
            projectDetailEntity.setProjectName(projectEntity.getProjectName());
            projectDetailEntity.setStartTime(new Date());
            //操作人
            projectDetailEntity.setOperUser(user.getId());
            //操作单位
            projectDetailEntity.setOperDepartment(user.getDepartmentId());
            projectDetailEntity.setPaymentDepartment(projectEntity.getPaymentDepartment());
            projectDetailEntity.setState(0);
            projectDetailMapper._addEntity(projectDetailEntity);

            return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "提交成功！");
        } else {
            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "数据不能为空，请重新录入！");
        }
            /*
            ProjectDetailEntity projectDetailEntity = new ProjectDetailEntity();
            projectDetailEntity.setId(projectDetailId);
            //BigDecimal totalAmount1 = projectEntity.getSurplusAmount();

            projectDetailEntity.setTotalAmount(projectEntity.getTotalAmount());
            projectDetailEntity.setPaymentAmount(projectEntity.getPaymentAmount());
            //发放剩余金额
            projectDetailEntity.setSurplusAmount(projectEntity.getSurplusAmount());
            projectDetailEntity.setProjectId(itemId);
            projectDetailEntity.setProjectName(projectEntity.getProjectName());
            projectDetailEntity.setStartTime(new Date());
            //操作人
            projectDetailEntity.setOperUser(user.getId());
            //操作单位
            projectDetailEntity.setOperDepartment(user.getDepartmentId());
            projectDetailEntity.setPaymentDepartment(projectEntity.getPaymentDepartment());
            projectDetailEntity.setState(0);
            projectDetailMapper._addEntity(projectDetailEntity);
            return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "成功发送验证！");

        } else {
            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, service.getHead().getError());
        }
        */
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    public Object queryByPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        if (params.get("areaId") != null && StringUtils.isNotEmpty(params.get("areaId").toString())) {
            AreasEntity areasEntity = areasMapper._get(params.get("areaId").toString());
            params.put("level", areasEntity.getLevel());
        }
        logger.info("The PersonController queryByPage method params are {}", params);
        params = setDepartmentIdForMap(request, params);
        return personService.queryPage(params);
    }

    //项目去重
    public List<PersonEntity> getDistProject(List<PersonEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getProjectId().equals(list.get(j).getProjectId())) {
                    list.remove(j);
                    j--;
                }
            }
        }
        return list;
    }


    public String getDataCheckOne(PersonEntity personEntity) throws Exception {
        Head head = new Head();
        head.setID(checkId);
        head.setUUID(UUID.randomUUID().toString());
        head.setCallDate(DateUtil.getDay());
        head.setCallTime(DateUtil.getHms());
        head.setCallUser(CallUser);
        head.setDistrict("140725");
        head.setBankNo(BankNo);
        ServiceCheckOne service = new ServiceCheckOne();
        BodyCheckOne body = new BodyCheckOne();
        body.setName(personEntity.getName());
        body.setIdNo(personEntity.getIdCardNo());
        body.setBankAcct(personEntity.getBankCardNo());
        body.setBankDep(BankDep);
        body.setExtend1("");
        body.setExtend2("");
        service.setBody(body);
        service.setHead(head);
        String s1 = convertToXmlService(service, "UTF-8");
        String d = encryptEcb(hexKey, s1, "utf-8");
        String z = getByteStream(d);
        String y = z + checkId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + d;
        logger.info("报头{}", y);
        String sb = SocketServer.SoketPull("202.99.212.79", 8212, y);
        String s = decryptEcb(hexKey, sb.substring(76, sb.length()));
        System.out.println(s);
        return s;
    }

    public String getDataCheckAll(List<PersonEntity> personEntities, String projectDetailId) throws Exception {

        String fileName = path + "140725" + "To" + BankNo;
        File file = new File(fileName);
        if (!file.exists()) {
            file.mkdir();
        }
        String fileName1 = fileName + File.separator + CallUser + "_" + 140725 + "To" + BankNo + projectDetailId + ".txt";
        File file1 = new File(fileName1);
        if (!file1.exists()) {
            file1.createNewFile();
        } else {
            file1.delete();
            file1.createNewFile();
        }

        Head head = new Head();
        head.setID(checkAllId);
        head.setUUID(projectDetailId);
        head.setCallDate(DateUtil.getDay());
        head.setCallTime(DateUtil.getHms());
        head.setCallUser(CallUser);
        head.setDistrict("140725");
        head.setBankNo(BankNo);
        BodyCheckAll body = new BodyCheckAll();
        body.setRowCnt(personEntities.size());
        body.setExtend1("");
        body.setExtend2("");
        java.text.DecimalFormat format = new java.text.DecimalFormat("000000");
        int i = 0;
        String writeResult = "";
//        writeResult=personEntities.size()+"@|$"+""+"@|$"+""+"\n";
        for (PersonEntity personEntity : personEntities
        ) {
            i++;
            writeResult += format.format(i)
                    + "@|$" + personEntity.getName()
                    + "@|$" + personEntity.getIdCardNo()
                    + "@|$" + personEntity.getBankCardNo()
                    + "@|$" + ""
                    + "@|$" + ""
                    + "\n";
        }
        System.out.println("wr=" + writeResult);

        writeFileContent(fileName1, writeResult);
        ServiceCheckAll service = new ServiceCheckAll();
        service.setBody(body);
        service.setHead(head);
        String s1 = convertToXmlService(service, "UTF-8");
        String d = encryptEcb(hexKey, s1, "UTF-8");
        String b = getByteStream(d);
        String c = b + checkAllId + "        " + "        " + "        " + "        " + "        " + "        " + "        " + "        " + d;
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
