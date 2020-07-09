package com.ky.ykt.service;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.PersonUploadEntity;
import com.ky.ykt.mapper.PersonUploadMapper;
import com.ky.ykt.mybatis.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeChatService {

    @Autowired
    PersonUploadMapper personUploadMapper;

    public Object wechatLogin(JSONObject parseObject) {
        if (parseObject.containsKey("name") && parseObject.containsKey("idCardNo") && parseObject.containsKey("bankCardNo")) {
            List<PersonUploadEntity> personUploadEntities = personUploadMapper.queryByIdCardNo(parseObject.getString("idCardNo"));
            if (personUploadEntities.size() > 0 && personUploadEntities != null) {
                if (personUploadEntities.get(0).getBankCardNo().equals(parseObject.getString("bankCardNo"))) {
                    return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadEntities.get(0));
                }
            } else {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "没有查到该用户信息，请重新填写");
            }

        } else {
            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "登录信息错误，请重新填写");
        }
        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "登录信息错误，请重新填写");
    }

}
