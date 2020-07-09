package com.ky.ykt.service;

import com.ky.ykt.entity.DepartmentEntity;
import com.ky.ykt.mapper.DepartmentMapper;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DepartmentService
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/18
 **/
@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    @Autowired
    DepartmentMapper departmentMapper;

    public Object queryAll(Map params) {
        List<DepartmentEntity> departmentEntities = departmentMapper._queryAll(params);
        return departmentEntities;
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public RestResult queryPage(Map params) {
        List<DepartmentEntity> list = departmentMapper._queryPage(params);
        long count = departmentMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    public List<DepartmentEntity> queryByParentId(Map<String, Object> params) {
        params.put("isUse", 0);
        List<DepartmentEntity> departmentEntities = departmentMapper._queryAll(params);
//        Map<String, List<DepartmentEntity>> sub = departmentEntities.stream().filter(
//                node -> !node.getParentId().equals("0")).collect(Collectors.groupingBy(node -> node.getParentId())
//        );
//        departmentEntities.stream().filter(node -> node.getParentId().equals("0")).collect(Collectors.toList());

//        Map<String, List<DepartmentEntity>> sub = departmentEntities.stream().filter(node -> !node.getParentId().equals("0")).collect(Collectors.groupingBy(node -> node.getParentId()));
//        departmentEntities.forEach(node -> node.setChildren(sub.get(node.getId())));
//        List<DepartmentEntity> collect = departmentEntities.stream().filter(node -> node.getParentId().equals("0")).collect(Collectors.toList());
        return departmentEntities;
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, departmentMapper._add(params));
    }

    public RestResult _get(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, departmentMapper._get(id));
    }

    public Object add(DepartmentEntity departmentEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, departmentMapper._addEntity(departmentEntity));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, departmentMapper._update(params));
    }

    public Object update(DepartmentEntity departmentEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, departmentMapper._updateEntity(departmentEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, departmentMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, departmentMapper._deleteForce(id));
    }
}
