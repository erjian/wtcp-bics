package cn.com.wanwei.bic.service;


import cn.com.wanwei.bic.model.CustomFormModel;

import java.util.Map;

public interface DynamicFormService {

    int batchInsert(CustomFormModel customFormModel, String username);

    int deleteById(String id);

    int deleteByPrincipalId(String principalId);

    String findByPidAndField(String principalId, String field);

    Map<String, Object> findByPrincipalId(String principalId);
}
