package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.bic.mapper.MaterialMapper;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.DateFormatUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public ResponseMessage deleteByPrimaryKey(Long id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        MaterialEntity materialEntity = materialMapper.selectByPrimaryKey(id);
        if (null != materialEntity) {
            materialMapper.deleteByPrimaryKey(id);
            responseMessage.setMsg("删除成功");
        } else {
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg("文件不存在");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage deleteByPrincipalIds(List<Long> ids) {
        materialMapper.deleteByPrincipalIds(ids);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ResponseMessage insert(MaterialEntity materialEntity, User user) {
        materialEntity.setCreatedUser(user.getUsername());
        materialEntity.setCreatedDate(new Date());
        materialMapper.insert(materialEntity);
        return ResponseMessage.defaultResponse().setMsg("添加成功");
    }

    @Override
    public ResponseMessage batchInsert(Long principalId, List<MaterialEntity> materialList, User user) {
        for(MaterialEntity item : materialList){
            item.setCreatedUser(user.getUsername());
            item.setCreatedDate(new Date());
            item.setPrincipalId(principalId);
        }
        materialMapper.batchInsert(materialList);
        return ResponseMessage.defaultResponse().setMsg("添加成功");
    }

    @Override
    public ResponseMessage selectByPrimaryKey(Long id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        MaterialEntity materialEntity = materialMapper.selectByPrimaryKey(id);
        if (null != materialEntity) {
            responseMessage.setData(materialEntity);
        } else {
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg("文件不存在");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage findByPrincipalId(Long principalId) {
        return ResponseMessage.defaultResponse().setData(materialMapper.findByPrincipalId(principalId));
    }

    @Override
    public ResponseMessage findByPidAndType(Long principalId, String type) {
        return ResponseMessage.defaultResponse().setData(materialMapper.findByPidAndType(principalId, type));
    }

    @Override
    public ResponseMessage findByPidAndIdentify(Long principalId, Integer fileIdentify) {
        return ResponseMessage.defaultResponse().setData(materialMapper.findByPidAndIdentify(principalId, fileIdentify));
    }

    @Override
    public ResponseMessage updateByPrimaryKey(Long id, MaterialEntity materialEntity, User user) {
        materialEntity.setUpdatedUser(user.getUsername());
        materialEntity.setUpdatedDate(new Date());
        materialMapper.updateByPrimaryKey(materialEntity);
        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }
}
