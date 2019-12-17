/**
 * 该源代码文件 ScenicService.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:47:54
 */
package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

/**
 * wtcp-bics - ScenicService 景区基础信息管理接口
 */
public interface ScenicService {

	/**
	 * 保存一条记录
	 * @auth linjw 2019年10月9日14:48:19
	 * @param scenicModel
	 * @return
	 */
	ResponseMessage save(EntityTagsModel<ScenicEntity> scenicModel, User user, Long ruleId, Integer appCode) throws Exception;

	/**
	 * 根据Id删除一条记录
	 * @auth linjw 2019年10月9日15:26:58
	 * @param id
	 * @return
	 */
	ResponseMessage deleteByPrimaryKey(String id) throws Exception;

	/**
	 * 查询一条记录
	 * @auth linjw 2019年10月9日14:49:18
	 * @param id
	 * @return
	 */
	ScenicEntity selectByPrimaryKey(String id) throws Exception;

	/**
	 * 编辑一条记录
	 * @auth linjw 2019年10月9日14:50:01
	 * @param id
	 * @param scenicModel
	 * @return
	 */
	ResponseMessage edit(String id, EntityTagsModel<ScenicEntity> scenicModel, User user) throws Exception;

	/**
	 * 获取分页列表
	 * @author linjw 2019年10月9日15:05:47
	 *
	 * @param page
	 * @param size
	 * @param user
	 * @param filter
	 * @return
	 */
	ResponseMessage findByPage(Integer page, Integer size, User user, Map<String, Object> filter) throws Exception;

	/**
	 * 关联机构
	 * @author linjw 2019年10月9日17:28:51
	 * @param updatedUser
	 * @param model
	 * @return
	 */
    ResponseMessage dataBind(String updatedUser, DataBindModel model) throws Exception;

	/**
	 * 修改权重
	 * @author linjw 2019年10月9日17:45:27
	 * @param weightModel
	 * @param user
	 * @return
	 */
	ResponseMessage changeWeight(WeightModel weightModel, User user) throws Exception;

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
    ResponseMessage examineScenic(String id, int auditStatus, String msg, User currentUser) throws Exception;

	/**
	 * 获取景区信息，title可以为空
	 * @param title
	 * @return
	 */
	ResponseMessage getScenicInfo(String title);

	/**
	 * 关联标签
	 * @param tags
	 * @param user
	 * @return
	 */
    ResponseMessage relateTags(Map<String, Object> tags, User user);

	/**
	 * C端获取一条景区详情
	 * @param id
	 * @return
	 */
	ResponseMessage getOne(String id) throws Exception;

    ResponseMessage findByTitleAndIdNot(String title, String s);

	/**
	 * 获取景区列表
	 * @param type code
	 * @param searchValue 搜索条件
	 * @return
	 */
	ResponseMessage findBySearchValue(String type,String searchValue);

	/**
	 * 根据区域编码获取景区列表
	 * @param page 页数
	 * @param size 每页数量
	 * @param filter  参数
	 * @return 景区列表
	 */
    ResponseMessage scenicPageNew(Integer page, Integer size, Map<String,Object> filter);

	/**
	 * 根据ids获取景区列表
	 * @param ids ids
	 * @return 景区列表
	 */
	ResponseMessage findListByIds(String ids, Integer status);
}
