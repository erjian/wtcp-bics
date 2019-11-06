package cn.com.wanwei.bic.model;

import cn.com.wanwei.bic.entity.PeripheryEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 周边管理model
 * @author
 */
@Data
public class PeripheryModel {

    private String type;

    private List<Map<String, Object>> list;

    private PeripheryEntity peripheryEntity;

}
