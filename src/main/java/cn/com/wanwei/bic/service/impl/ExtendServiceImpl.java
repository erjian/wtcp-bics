package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.ExtendEntity;
import cn.com.wanwei.bic.mapper.ExtendMapper;
import cn.com.wanwei.bic.service.ExtendService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/18 10:33:33
 * @desc wtcp-bics - ExtendServiceImpl 扩展信息管理业务层实现类
 */
@Service
@Slf4j
@RefreshScope
public class ExtendServiceImpl implements ExtendService {

    @Autowired
    private ExtendMapper extendMapper;

    @Autowired
    private AuditLogServiceImpl auditLogService;

    /**
     * 扩展信息管理分页列表
     * @param page
     * @param size
     * @param currentUser
     * @param filter
     * @return
     */
    @Override
    public ResponseMessage findByPage(Integer page, Integer size, User currentUser, Map<String, Object> filter) throws Exception{
        Sort.Order[] order = new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "created_date"),
                new Sort.Order(Sort.Direction.DESC, "updated_date")};
        Sort sort = Sort.by(order);
        MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        PageHelper.orderBy(pageRequest.getOrders());
        //查询扩展信息列表数据
        Page<ExtendEntity> userEntities = extendMapper.findByPage(filter);
        PageInfo<ExtendEntity> pageInfo = new PageInfo<>(userEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    /**
     * 扩展信息新增
     * @param extendEntity
     * @param username
     * @return
     */
    @Override
    public ResponseMessage save(ExtendEntity extendEntity, String username) throws Exception{
        extendEntity.setId(UUIDUtils.getInstance().getId());
        extendEntity.setCreatedUser(username);
        extendEntity.setCreatedDate(new Date());
        extendEntity.setStatus(1);
        extendMapper.insert(extendEntity);
        return ResponseMessage.defaultResponse().setMsg("新增成功!");
    }

    /**
     * 扩展信息编辑
     * @param id
     * @param extendEntity
     * @param username
     * @return
     */
    @Override
    public ResponseMessage edit(String id, ExtendEntity extendEntity, String username) throws Exception{
        ExtendEntity entity = extendMapper.selectByPrimaryKey(id);
        if(null == entity){
            return ResponseMessage.validFailResponse().setMsg("不存在扩展信息");
        }
        extendEntity.setId(id);
        extendEntity.setCreatedDate(entity.getCreatedDate());
        extendEntity.setCreatedUser(entity.getCreatedUser());
        extendEntity.setStatus(1);  //编辑修改状态为--> 1: 下线
        extendEntity.setUpdatedDate(new Date());
        extendEntity.setUpdatedUser(username);
        extendMapper.updateByPrimaryKey(extendEntity);
        return ResponseMessage.defaultResponse().setMsg("更新成功!");
    }

    /**
     * 根据扩展信息id查询扩展信息详情
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage selectByPrimaryKey(String id) throws Exception{
        ExtendEntity extendEntity = extendMapper.selectByPrimaryKey(id);
        if(extendEntity == null){
            return ResponseMessage.validFailResponse().setMsg("暂无该扩展信息！");
        }
        return ResponseMessage.defaultResponse().setData(extendEntity);
    }

    /**
     * 扩展信息上线
     * @param id
     * @param username
     * @param type
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage auditOrIssue(String id, String username, int type) throws Exception {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        ExtendEntity extendEntity = extendMapper.selectByPrimaryKey(id);
        if(extendEntity == null){
            return ResponseMessage.validFailResponse().setMsg("无扩展信息!");
        }
        auditLogEntity.setPrincipalId(id);
        auditLogEntity.setType(type);
        //更新扩展信息状态
        extendEntity.setId(id);
        extendEntity.setUpdatedUser(username);
        extendEntity.setUpdatedDate(new Date());
        if(type == 1 ){
            if(extendEntity.getStatus() == 1){
                responseMessage.setMsg("上线成功!");
                extendEntity.setStatus(9);
                auditLogEntity.setPreStatus(1);
                auditLogEntity.setStatus(extendEntity.getStatus());
            }else{
                responseMessage.setMsg("下线成功!");
                extendEntity.setStatus(1);
                auditLogEntity.setPreStatus(9);
                auditLogEntity.setStatus(extendEntity.getStatus());
            }
        }
        extendMapper.updateByPrimaryKey(extendEntity);
        // 记录审核/上线流水操作
        auditLogService.create(auditLogEntity,username);
        return responseMessage;
    }

    /**
     * 根据扩展信息id删除扩展信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage deleteByPrimaryKey(String id) throws Exception {
        extendMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功!");
    }

    /**
     * 扩展信息权重修改
     * @param id  扩展信息id
     * @param weight  权重
     * @param username
     * @return
     */
    @Override
    public ResponseMessage changeWeight(String id, Float weight, String username) throws Exception{
        ExtendEntity extendEntity = extendMapper.selectByPrimaryKey(id);
        if(extendEntity == null){
            return ResponseMessage.validFailResponse().setMsg("无扩展信息！");
        }
        extendEntity.setId(id);
        extendEntity.setUpdatedUser(username);
        extendEntity.setUpdatedDate(new Date());
        extendEntity.setWeight(weight);
        extendMapper.updateByPrimaryKey(extendEntity);
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }
}
