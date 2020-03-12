package cn.com.wanwei.bic.service;


import cn.com.wanwei.bic.entity.DynamicFormEntity;

import java.util.List;

public interface DynamicFormService {

    int insert(DynamicFormEntity dynamicFormEntity, String username);

    int batchInsert(List<DynamicFormEntity> dynamicFormList, String username);

    int deleteById(String id);

    int deleteByPrincipalId(String principalId);

    DynamicFormEntity findById(String id);

    List<DynamicFormEntity> findByPrincipalId(String principalId);


}
