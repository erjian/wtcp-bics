/**
 * 该源代码文件 BusinessServiceImpl.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:53:22
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.BusinessEntity;
import cn.com.wanwei.bic.mapper.BusinessMapper;
import cn.com.wanwei.bic.service.BusinessService;
import cn.com.wanwei.common.model.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * wtcp-bics - BusinessServiceImpl 营业信息管理接口实现类
 */
@Service
@Slf4j
public class BusinessServiceImpl implements BusinessService {

	@Autowired
	private BusinessMapper businessMapper;

	@Override
	public ResponseMessage save(BusinessEntity record, String userName) {
		record.setCreatedUser(userName);
		record.setCreatedDate(new Date());
		businessMapper.insert(record);
		return ResponseMessage.defaultResponse().setMsg("保存成功");
	}

	@Override
	public BusinessEntity selectByPrimaryKey(Long id) {
		return businessMapper.selectByPrimaryKey(id);
	}

	@Override
	public ResponseMessage edit(Long id, BusinessEntity record, String userName) {
		BusinessEntity entity = businessMapper.selectByPrimaryKey(id);
		if(null == entity){
			return ResponseMessage.validFailResponse().setMsg("不存在该营业信息");
		}
		record.setId(id);
		record.setCreatedDate(entity.getCreatedDate());
		record.setCreatedUser(entity.getCreatedUser());
		record.setPrincipalId(entity.getPrincipalId());
		record.setUpdatedDate(new Date());
		record.setUpdatedUser(userName);
		businessMapper.updateByPrimaryKey(record);
		return ResponseMessage.defaultResponse().setMsg("更新成功");
	}
}