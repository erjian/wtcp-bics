/**
 * 该源代码文件 ResourceConfigService.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年12月20日11:14:02
 */
package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.ResourceConfigEntity;
import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * wtcp-bics - ResourceConfigService 资源配置信息管理接口
 */
public interface ResourceConfigService {

	/**
	 * 新增一条记录
	 * @auth linjw 2019年12月20日11:14:30
	 * @param entity
	 * @param user
	 * @return
	 */
	ResponseMessage save(ResourceConfigEntity entity, User user) throws Exception;

	/**
	 * 编辑一条记录
	 * @auth linjw 2019年12月20日11:14:30
	 * @param id
	 * @param entity
	 * @param user
	 * @return
	 */
	ResponseMessage edit(String id, ResourceConfigEntity entity, User user) throws Exception;

	/**
	 * 根据id查询一条记录
	 * @auth linjw 2019年12月20日11:15:03
	 * @param id
	 * @return
	 */
	ResponseMessage selectByPrimaryKey(String id) throws Exception;

	/**
	 * 根据code查询一条记录
	 * @auth linjw 2019年12月20日11:18:06
	 * @param code
	 * @return
	 */
	ResponseMessage selectByCode(String code) throws Exception;

	/**
	 * 查询表信息
	 * @auth linjw 2019年12月20日16:32:29
	 * @return
	 * @throws Exception
	 */
	ResponseMessage selectTableInfo() throws Exception;

	/**
	 * 根据表名查询相应的字段信息
	 * @auth linjw 2019年12月20日17:05:33
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	ResponseMessage selectColumnInfo(String tableName) throws Exception;
}
