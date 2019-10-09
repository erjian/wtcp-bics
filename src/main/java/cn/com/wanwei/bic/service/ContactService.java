/**
 * 该源代码文件 ContactService.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日16:41:28
 */
package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.ContactEntity;
import cn.com.wanwei.common.model.ResponseMessage;

/**
 * wtcp-bics - ContactService 通讯信息管理接口
 */
public interface ContactService {

	/**
	 * 保存一条记录
	 * @auth linjw 2019年10月9日16:26:22
	 * @param record
	 * @return
	 */
	ResponseMessage save(ContactEntity record, String userName) throws Exception;

	/**
	 * 查询一条记录
	 * @auth linjw 2019年10月9日16:27:01
	 * @param id
	 * @return
	 */
	ContactEntity selectByPrimaryKey(Long id) throws Exception;

	/**
	 * 编辑一条记录
	 * @auth linjw 2019年10月9日16:27:08
	 * @param id
	 * @param record
	 * @return
	 */
	ResponseMessage edit(Long id, ContactEntity record, String userName) throws Exception;
}
