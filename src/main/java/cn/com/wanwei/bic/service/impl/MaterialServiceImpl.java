package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.bic.mapper.MaterialMapper;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.utils.ParseContentUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public ResponseMessage deleteByPrimaryKey(String id) {
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
    public ResponseMessage deleteOneByPidAndId(String principalId, String id) {
        materialMapper.deleteOneByPidAndId(principalId, id);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ResponseMessage deleteByPrincipalIds(List<String> ids) {
        materialMapper.deleteByPrincipalIds(ids);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ResponseMessage deleteByPrincipalId(String principalId) {
        materialMapper.deleteByPrincipalId(principalId);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ResponseMessage insert(MaterialEntity materialEntity, User user) {
        materialEntity.setId(UUIDUtils.getInstance().getId());
        materialEntity.setCreatedUser(user.getUsername());
        materialEntity.setCreatedDate(new Date());
        if(StringUtils.isNotEmpty(materialEntity.getFileName()) && materialEntity.getFileName().length() > 100){
            String[] nameArray = materialEntity.getFileName().split(",");
            String fileName = nameArray[0].substring(0, 20) + nameArray[1];
            materialEntity.setFileName(fileName);
        }
        materialMapper.insert(materialEntity);
        return ResponseMessage.defaultResponse().setMsg("添加成功");
    }

    @Override
    public ResponseMessage saveByDom(String content, String principalId, User user) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<MaterialEntity> fileList = ParseContentUtils.getInstance().parse(content,principalId, user);
        if(null != fileList && CollectionUtils.isNotEmpty(fileList)){
            materialMapper.batchInsert(fileList);
            responseMessage.setMsg("保存成功");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage batchInsert(String principalId, List<MaterialEntity> materialList, User user) {
        for(MaterialEntity item : materialList){
            item.setId(UUIDUtils.getInstance().getId());
            item.setCreatedUser(user.getUsername());
            item.setCreatedDate(new Date());
            item.setPrincipalId(principalId);
            if(StringUtils.isNotEmpty(item.getFileName()) && item.getFileName().length() > 100){
                String[] nameArray = item.getFileName().split(",");
                String fileName = nameArray[0].substring(0, 20) + nameArray[1];
                item.setFileName(fileName);
            }
        }
        materialMapper.batchInsert(materialList);
        return ResponseMessage.defaultResponse().setMsg("添加成功");
    }

    @Override
    public ResponseMessage selectByPrimaryKey(String id) {
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
    public ResponseMessage findByPrincipalId(String principalId) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<MaterialEntity> backList = materialMapper.findByPrincipalId(principalId);
        if(backList.size() > 0){
            responseMessage.setData(backList);
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage findByPidAndType(String principalId, String type) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<MaterialEntity> backList = materialMapper.findByPidAndType(principalId, type);
        if(backList.size() > 0){
            responseMessage.setData(backList);
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage findByPidAndIdentify(String principalId, Integer fileIdentify) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<MaterialEntity> backList = materialMapper.findByPidAndIdentify(principalId, fileIdentify);
        if(backList.size() > 0){
            responseMessage.setData(backList);
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage updateByPrimaryKey(String id, MaterialEntity materialEntity, User user) {
        materialEntity.setUpdatedUser(user.getUsername());
        materialEntity.setUpdatedDate(new Date());
        materialMapper.updateByPrimaryKey(materialEntity);
        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage updateIdentify(String principalId, String id, Integer identify, User user) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        // 查询数据是否存在
        MaterialEntity entity = materialMapper.findByIdAndPid(id, principalId);
        if(null == entity){
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg("资源不存在");
        }else{
            // 先获取当前资源在改标识下的数据
            List<MaterialEntity> oldList = materialMapper.findByPidAndIdentify(principalId, identify);
//            if(null != oldList){
//                for(MaterialEntity item : oldList){
//                    item.setFileIdentify(0);
//                    item.setUpdatedUser(user.getUsername());
//                    item.setUpdatedDate(new Date());
//                    materialMapper.updateByPrimaryKey(item);
//                }
//            }
            // 更新数据
            entity.setFileIdentify(identify);
            entity.setUpdatedUser(user.getUsername());
            entity.setUpdatedDate(new Date());
            materialMapper.updateByPrimaryKey(entity);
            responseMessage.setMsg("设置成功");
        }
        return responseMessage;
    }

    @Override
    public Map<String, Object> handleMaterial(String principalId) {
        List<MaterialEntity> list = materialMapper.findByPrincipalId(principalId);
        Map<String, Object> materialList = Maps.newHashMap();
        List<MaterialEntity> imageList = Lists.newArrayList();
        List<MaterialEntity> audioList = Lists.newArrayList();
        List<MaterialEntity> videoList = Lists.newArrayList();
        List<MaterialEntity> fileList = Lists.newArrayList();
        List<MaterialEntity> titleImageList = Lists.newArrayList();
        List<MaterialEntity> spotImageList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(list)){
            for(MaterialEntity entity:list){
                if(entity.getFileType().toLowerCase().equals("image")){
                    imageList.add(entity);
                }
                if(entity.getFileType().toLowerCase().equals("audio")){
                    audioList.add(entity);
                }
                if(entity.getFileType().toLowerCase().equals("video")){
                    videoList.add(entity);
                }
                if(entity.getFileType().toLowerCase().equals("file")){
                    fileList.add(entity);
                }
                if(null != entity.getFileIdentify() && entity.getFileIdentify() == 1){
                    titleImageList.add(entity);
                }
                if(null != entity.getFileIdentify() && entity.getFileIdentify() == 2){
                    spotImageList.add(entity);
                }
                if(null != entity.getFileIdentify() && entity.getFileIdentify() == 3){
                    titleImageList.add(entity);
                    spotImageList.add(entity);
                }
            }
        }
        materialList.put("image", imageList);
        materialList.put("audio", audioList);
        materialList.put("video", videoList);
        materialList.put("file", fileList);
        materialList.put("titleImage", titleImageList);
        materialList.put("spotImage", spotImageList);
        return materialList;
    }
}
