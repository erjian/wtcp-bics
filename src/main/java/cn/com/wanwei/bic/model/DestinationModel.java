/**
 * 该源代码文件 DestinationModel.java 是工程“wtcp-bics”的一部分。
 * 
 * @project wtcp-bics
 * @author linjw
 * @date 2019年11月7日17:31:12
 */

package cn.com.wanwei.bic.model;

import cn.com.wanwei.bic.entity.DestinationEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - DestinationModel
 */
@Data
public class DestinationModel {

	private List<Map<String, Object>> list;

	private DestinationEntity destinationEntity;
}
