package cn.com.wanwei.bic.model;

import cn.com.wanwei.bic.entity.PoiEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PoiModel {

    private String type;

    private List<Map<String, Object>> list;

    private PoiEntity poiEntity;
}
