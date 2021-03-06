/**
 * 该源代码文件 BusinessService.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日16:25:55
 */
package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.BusinessEntity;
import cn.com.wanwei.common.model.ResponseMessage;

/**
 * wtcp-bics - BusinessService 营业信息管理接口
 */
public interface BusinessService {

	/**
	 * 保存一条记录
	 * @auth linjw 2019年10月9日16:26:22
	 * @param record
	 * @return
	 */
	ResponseMessage insert(BusinessEntity record, String userName) throws Exception;

	/**
	 * 编辑一条记录
	 * @auth linjw 2019年10月9日16:27:08
	 * @param id
	 * @param record
	 * @return
	 */
	ResponseMessage updateById(String id, BusinessEntity record, String userName) throws Exception;

	/**
	 * 根据关联主键查询一条记录
	 * @auth linjw
	 * @param principalId
	 * @return
	 * @throws Exception
	 */
    BusinessEntity findByPrincipalId(String principalId) throws Exception;
}
