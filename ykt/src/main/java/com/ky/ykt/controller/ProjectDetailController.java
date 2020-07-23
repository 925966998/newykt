package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.*;
import com.ky.ykt.excle.ExcelHMStyle;
import com.ky.ykt.excle.ExcelStyle;
import com.ky.ykt.excle.ExportExcel;
import com.ky.ykt.excle.ExportHM;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mapper.*;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.PersonService;
import com.ky.ykt.service.ProjectDetailService;
import com.ky.ykt.utils.HttpUtils;
import com.ky.ykt.utils.NumberToCh;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ProjectController
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
@RestController
@RequestMapping("/ky-ykt/projectDetail")
public class ProjectDetailController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDetailController.class);

    @Autowired
    ProjectDetailService projectDetailService;
    @Autowired
    PersonService personService;
    @Autowired
    ProjectDetailMapper projectDetailMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    PersonMapper personMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    ProjectTypeMapper projectTypeMapper;

    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectDetailController queryByParams method params are {}", params);
        return projectDetailService.queryAll(params);
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectDetailController queryPage method params are {}", params);
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
        if(params.get("state") != null){
            if(params.get("state").equals("1")){
                params.put("projectStatus", "1");
            }else if(params.get("state").equals("0")){
                params.put("projectStatus", "0");
            }else if(params.get("state").equals("3")){
                params.put("projectStatus", "3");
                params.put("userDepartment",user.getDepartmentId());
            }
        }
        if(params.get("projectName") != null){
            if(params.get("projectName").equals("0")){
                params.put("projectName", " ");
            }
        }
        RestResult restResult = projectDetailService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }

    @RequestMapping(value = "/auditInfo", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object auditInfo(String id) {
        logger.info("The ProjectDetailController auditInfo method params are {}", id);
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> returnMap = new HashMap<String, Object>();
        long num = personService.queryCountByProjectCode(id);
        Map<String, Object> projectDetailEntityMap = projectDetailService.queryOne(id);
        returnMap.put("projectDetailEntity", projectDetailEntityMap);
        returnMap.put("pullNum", num);
        return returnMap;
    }

    @Log(description = "项目审核操作", module = "项目管理")
    @RequestMapping(value = "/audit", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(ProjectDetailEntity projectDetailEntity) {
        logger.info("The ProjectDetailController saveOrUpdate method params are {}", projectDetailEntity);
        if(projectDetailEntity.getState() == 2){
            ProjectDetailEntity projectDetailEntity1 = projectDetailMapper._get(projectDetailEntity.getId());
            ProjectEntity projectEntity = projectMapper._get(projectDetailEntity1.getProjectId());
            projectEntity.setPaymentAmount(projectEntity.getPaymentAmount().subtract(projectDetailEntity1.getPaymentAmount()));
            projectEntity.setSurplusAmount(projectEntity.getSurplusAmount().add(projectDetailEntity1.getPaymentAmount()));
            projectMapper._updateEntity(projectEntity);
        }
        return projectDetailService.update(projectDetailEntity);
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
            ProjectDetailEntity projectDetailEntity = new ProjectDetailEntity();
            projectDetailEntity.setId(params.get("id").toString());
            projectDetailEntity.setState(3);
            projectDetailService.update(projectDetailEntity);
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
                    entity.getOpeningBank(),
                    entity.getBankCardNo(),
                    entity.getProjectName(),
                    entity.getGrantAmount(),
                    //entity.getCounty(),
                    entity.getCountyName(),
                    entity.getTownName(),
                    entity.getVillageName(),
                    entity.getAddress(),
                    projectDetailEntity.getId()
            });
        }
        resultMap.put("header",
                new String[]{"姓名", "手机号", "身份证号", "开户行", "银行卡号", "项目资金名称", "发放金额", "所属区县", "所属乡镇", "所属村组", "详细地址", "流水号", "回执状态","失败原因"});
        resultMap.put("data", data);
        resultMap.put("style", style);
        return resultMap;
    }

    public JSONObject toJson(PagerResult data) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("total", data.getTotalItemsCount());
        jsonObj.put("rows", data.getItems());
        return jsonObj;
    }

    @RequestMapping(value = "/exporthm", method = RequestMethod.GET)
    protected Object exporthm(HttpServletRequest request, HttpServletResponse response) {
        Map params = HttpUtils.getParams(request);
        Map map = this.fieldExporthm(params);
        String fileUrl = "";
        List<String[]> data = (List<String[]>) map.get("data");
        ExcelHMStyle style = (ExcelHMStyle) map.get("style");
        try {
            /*
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String((style.getXlsName() + ".xls").getBytes(), "iso-8859-1"));
            OutputStream out = response.getOutputStream();
            */
            fileUrl = ExportHM.exporthm(data, style);
        } catch (Exception e) {
            logger.error("exportExcel error:{}", e);
        }
        Map map1 = new HashMap();
       map1.put("fileUrl", "http://" + request.getServerName() + ":" + request.getServerPort() + fileUrl);
       //map1.put("fileUrl", fileUrl);
        return map1;
    }


    public Map fieldExporthm(Map params) {
        Map resultMap = new HashMap();
        ExcelHMStyle style = new ExcelHMStyle();
        List<String[]> data = new ArrayList();
        ProjectDetailEntity projectDetailEntity = projectDetailMapper._get(params.get("id").toString());
        ProjectEntity projectEntity = projectMapper._get(projectDetailEntity.getProjectId());
        DepartmentEntity departmentEntity = departmentMapper._get(projectDetailEntity.getPaymentDepartment());
        SysUserEntity sysUserEntity = sysUserMapper._get(projectDetailEntity.getOperUser());
        ProjectTypeEntity projectTypeEntity = projectTypeMapper._get(projectEntity.getProjectType());
        Map map = new HashMap();
        map.put("projectId", projectDetailEntity.getId());
        List<PersonEntity> entities = personMapper._queryAll(map);
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        String tStamp = dfs.format(new Date());
        //style.setColumnWidth(25);
        //sheet
        style.setSheetName(projectDetailEntity.getProjectName() + "发放花名表");
        //文件名字
        style.setXlsName(projectDetailEntity.getProjectName() + "发放花名表_" + tStamp);
        //单据号
        style.setDocumentNumber(tStamp);
        //日期
        style.setDocumentDate(sdf.format(projectDetailEntity.getStartTime()));
        //主管单位
        style.setDocumentCompetent(departmentEntity.getDepartmentName());
        //备注
        style.setDocumentRemark("");
        //审核意见
        style.setDocumentOpinion("");
        //单据名称
        style.setDocumentBillName(projectDetailEntity.getProjectName());
        //负责人
        style.setDocumentPrincipalPerson(" ");
        //经办人
        style.setDocumentResponsiblePerson(" ");
        //资金类型
        style.setDocumentProjectType(projectTypeEntity.getName());
        BigDecimal sum = new BigDecimal(BigInteger.ZERO);
        for (PersonEntity entity : entities) {
            sum = sum.add(new BigDecimal(entity.getGrantAmount()));
            data.add(new String[]{
                    entity.getName(),
                    entity.getIdCardNo(),
                    entity.getCountyName() + "" + entity.getTownName() + "" + entity.getVillageName() + "" + entity.getAddress(),
                    entity.getBankCardNo(),
                    entity.getGrantAmount(),
                    entity.getFailReason()
            });
        }
        //合计
        style.setSumMoney(sum.toString());
        //合计大写
        String sumMoney = NumberToCh.number2CNMontrayUnit(sum);
        style.setBigSumMoney(sumMoney);
        resultMap.put("data", data);
        resultMap.put("style", style);
        return resultMap;
    }

    /**
     * 预览pdf文件，获取PDF需要浏览的PDF文件流
     *
     * @param request
     * @param response
     * @param fileName
     * @author chunlynn
     */
    @RequestMapping(value = "/pdfStreamHandeler", method = RequestMethod.GET)
    public void pdfStreamHandeler(HttpServletRequest request, HttpServletResponse response, String fileName, String urlPath) {

        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            logger.debug("==================pdf处理开始==================");
            System.out.println("请求PDF路径：" + urlPath);
            os = response.getOutputStream();
            //获得PDF文件流，核心代码

            File file = new File(urlPath);
            InputStream is = new FileInputStream(file);
            System.out.println("获取流结束。。。。");
            bis = new BufferedInputStream(is);
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }

        } catch (Exception e) {
            logger.error("pdf处理出现异常：" + e.getMessage() + "; ");

        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
