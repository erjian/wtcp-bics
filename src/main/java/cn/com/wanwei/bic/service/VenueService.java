/**
 * 该源代码文件 VenueService.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2020年3月2日15:41:06
 */
package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.VenueEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * wtcp-bics - VenueService 场馆基础信息管理接口
 */
public interface VenueService {

	/**
	 * 保存一条记录
	 * @auth linjw 2020年3月2日14:50:03
	 * @param venueModel
	 * @return
	 */
	ResponseMessage save(EntityTagsModel<VenueEntity> venueModel, User user, Long ruleId, Integer appCode) throws Exception;

	/**
	 * 根据Id删除一条记录
	 * @auth linjw 2019年10月9日15:26:58
	 * @param id
	 * @return
	 */
	ResponseMessage deleteByPrimaryKey(String id) throws Exception;

	/**
	 * 查询一条记录
	 * @auth linjw 2020年3月2日14:59:55
	 * @param id
	 * @return
	 */
	VenueEntity selectByPrimaryKey(String id) throws Exception;

	/**
	 * 编辑一条记录
	 * @auth linjw 2020年3月2日15:01:01
	 * @param id
	 * @param venueModel
	 * @return
	 */
	ResponseMessage edit(String id, EntityTagsModel<VenueEntity> venueModel, User user) throws Exception;

	/**
	 * 获取分页列表
	 * @author linjw 2020年3月2日14:38:02
	 *
	 * @param page
	 * @param size
	 * @param filter
	 * @return
	 */
	ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) throws Exception;

	/**
	 * 关联机构
	 * @author linjw 2019年10月9日17:28:51
	 * @param updatedUser
	 * @param model
	 * @return
	 */
	ResponseMessage dataBind(String updatedUser, DataBindModel model) throws Exception;

	/**
	 * 修改审核状态
	 * @author linjw 2019年10月16日11:27:27
	 * @param id
	 * @param status
	 * @param username
	 * @return
	 * @throws Exception
	 */
	ResponseMessage changeStatus(String id, Integer status, String username) throws Exception;

	/**
	 * 景区审核
	 * @author linjw 2019年10月21日11:32:41
	 * @param id
	 * @param auditStatus
	 * @param msg
	 * @param currentUser
	 * @return
	 * @throws Exception
	 */
	ResponseMessage examineVenue(String id, int auditStatus, String msg, User currentUser) throws Exception;

	/**
	 * 获取场馆信息，title可以为空
	 * @param title
	 * @param status
	 * @return
	 */
	ResponseMessage getVenueInfo(String title, Integer status);

	/**
	 * 关联标签
	 * @param tags
	 * @param user
	 * @return
	 */
	ResponseMessage relateTags(Map<String, Object> tags, User user);

	ResponseMessage findByTitleAndIdNot(String title, String s);

}
