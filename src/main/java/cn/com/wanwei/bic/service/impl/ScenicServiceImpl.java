/**
 * 该源代码文件 ScenicServiceImpl.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:53:22
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.entity.ScenicTagsEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.ScenicMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.ScenicModel;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - ScenicServiceImpl 景区基础信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class ScenicServiceImpl implements ScenicService {

	@Autowired
	private ScenicMapper scenicMapper;

	@Autowired
	private CoderServiceFeign coderServiceFeign;

	@Autowired
	private TagsService tagsService;

	@Autowired
	private AuditLogMapper auditLogMapper;

	@Override
	public ResponseMessage save(ScenicModel scenicModel, String userName, Long ruleId, Integer appCode) {
		ScenicEntity record = scenicModel.getScenicEntity();
		String type = scenicModel.getType();
		record.setId(UUIDUtils.getInstance().getId());
		ResponseMessage result =coderServiceFeign.buildSerialByCode(ruleId, appCode, type);
		record.setCode(result.getData().toString());
		record.setCreatedUser(userName);
		record.setCreatedDate(new Date());
		record.setStatus(0);
		scenicMapper.insert(record);
		return ResponseMessage.defaultResponse().setMsg("保存成功");
	}

	@Override
	public ResponseMessage deleteByPrimaryKey(String id) {
		scenicMapper.deleteByPrimaryKey(id);
		return ResponseMessage.defaultResponse().setMsg("删除成功");
	}

	@Override
	public ScenicEntity selectByPrimaryKey(String id) {
		return scenicMapper.selectByPrimaryKey(id);
	}

	@Override
	public ResponseMessage edit(String id, ScenicModel scenicModel, User user) {
		ScenicEntity entity = scenicMapper.selectByPrimaryKey(id);
		ScenicEntity record = scenicModel.getScenicEntity();
		if(null == entity){
			return ResponseMessage.validFailResponse().setMsg("不存在该景区");
		}
		record.setId(id);
		record.setCode(entity.getCode());
		record.setCreatedDate(entity.getCreatedDate());
		record.setCreatedUser(entity.getCreatedUser());
		record.setDeptCode(entity.getDeptCode());
		record.setStatus(0);
		record.setUpdatedDate(new Date());
		record.setUpdatedUser(user.getUsername());
		scenicMapper.updateByPrimaryKeyWithBLOBs(record);
		this.saveTags(scenicModel.getList(),id,user);
		return ResponseMessage.defaultResponse().setMsg("更新成功");
	}

	private void saveTags(List<Map<String, Object>> tagsList, String principalId, User user){
		List<BaseTagsEntity> list = Lists.newArrayList();
		for(int i=0; i<tagsList.size(); i++){
			BaseTagsEntity entity = new BaseTagsEntity();
			entity.setTagCatagory(tagsList.get(i).get("tagCatagory").toString());
			entity.setTagName(tagsList.get(i).get("tagName").toString());
			list.add(entity);
		}
		tagsService.batchInsert(principalId,list,user, ScenicTagsEntity.class);
	}

	@Override
	public ResponseMessage findByPage(Integer page, Integer size, User user, Map<String, Object> filter) {
		Sort.Order[] order = new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "created_date"),
				new Sort.Order(Sort.Direction.DESC, "updated_date")};
		Sort sort = Sort.by(order);
		MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
		PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
		PageHelper.orderBy(pageRequest.getOrders());
		Page<ScenicEntity> userEntities = scenicMapper.findByPage(filter);
		PageInfo<ScenicEntity> pageInfo = new PageInfo<>(userEntities, pageRequest);
		return ResponseMessage.defaultResponse().setData(pageInfo);
	}

	@Override
	public ResponseMessage dataBind(String updatedUser, DataBindModel model) {
		String deptCode = model.getDeptCode();
		List<String> ids = model.getIds();
		scenicMapper.dataBind(updatedUser, new Date(), deptCode, ids);
		return ResponseMessage.defaultResponse().setMsg("关联机构成功");
	}

	@Override
	public ResponseMessage changeWeight(String id, Float weightNum, String username) {
		ScenicEntity entity = scenicMapper.selectByPrimaryKey(id);
		if(null == entity){
			return ResponseMessage.validFailResponse().setMsg("无景区信息");
		}
		entity.setUpdatedUser(username);
		entity.setUpdatedDate(new Date());
		entity.setWeight(weightNum);
		scenicMapper.updateByPrimaryKeyWithBLOBs(entity);
		return ResponseMessage.defaultResponse().setMsg("权重修改成功");
	}

	@Override
	public ResponseMessage changeStatus(String id, Integer status, String username) throws Exception {
		ScenicEntity entity = scenicMapper.selectByPrimaryKey(id);
		if(null == entity){
			return ResponseMessage.validFailResponse().setMsg("无景区信息");
		}
		if(status == 9 && entity.getStatus() != 1){
			return ResponseMessage.validFailResponse().setMsg("景区未审核通过，不能上线，请先审核景区信息");
		}
		// 添加上下线记录
		String msg = status == 9? "上线成功" : "下线成功";
		saveAuditLog(entity.getStatus(),status,id,username,msg, 1);
		entity.setUpdatedUser(username);
		entity.setUpdatedDate(new Date());
		entity.setStatus(status);
		scenicMapper.updateByPrimaryKeyWithBLOBs(entity);
		return ResponseMessage.defaultResponse().setMsg("状态变更成功");
	}

	@Override
	public ResponseMessage examineScenic(String id, int auditStatus, String msg, User currentUser) throws Exception {
		ScenicEntity scenicEntity = scenicMapper.selectByPrimaryKey(id);
		if (scenicEntity != null) {
			// 添加审核记录
			saveAuditLog(scenicEntity.getStatus(),auditStatus,id,currentUser.getUsername(),msg, 0);
			scenicEntity.setStatus(auditStatus);
			scenicEntity.setUpdatedDate(new Date());
			scenicMapper.updateByPrimaryKeyWithBLOBs(scenicEntity);
		} else {
			return ResponseMessage.validFailResponse().setMsg("景区信息不存在");
		}
		return ResponseMessage.defaultResponse();
	}

	private int saveAuditLog(int preStatus, int auditStatus, String principalId, String userName, String msg, int type){
		AuditLogEntity auditLogEntity = new AuditLogEntity();
		auditLogEntity.setId(UUIDUtils.getInstance().getId());
		auditLogEntity.setType(type);
		auditLogEntity.setPreStatus(preStatus);
		auditLogEntity.setStatus(auditStatus);
		auditLogEntity.setPrincipalId(principalId);
		auditLogEntity.setCreatedDate(new Date());
		auditLogEntity.setCreatedUser(userName);
		auditLogEntity.setOpinion(msg);
		return auditLogMapper.insert(auditLogEntity);
	}
}
