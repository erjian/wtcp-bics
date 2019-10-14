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

/**
 * wtcp-bics - ScenicModel
 */
@Data
public class ScenicModel {

	private String type;

	private ScenicEntity scenicEntity;
}
