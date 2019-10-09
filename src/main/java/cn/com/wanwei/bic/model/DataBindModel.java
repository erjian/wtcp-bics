/**
 * 该源代码文件 DataBindModel.java 是工程“wtcp-bics”的一部分。
 * 
 * @project wtcp-bics
 * @author linjw
 * @date 2019年10月9日17:24:51
 */

package cn.com.wanwei.bic.model;

import lombok.Data;

import java.util.List;

/**
 * wtcp-traveltips - DataBindModel
 * 攻略model
 */
@Data
public class DataBindModel {

	private String deptCode;

	private List<Long> ids;
}
