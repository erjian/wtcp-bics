/**
 * 该源代码文件 EnterpriseService.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:47:54
 */
package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.EnterpriseEntity;
import cn.com.wanwei.common.model.ResponseMessage;

/**
 * wtcp-bics - EnterpriseService 企业信息管理接口
 */
public interface EnterpriseService {

	/**
	 * 保存一条记录
	 * @auth linjw 2019年10月9日14:48:19
	 * @param record
	 * @return
	 */
	ResponseMessage save(EnterpriseEntity record, String userName) throws Exception;

	/**
	 * 编辑一条记录
	 * @auth linjw 2019年10月9日14:50:01
	 * @param id
	 * @param record
	 * @return
	 */
	ResponseMessage edit(String id, EnterpriseEntity record, String userName) throws Exception;

	/**
	 * 根据关联主键获取一条数据
	 * @auth linjw 2019年10月16日09:39:57
	 * @param principalId
	 * @return
	 */
    EnterpriseEntity selectByPrincipalId(String principalId);
}
