package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.*;
import com.ky.ykt.excle.ExcelHead;
import com.ky.ykt.excle.ExcelStyle;
import com.ky.ykt.excle.ExcelUtils;
import com.ky.ykt.excle.ExportExcel;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mapper.AreasMapper;
import com.ky.ykt.mapper.DepartmentMapper;
import com.ky.ykt.mapper.PersonUploadMapper;
import com.ky.ykt.mapper.ProjectDetailMapper;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.PersonService;
import com.ky.ykt.service.PersonUploadService;
import com.ky.ykt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-29 11:34
 * @version: v1.0
 */
@RestController
@RequestMapping("/ky-ykt/personUpload")
public class PersonUploadController {
    private static final Logger logger = LoggerFactory.getLogger(PersonUploadController.class);

    @Autowired
    PersonUploadMapper personUploadMapper;
    @Autowired
    PersonUploadService personUploadService;
    @Autowired
    ProjectDetailMapper projectDetailMapper;
    @Autowired
    PersonService personService;
    @Autowired
    AreasMapper areasMapper;
    @Autowired
    DepartmentMapper departmentMapper;

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryParams", method = RequestMethod.GET)
    public Object queryParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params = setDepartmentIdForMap(request, params);
        logger.info("The PersonUploadController queryByParams method params are {}", params);
        return personUploadService.queryAll(params);
    }

    @RequestMapping(value = "/personUploadExport", method = RequestMethod.GET)
    protected void personUploadExportX(HttpServletRequest request, HttpServletResponse response) {
        Map params = HttpUtils.getParams(request);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if (!user.getUserName().equals("admin")) {
            List<DepartmentEntity> departmentEntities = departmentMapper.queryByParentId(user.getDepartmentId());
            List<String> departmentIdList = new ArrayList<String>();
            if (departmentEntities != null && departmentEntities.size() > 0) {
                for (DepartmentEntity departmentEntity : departmentEntities
                ) {
                    departmentIdList.add(departmentEntity.getId());
                }

            }
            departmentIdList.add(user.getDepartmentId());
            params.put("departmentIdList", departmentIdList);
            params.put("departmentIdListFlag", "departmentIdListFlag");
        }
        if(params.get("areaId") != null && StringUtils.isNotEmpty(params.get("areaId").toString())){
            AreasEntity areasEntity = areasMapper._get(params.get("areaId").toString());
            params.put("level",areasEntity.getLevel());
        }
        Map map = this.personUploadExport(params);
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

    public Map personUploadExport(Map params) {
        Map resultMap = new HashMap();
        ExcelStyle style = new ExcelStyle();
        List<String[]> data = new ArrayList();
        List<PersonUploadEntity> entities = (List<PersonUploadEntity>) personUploadService.queryAll(params).getData();
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
        String tStamp = dfs.format(new Date());
        style.setColumnWidth(25);
        style.setSheetName("导出");
        style.setXlsName("人员信息表_" + tStamp);
        for (PersonUploadEntity entity : entities) {
            data.add(new String[]{
                    entity.getName(),
                    entity.getPhone(),
                    entity.getIdCardNo(),
                    "",
                    //entity.getCounty(),
                    entity.getCountyName(),
                    entity.getTownName(),
                    entity.getVillageName(),
                    entity.getAddress(),
                    entity.getBankCardNo(),
                    "",
                    entity.getOpeningBank()
            });
        }
        resultMap.put("header",
                new String[]{"姓名", "手机号", "身份证号", "发放金额", "所属区县", "所属乡镇", "所属村组", "详细地址", "社保卡号", "补贴类型", "开户行"});
        resultMap.put("data", data);
        resultMap.put("style", style);
        return resultMap;


    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonUploadController queryByParams method params are {}", params);
        return personUploadService.queryByAll(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "人员档案新增，修改操作", module = "人员档案")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(@RequestBody String body, HttpServletRequest request) {
        logger.info("The PersonUploadController saveOrUpdate method params are {}", body);
        PersonUploadEntity personUploadEntity = JSONObject.parseObject(body, PersonUploadEntity.class);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if (personUploadEntity.getId() != null && personUploadEntity.getId().length() > 0) {
            personUploadEntity.setDepartmentId(user.getDepartmentId());
            return personUploadService.update(personUploadEntity);
        } else {
            personUploadEntity.setDepartmentId(user.getDepartmentId());
            return personUploadService.add(personUploadEntity);
        }
    }

    /**
     * 逻辑删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "人员管理逻辑删除操作", module = "人员管理")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonUploadController delete method params is {}", params);
        return personUploadService.delete(params.get("id").toString());
    }

    /**
     * 物理删除
     */
    @Log(description = "人员管理物理删除操作", module = "人员管理")
    @RequestMapping(value = "deleteForce", method = RequestMethod.GET)
    public Object deleteForce(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonUploadController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                personUploadService._deleteForce(split[i]);
            }
        } else {
            personUploadService._deleteForce(params.get("id").toString());
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
        logger.info("The PersonUploadController queryPage method params are {}", params);
        //params = setDepartmentIdForMap(request,params);
        //List<ProjectDetailEntity> projectDetailEntities = projectDetailMapper._queryPage(params);
        /*
        List<ProjectDetailEntity> projectDetailEntities = projectDetailMapper._queryProjectId(params);
        if (projectDetailEntities.size() > 0) {
            params.put("projectId", projectDetailEntities.get(0).getId());
        }
        */
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if (user.getRoleId().equals("426f5a25-c237-472c-975f-9a08e93622c7") || user.getRoleId().equals("f1efcd40-eafd-436f-af4b-c337c4956262")) {
            List<DepartmentEntity> departmentEntities = departmentMapper.queryByParentId(user.getDepartmentId());
            List<String> departmentIdList = new ArrayList<String>();
            if (departmentEntities != null && departmentEntities.size() > 0) {
                for (DepartmentEntity departmentEntity : departmentEntities
                ) {
                    departmentIdList.add(departmentEntity.getId());
                }

            }
            departmentIdList.add(user.getDepartmentId());
            params.put("departmentIdList", departmentIdList);
            params.put("departmentIdListFlag", "departmentIdListFlag");
        }
        return personUploadService.queryPage(params);
        //return personService.queryPage(params);
    }

    @Log(description = "人员管理提交审核操作", module = "人员管理")
    @RequestMapping(value = "/doSubmitAudit", method = RequestMethod.POST)
    public Object doSubmitAudit(@RequestBody String id) {
        logger.info("The PersonUploadController doSubmitAudit method params are {}", id);
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                personUploadService.doSubmitAudit(split[i]);
            }
        } else {
            personUploadService.doSubmitAudit(id);
        }
        return new RestResult();
    }

    @Log(description = "人员管理审核操作", module = "人员管理")
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public Object audit(@RequestBody String body) {
        logger.info("The PersonUploadController audit method params are {}", body);
        Map params = (Map) JSON.parse(body);
        return personUploadService.audit(params);
    }

    @Log(description = "人员管理推送银行进行发放资金操作", module = "人员管理")
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public Object push(@RequestBody String id) {
        logger.info("The push push method params are {}", id);
        if (id.contains(",")) {
            if (id.contains(",")) {
                String[] split = id.split(",");
                for (int i = 0; i < split.length; i++) {
                    personUploadService.push(split[i]);
                }
            } else {
                personUploadService.push(id);
            }
        }
        return null;
    }

    public static Map setDepartmentIdForMap(HttpServletRequest request, Map params) {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        params.put("departmentId", user.getDepartmentId());
        return params;

    }

    @RequestMapping(value = "/import", method = RequestMethod.POST)
    @Transactional
    public RestResult importExcel(@RequestParam MultipartFile file, HttpServletRequest request) {
        logger.info("The file is {}", file);
        //String projectId = request.getParameter("projectId");
        if (file == null || file.getName().equals("") || file.getSize() <= 0) {
            return new RestResult(40000, RestResult.ERROR_MSG, "文件不合法,请检查文件是否为Excel文件");
        }

        String fileName = file.getOriginalFilename();
        try {
            ExcelUtils.checkFile(fileName);
        } catch (Exception e) {
            return new RestResult(40000, RestResult.ERROR_MSG, "文件不合法,请检查文件是否为Excel文件");
        }
        //String filePath = request.getSession().getServletContext().getRealPath("upload/");
        String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String path = filePath + fileName;
        File uploadFile = new File(path);
        List<ExcelHead> headList = personUploadMapper._queryColumnAndComment();
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        List<PersonUploadEntity> resultList = new ArrayList<>();
        try {
            file.transferTo(uploadFile);
            InputStream inputStream = new FileInputStream(uploadFile);
            List<PersonUploadEntity> personEntities = ExcelUtils.readExcelToEntity(PersonUploadEntity.class, inputStream, uploadFile.getName(), headList);

            //计算发放剩余金额
            //校验录入的表的字段是否合格
            //EXCAL表身份证号校验
            String idCardNoRegex = "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)|(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)";
            //EXCAL手机号校验
            //String phoneRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
            int i = 1;
            for (PersonUploadEntity personEntity : personEntities) {
                /*if (personEntity.getName() != null || personEntity.getBankCardNo() != null || personEntity.getAddress() != null
                        || personEntity.getCounty() != null || personEntity.getIdCardNo() != null || personEntity.getPhone() != null
                        || personEntity.getGrantAmount() != null) {*/
                if (personEntity.getName() != null || personEntity.getBankCardNo() != null || personEntity.getAddress() != null
                        || personEntity.getCounty() != null || personEntity.getIdCardNo() != null) {
                    if (StringUtils.isEmpty(personEntity.getName()) || StringUtils.isEmpty(personEntity.getBankCardNo()) || StringUtils.isEmpty(personEntity.getIdCardNo())) {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "第"+i+"行，"+personEntity.getName()+"姓名/银行卡号/身份证号均不能为空");
                    }
                    i++;
                    /*
                    if (personEntity.getGrantAmount() == null || personEntity.getGrantAmount() == "") {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "该表中第" + i + "行发放金额有误，请重新录入");
                    }
                    */
                    boolean idCardMatches = personEntity.getIdCardNo().matches(idCardNoRegex);
                    if (personEntity.getIdCardNo() == null || personEntity.getIdCardNo() == "" || idCardMatches == false) {
                        return new RestResult(40000, RestResult.ERROR_MSG, "该表中第" + i + "行,"+personEntity.getName()+"身份证号有误，请重新录入");
                    }
                    /*boolean phoneMatches = personEntity.getPhone().matches(phoneRegex);
                    if (personEntity.getIdCardNo() == null || personEntity.getIdCardNo() == "" || phoneMatches == false) {
                        return new RestResult(40000, RestResult.ERROR_MSG, "该表中第" + i + "行手机号有误，请重新录入");
                    }*/

                    //身份账号+银行卡号+发放部门+资金项目 需要唯一
                    List<PersonUploadEntity> uploadEntity = personUploadMapper.queryByIdCardNo(personEntity.getIdCardNo());
                    for (PersonUploadEntity personUploadEntity : uploadEntity) {
                        if (personEntity.getIdCardNo().equals(personUploadEntity.getIdCardNo()) && personEntity.getBankCardNo().equals(personUploadEntity.getBankCardNo())) {
                            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "第"+i+"行，"+personEntity.getName()+"已存在");
                        }
                    }
                    //本次录入检查唯一
                    if(resultList != null &&resultList.size()>0){
                        for (int j = 0; j < resultList.size(); j++) {
                            PersonUploadEntity personUploadEntity =  resultList.get(j);
                            if(personUploadEntity.getIdCardNo().equals(personEntity.getIdCardNo()) && personUploadEntity.getBankCardNo().equals(personEntity.getBankCardNo())){
                                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "第"+i+"行，"+personEntity.getName()+"已经录过，请重新录入");
                            }
                        }
                    }
                    String personId = UUID.randomUUID().toString();

                    AreasEntity countyEntity = areasMapper.queryByIdByName(personEntity.getCounty(), 2);
                    if (countyEntity == null) {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "第" + i + "行，"+personEntity.getName()+"所属区县在系统不存在");
                    }
                    List<AreasEntity> townEntities = areasMapper.queryByAreasPid(personEntity.getTown(), countyEntity.getId());
                    if (townEntities == null || townEntities.size() == 0) {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "第" + i + "行，"+personEntity.getName()+"所属乡镇在系统不存在");
                    }
                    List<AreasEntity> villageEntities = new ArrayList<>();
                    for (AreasEntity areasEntity :
                            townEntities) {
                        villageEntities.addAll(areasMapper.queryByAreasPid(personEntity.getVillage(), areasEntity.getId()));
                    }
                    if (villageEntities == null || villageEntities.size() == 0) {
                        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "第" + i + "行，"+personEntity.getName()+"所属村组在系统不存在");
                    }
                    //乡镇
                    List<AreasEntity> collect = villageEntities.stream()
                            .filter(AreasEntity -> AreasEntity.getName().equals(personEntity.getVillage()))
                            .collect(toList());

                    personEntity.setCounty(countyEntity.getId());
                    personEntity.setTown(townEntities.get(0).getId());
                    personEntity.setVillage(collect.get(0).getId());
                    personEntity.setId(personId);
                    //personEntity.setProjectId(projectDetailId);
                    //personEntity.setStatus("3");//新增状态是未提交 3
                    personEntity.setDepartmentId(user.getDepartmentId());
                    //personEntity.setUserId(user.getId());
                    personEntity.setProjectType("0");
                    personEntity.setName(personEntity.getName().replaceAll(" ",""));
                    resultList.add(personEntity);
                }
            }
            logger.info("execute success {}", personEntities.size());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        } finally {
            uploadFile.delete();
        }
        if (resultList != null && resultList.size() > 0) {
            for (PersonUploadEntity personUploadEntity : resultList
            ) {
                personUploadMapper._addEntity(personUploadEntity);
            }

        }

        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "上传成功");
    }

}