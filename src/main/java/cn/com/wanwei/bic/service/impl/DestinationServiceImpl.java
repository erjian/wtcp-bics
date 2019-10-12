package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.AuditLogEntity;
import cn.com.wanwei.bic.entity.DestinationEntity;
import cn.com.wanwei.bic.mapper.DestinationMapper;
import cn.com.wanwei.bic.service.DestinationService;
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
 * @date 2019/10/10 9:30:30
 * @desc wtcp-bics - DestinationServiceImpl 目的地基础信息管理接口实现类.
 */
@Service
@Slf4j
@RefreshScope
public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DestinationMapper destinationMapper;

    @Autowired
    AuditLogServiceImpl auditLogService;

    /**
     * 查询目的地分页列表数据
     * @param page  页数
     * @param size  条数
     * @param user  用户信息
     * @param filter   查询参数
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage findByPage(Integer page, Integer size, User user, Map<String, Object> filter) throws Exception {
        Sort.Order[] order = new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "created_date"),
                new Sort.Order(Sort.Direction.DESC, "updated_date")};
        Sort sort = Sort.by(order);
        MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
        PageHelper.startPage(pageRequest.getPage(), pageRequest.getSize());
        PageHelper.orderBy(pageRequest.getOrders());
        Page<DestinationEntity> userEntities = destinationMapper.findByPage(filter);
        PageInfo<DestinationEntity> pageInfo = new PageInfo<>(userEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    /**
     * 目的地基础信息新增
     * @param destinationEntity  目的地基础信息实体
     * @param username  用户名
     * @return
     */
    @Override
    public ResponseMessage save(DestinationEntity destinationEntity, String username) throws Exception{
        try{
            destinationEntity.setId(UUIDUtils.getInstance().getId());
            destinationEntity.setCreatedUser(username);
            destinationEntity.setCreatedDate(new Date());
            destinationEntity.setStatus(0);
            destinationMapper.insert(destinationEntity);
            return ResponseMessage.defaultResponse().setMsg("保存成功!");
        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("保存失败!");
        }

    }

    /**
     *
     * @param id  主键ID
     * @param destinationEntity  目的地基础信息实体
     * @param username  用户名
     * @return
     */
    @Override
    public ResponseMessage edit(String id, DestinationEntity destinationEntity, String username)throws Exception {
        DestinationEntity entity = destinationMapper.selectByPrimaryKey(id);
        if(null == entity){
            return ResponseMessage.validFailResponse().setMsg("不存在该目的地！");
        }
        destinationEntity.setId(id);
        destinationEntity.setCreatedDate(entity.getCreatedDate());
        destinationEntity.setCreatedUser(entity.getCreatedUser());
        destinationEntity.setStatus(0);  //编辑修改状态为--> 0:待审
        destinationEntity.setUpdatedDate(new Date());
        destinationEntity.setUpdatedUser(username);
        destinationMapper.updateByPrimaryKey(destinationEntity);
        return ResponseMessage.defaultResponse().setMsg("更新成功!");
    }

    /**
     * 根据目的地id查询目的地信息
     * @param id   主键id
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage selectByPrimaryKey(String id) throws Exception{
        DestinationEntity destinationEntity = destinationMapper.selectByPrimaryKey(id);
        if(destinationEntity==null){
            return ResponseMessage.validFailResponse().setMsg("暂无该目的地信息！");
        }
        return ResponseMessage.defaultResponse().setData(destinationEntity);
    }

    /**
     * 删除目的地信息
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ResponseMessage deleteByPrimaryKey(String id) throws Exception {
        destinationMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功!");
    }

    /**
     * 目的地权重修改
     * @param id  主键ID
     * @param weightNum   权重
     * @param username
     * @return
     */
    @Override
    public ResponseMessage changeWeight(String id, Float weightNum, String username) throws Exception{
        DestinationEntity destinationEntity = destinationMapper.selectByPrimaryKey(id);
        if(null == destinationEntity){
            return ResponseMessage.validFailResponse().setMsg("无目的地信息！");
        }
        destinationEntity.setUpdatedUser(username);
        destinationEntity.setUpdatedDate(new Date());
        destinationEntity.setWeight(weightNum);
        destinationMapper.updateByPrimaryKey(destinationEntity);
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    /**
     * 目的地信息审核/上线
     * @param auditLogEntity   审核/上线实体类
     * @param username
     * @return
     */
    @Override
    public ResponseMessage changeStatus(AuditLogEntity auditLogEntity, String username, int type) throws Exception{
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        DestinationEntity destinationEntity = destinationMapper.selectByPrimaryKey(auditLogEntity.getPrincipalId());
        if(null == destinationEntity){
            return ResponseMessage.validFailResponse().setMsg("无目的地信息！");
        }
        if(type == 1 ){
            //上线
            if(destinationEntity.getStatus() == 0 || destinationEntity.getStatus() == 2){
                return ResponseMessage.validFailResponse().setMsg("该信息未审核通过,不可上下线操作!");
            }else{
                if(auditLogEntity.getStatus() == 9){
                    responseMessage.setMsg("上线成功!");
                }else{
                    responseMessage.setMsg("下线成功!");
                }
            }
        }else{
            responseMessage.setMsg("审核成功!");
        }
        destinationEntity.setId(auditLogEntity.getPrincipalId());
        destinationEntity.setStatus(auditLogEntity.getStatus());
        destinationEntity.setUpdatedUser(username);
        destinationEntity.setUpdatedDate(new Date());
        destinationMapper.updateByPrimaryKey(destinationEntity);
        // 记录审核/上线流水操作
        auditLogService.create(auditLogEntity,username);
        return responseMessage;
    }

}
