package cn.com.wanwei.bic.model;

import cn.com.wanwei.bic.entity.EntertainmentEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - EntertainmentModel
 */
@Data
public class EntertainmentModel {

    private String jpin;

    private List<Map<String, Object>> list;

    private EntertainmentEntity entertainmentEntity;
}
