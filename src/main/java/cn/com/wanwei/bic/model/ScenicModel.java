/**
 * 该源代码文件 ScenicModel.java 是工程“wtcp-bics”的一部分。
 * 
 * @project wtcp-bics
 * @author linjw
 * @date 2019年10月14日10:31:49
 */

package cn.com.wanwei.bic.model;

import cn.com.wanwei.bic.entity.ScenicEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - ScenicModel
 */
@Data
public class ScenicModel {

	private String type;

	private List<Map<String, Object>> list;

	private ScenicEntity scenicEntity;
}
