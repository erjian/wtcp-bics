package cn.com.wanwei.bic.model;

import cn.com.wanwei.bic.entity.TravelAgentEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - TravelAgentModel
 */
@Data
public class TravelAgentModel {
    private String jpin;

    private List<Map<String, Object>> list;

    private TravelAgentEntity travelAgentEntity;
}
