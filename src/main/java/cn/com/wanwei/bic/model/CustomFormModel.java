/**
 * 该源代码文件 CustomFormModel.java 是工程“wtcp-bics”的一部分。
 * 
 * @project wtcp-bics
 * @author linjw
 * @date 2020年3月12日10:42:17
 */

package cn.com.wanwei.bic.model;

import lombok.Data;

import java.util.Map;

/**
 * wtcp-bics - CustomFormModel
 */
@Data
public class CustomFormModel {

	private String principalId;

	private Map<String, String> form;
}
