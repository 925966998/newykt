package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.*;
import com.ky.ykt.excle.ExcelStyle;
import com.ky.ykt.excle.ExportExcel;
import com.ky.ykt.mapper.AreasMapper;
import com.ky.ykt.mapper.DepartmentMapper;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.ProjectService;
import com.ky.ykt.service.ProjectTypeService;
import com.ky.ykt.service.StatisticsService;
import com.ky.ykt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName Statisticscontroller
 * @Description: TODO
 * @Author czw
 * @Date 2020/3/4
 **/
@RestController
@RequestMapping("/ky-ykt/statistics")
public class StatisticsController {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);
    @Autowired
    StatisticsService statisticsService;
    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectTypeService projectTypeService;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    AreasMapper areasMapper;


    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The Statisticscontroller queryPage method params are {}", params);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if(!user.getRoleId().equals("a599f1da-f57c-4afc-a600-b58e15836aa0")){
            DepartmentEntity departmentEntity = departmentMapper._get(user.getDepartmentId());
            params.put("userId",user.getId());
            params.put("DJFlag","4J");
        }
        if (user.getRoleId().equals("426f5a25-c237-472c-975f-9a08e93622c7")) {
            if (params.get("operDepartment") != null) {
                List<DepartmentEntity> departmentEntities = departmentMapper.queryByParentId(params.get("operDepartment").toString());
                List<String> departmentIdList = new ArrayList<String>();
                if (departmentEntities != null && departmentEntities.size() > 0) {
                    for (DepartmentEntity departmentEntity : departmentEntities
                    ) {
                        departmentIdList.add(departmentEntity.getId());
                    }
                    departmentIdList.add(params.get("operDepartment").toString());
                    params.put("departmentIdList", departmentIdList);
                    params.put("departmentIdListFlag", "departmentIdListFlag");
                }
            } else {
                String roleCode = request.getSession().getAttribute("roleCode").toString();
                if (roleCode.equals("3") || roleCode.equals("4")) {
                    List<DepartmentEntity> departmentEntities = departmentMapper.queryByParentId(user.getDepartmentId());
                    List<String> departmentIdList = new ArrayList<String>();
                    params.put("operDepartment", user.getDepartmentId());
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
        }
        RestResult restResult = statisticsService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();

        return this.toJson(data);
    }

    @RequestMapping(value = "/statisticCount", method = RequestMethod.GET)
    public Object statisticCount(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The Statisticscontroller queryPage method params are {}", params);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if(!user.getRoleId().equals("a599f1da-f57c-4afc-a600-b58e15836aa0")){
            params.put("userId", user.getId());
            params.put("DJFlag","4J");
        }
        BigDecimal bigDecimal = statisticsService.statisticCount(params);
        return bigDecimal;
    }

    @RequestMapping(value = "/projectPage", method = RequestMethod.GET)
    public Object projectPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The Statisticscontroller queryPage method params are {}", params);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if(!user.getRoleId().equals("a599f1da-f57c-4afc-a600-b58e15836aa0")){
            params.put("userId", user.getId());
            params.put("DJFlag","4J");
        }
        RestResult restResult = statisticsService.statisticPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }

    public JSONObject toJson(PagerResult data) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("total", data.getTotalItemsCount());
        jsonObj.put("rows", data.getItems());
        jsonObj.put("footer", data.getFooters());
        return jsonObj;
    }

    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    public Map<String, Object> statistic(String id,HttpServletRequest request) {
        logger.info("The ProjectController select method params are {}", id);
        Map params = HttpUtils.getParams(request);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if(!user.getRoleId().equals("a599f1da-f57c-4afc-a600-b58e15836aa0")){
            params.put("userId", user.getId());
        }
        List<StatisticEntity> statisticEntities = projectService.queryType(params);
        Map<String, Object> mapResult = new HashMap<String, Object>();
        List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
        for (StatisticEntity statisticEntity : statisticEntities
        ) {
            Map<String, Object>
                    map = new HashMap<String, Object>();
            map.put("value", statisticEntity.getNum());
            map.put("name", statisticEntity.getProjectType());
            datas.add(map);
        }
        mapResult.put("datas", statisticEntities);
        return mapResult;
    }

    @RequestMapping(value = "/dateStatistic", method = RequestMethod.GET)
    public List<StatisticEntity> dateStatistic(HttpServletRequest request) {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if(!user.getRoleId().equals("a599f1da-f57c-4afc-a600-b58e15836aa0")){
            mapResult.put("userId",user.getId());
            mapResult.put("paymentDepartment",user.getDepartmentId());
            mapResult.put("DJFlag","4J");
        }
        List<StatisticEntity> statistic = projectService.statistic(mapResult);
        return statistic;
    }

    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Map<String, Object> query() {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        List<StatisticEntity> statistic = projectService.statistic(mapResult);
        return mapResult;
    }

    @RequestMapping(value = "/excel", method = RequestMethod.GET)
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
            params.put("userId",user.getId());
            params.put("DJFlag","4J");
        }
        if(params.get("areaId") != null && StringUtils.isNotEmpty(params.get("areaId").toString())){
            AreasEntity areasEntity = areasMapper._get(params.get("areaId").toString());
            params.put("level",areasEntity.getLevel());
        }
        Map map = this.excel(params);
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

    public Map excel(Map params) {
        Map resultMap = new HashMap();
        ExcelStyle style = new ExcelStyle();
        List<String[]> data = new ArrayList();
        List<StatisticEntity> entities = (List<StatisticEntity>) statisticsService.staticsAll(params).getData();
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
        String tStamp = dfs.format(new Date());
        style.setColumnWidth(25);
        style.setSheetName("导出");
        style.setXlsName("人员信息表_" + tStamp);
            for (int i = 0; i < entities.size(); i++) {
                data.add(new String[]{
                        entities.get(i).getUserName(),
                entities.get(i).getPhone(),
                        entities.get(i).getIdCardNo(),
                entities.get(i).getProjectName(),
                entities.get(i).getGrantAmount().toString(),
                entities.get(i).getCounty(),
                entities.get(i).getTown(),
                entities.get(i).getVillage(),
                entities.get(i).getAddress(),
                        });
            }
        resultMap.put("header",
                new String[]{"姓名", "手机号", "身份证号","资金发放名称", "发放金额", "所属区县","所属乡镇","所属村组","详细地址"});
        resultMap.put("data", data);
        resultMap.put("style", style);
        return resultMap;


    }
}
