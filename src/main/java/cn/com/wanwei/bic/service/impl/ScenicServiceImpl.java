/**
 * 该源代码文件 ScenicServiceImpl.java 是工程“wtcp-bics”的一部分。
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:53:22
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.mapper.ScenicMapper;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * wtcp-bics - ScenicServiceImpl 景区基础信息管理接口实现类
 */
@Service
@Slf4j
public class ScenicServiceImpl implements ScenicService {

	@Autowired
	private ScenicMapper scenicMapper;

	@Override
	public ResponseMessage save(ScenicEntity record, String userName) {
		scenicMapper.insert(record);
		return ResponseMessage.defaultResponse().setMsg("保存成功");
	}

	@Override
	public ResponseMessage deleteByPrimaryKey(Long id) {
		scenicMapper.deleteByPrimaryKey(id);
		return ResponseMessage.defaultResponse().setMsg("删除成功");
	}

	@Override
	public ScenicEntity selectByPrimaryKey(Long id) {
		return scenicMapper.selectByPrimaryKey(id);
	}

	@Override
	public ResponseMessage edit(Long id, ScenicEntity record, String userName) {
		scenicMapper.updateByPrimaryKeyWithBLOBs(record);
		return ResponseMessage.defaultResponse().setMsg("更新成功");
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
}
