/**
 * 该源代码文件 EnterpriseServiceImpl.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:53:22
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.BusinessEntity;
import cn.com.wanwei.bic.entity.EnterpriseEntity;
import cn.com.wanwei.bic.mapper.BusinessMapper;
import cn.com.wanwei.bic.mapper.EnterpriseMapper;
import cn.com.wanwei.bic.service.BusinessService;
import cn.com.wanwei.bic.service.EnterpriseService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * wtcp-bics - EnterpriseServiceImpl 企业信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class EnterpriseServiceImpl implements EnterpriseService {

	@Autowired
	private EnterpriseMapper enterpriseMapper;

	@Override
	public ResponseMessage save(EnterpriseEntity record, String userName) {
		record.setId(UUIDUtils.getInstance().getId());
		record.setCreatedUser(userName);
		record.setCreatedDate(new Date());
		enterpriseMapper.insert(record);
		return ResponseMessage.defaultResponse().setMsg("保存成功");
	}

	@Override
	public EnterpriseEntity selectByPrimaryKey(String id) {
		return enterpriseMapper.selectByPrimaryKey(id);
	}

	@Override
	public ResponseMessage edit(String id, EnterpriseEntity record, String userName) {
		EnterpriseEntity entity = enterpriseMapper.selectByPrimaryKey(id);
		if(null == entity){
			return ResponseMessage.validFailResponse().setMsg("不存在该企业信息");
		}
		record.setId(id);
		record.setCreatedDate(entity.getCreatedDate());
		record.setCreatedUser(entity.getCreatedUser());
		record.setPrincipalId(entity.getPrincipalId());
		record.setUpdatedDate(new Date());
		record.setUpdatedUser(userName);
		enterpriseMapper.updateByPrimaryKey(record);
		return ResponseMessage.defaultResponse().setMsg("更新成功");
	}
}
