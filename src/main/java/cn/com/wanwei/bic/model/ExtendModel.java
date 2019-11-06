package cn.com.wanwei.bic.model;

import cn.com.wanwei.bic.entity.ExtendEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/11/6 15:43:43
 * @desc wtcp-bics -  ExtendModel
 */
@Data
public class ExtendModel {

    private List<Map<String, Object>> list;

    private ExtendEntity extendEntity;

}
