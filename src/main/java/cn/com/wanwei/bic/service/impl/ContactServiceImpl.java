/**
 * 该源代码文件 ContactServiceImpl.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:53:22
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.ContactEntity;
import cn.com.wanwei.bic.mapper.ContactMapper;
import cn.com.wanwei.bic.service.ContactService;
import cn.com.wanwei.common.model.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * wtcp-bics - ContactServiceImpl 通讯信息管理接口实现类
 */
@Service
@Slf4j
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactMapper contactMapper;

	@Override
	public ResponseMessage save(ContactEntity record, String userName) {
		record.setCreatedUser(userName);
		record.setCreatedDate(new Date());
		contactMapper.insert(record);
		return ResponseMessage.defaultResponse().setMsg("保存成功");
	}

	@Override
	public ContactEntity selectByPrimaryKey(Long id) {
		return contactMapper.selectByPrimaryKey(id);
	}

	@Override
	public ResponseMessage edit(Long id, ContactEntity record, String userName) {
		ContactEntity entity = contactMapper.selectByPrimaryKey(id);
		if(null == entity){
			return ResponseMessage.validFailResponse().setMsg("不存在该通讯信息");
		}
		record.setId(id);
		record.setCreatedDate(entity.getCreatedDate());
		record.setCreatedUser(entity.getCreatedUser());
		record.setPrincipalId(entity.getPrincipalId());
		record.setUpdatedDate(new Date());
		record.setUpdatedUser(userName);
		contactMapper.updateByPrimaryKey(record);
		return ResponseMessage.defaultResponse().setMsg("更新成功");
	}
}
