package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.DestinationRelationEntity;
import cn.com.wanwei.bic.mapper.DestinationRelationMapper;
import cn.com.wanwei.bic.service.DestinationRelationService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhanglei
 * @project wtcp-bics
 * @date 2019/10/11 11:38:38
 * @desc 目的地关联信息实现类.
 */
@Service
@Slf4j
@RefreshScope
public class DestinationRelationServiceImpl implements DestinationRelationService {

    @Autowired
    private DestinationRelationMapper destinationRelationMapper;

    /**
     * 根据目的地id查询关联信息
     * @param id
     * @param destinationRelationEntityClass
     * @return
     */
    @Override
    public ResponseMessage findPrincipalByDestinationId(String id, Class<DestinationRelationEntity> destinationRelationEntityClass) {
        try {
            List<DestinationRelationEntity> destList = destinationRelationMapper.findPrincipalByDestinationId(id);
            if(destList.size() > 0){
                return ResponseMessage.defaultResponse().setData(destList);
            }else{
                return ResponseMessage.validFailResponse().setMsg("暂无关联信息!");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("查询失败!");
        }
    }

    /**
     * 目的地关联信息新增
     * @param destinationRelationEntity
     * @param username
     * @return
     */
    @Override
    public ResponseMessage save(DestinationRelationEntity destinationRelationEntity, String username) {
        try {
            destinationRelationEntity.setId(UUIDUtils.getInstance().getId());
            destinationRelationEntity.setCreatedUser(username);
            destinationRelationEntity.setCreatedDate(new Date());
            destinationRelationMapper.insert(destinationRelationEntity);
            return ResponseMessage.defaultResponse().setMsg("保存成功!");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("保存失败!");
        }
    }
}
