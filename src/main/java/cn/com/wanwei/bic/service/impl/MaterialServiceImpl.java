package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.bic.mapper.MaterialMapper;
import cn.com.wanwei.bic.model.InfoType;
import cn.com.wanwei.bic.model.MaterialModel;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.utils.ParseContentUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private MaterialModel materialModel;

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
        materialEntity.setUpdatedDate(new Date());
        materialEntity.setFileName(dealFileName(materialEntity.getFileName()));
        materialMapper.insert(materialEntity);
        return ResponseMessage.defaultResponse().setMsg("添加成功");
    }

    @Override
    public ResponseMessage saveByDom(String content, String principalId, User user) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<MaterialEntity> fileList = ParseContentUtils.getInstance().parse(content, principalId, user);
        if (null != fileList && CollectionUtils.isNotEmpty(fileList)) {
            materialMapper.batchInsert(fileList);
            responseMessage.setMsg("保存成功");
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage batchInsert(String principalId, List<MaterialEntity> materialList, User user) {
        for (MaterialEntity item : materialList) {
            item.setCreatedUser(user.getUsername());
            item.setCreatedDate(new Date());
            item.setUpdatedDate(new Date());
            item.setPrincipalId(principalId);
            item.setFileName(dealFileName(item.getFileName()));
            if (StringUtils.isNotBlank(item.getId())) {
                materialMapper.deleteByPrimaryKey(item.getId());
            } else {
                item.setId(UUIDUtils.getInstance().getId());
            }
        }
        materialMapper.batchInsert(materialList);
        return ResponseMessage.defaultResponse().setMsg("添加成功");
    }

    private String dealFileName(String fileName) {
        if (StringUtils.isNotBlank(fileName) && fileName.length() > 100) {
            String[] nameArray = fileName.split("[.]");
            if (nameArray.length == 2 && nameArray[0].length() >= 20) {
                return nameArray[0].substring(0, 20) + "." + nameArray[1];
            }
        }
        return fileName;
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
        if (backList.size() > 0) {
            responseMessage.setData(backList);
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage findByPidAndType(String principalId, String type) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<MaterialEntity> backList = materialMapper.findByPidAndType(principalId, type);
        if (backList.size() > 0) {
            responseMessage.setData(backList);
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage findByPidAndIdentify(String principalId, String fileIdentify) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<MaterialEntity> backList = materialMapper.findByPidAndIdentify(principalId, fileIdentify);
        if (backList.size() > 0) {
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
    public ResponseMessage updateIdentify(String principalId, String id, String identify, User user) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        MaterialEntity entity = materialMapper.findByIdAndPid(id, principalId);
        if (null == entity) {
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg("资源不存在");
        } else {
            entity.setFileIdentify(identify);
            entity.setUpdatedUser(user.getUsername());
            entity.setUpdatedDate(new Date());
            materialMapper.updateByPrimaryKey(entity);
            responseMessage.setMsg("设置成功");
        }
        return responseMessage;
    }

//    @Override
//    public Map<String, Object> handleMaterial(String principalId) {
//        List<MaterialEntity> list = materialMapper.findByPrincipalId(principalId);
//        Map<String, Object> materialList = Maps.newHashMap();
//        List<MaterialEntity> imageList = Lists.newArrayList();
//        List<MaterialEntity> audioList = Lists.newArrayList();
//        List<MaterialEntity> videoList = Lists.newArrayList();
//        List<MaterialEntity> fileList = Lists.newArrayList();
//        List<MaterialEntity> titleImageList = Lists.newArrayList();
//        List<MaterialEntity> spotImageList = Lists.newArrayList();
//        if (CollectionUtils.isNotEmpty(list)) {
//            for (MaterialEntity entity : list) {
//                if (entity.getFileType().toLowerCase().equals("image")) {
//                    imageList.add(entity);
//                }
//                if (entity.getFileType().toLowerCase().equals("audio")) {
//                    audioList.add(entity);
//                }
//                if (entity.getFileType().toLowerCase().equals("video")) {
//                    videoList.add(entity);
//                }
//                if (entity.getFileType().toLowerCase().equals("file")) {
//                    fileList.add(entity);
//                }
//                if (null != entity.getFileIdentify() && entity.getFileIdentify() == "1") {
//                    titleImageList.add(entity);
//                }
//                if (null != entity.getFileIdentify() && entity.getFileIdentify() == "2") {
//                    spotImageList.add(entity);
//                }
//                if (null != entity.getFileIdentify() && entity.getFileIdentify() == "3") {
//                    titleImageList.add(entity);
//                    spotImageList.add(entity);
//                }
//            }
//        }
//        materialList.put("image", imageList);
//        materialList.put("audio", audioList);
//        materialList.put("video", videoList);
//        materialList.put("file", fileList);
//        materialList.put("titleImage", titleImageList);
//        materialList.put("spotImage", spotImageList);
//        return materialList;
//    }

    @Override
    public ResponseMessage findByIds(String ids, Integer parameter) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<String> idsList = Arrays.asList(ids.split(","));
        if (parameter == 1) {
            List<MaterialEntity> list = materialMapper.findByIds(idsList);
            responseMessage.setData(list);
        } else if (parameter == 2) {
            Map<String, Object> map = this.findByIdsEach(idsList);
            responseMessage.setData(map);
        } else {
            responseMessage.setData("暂无数据");
        }
        return responseMessage;
    }

    public Map<String, Object> findByIdsEach(List<String> ids) {
        Map<String, Object> map = new HashMap<>();
        for (String id : ids) {
            List<MaterialEntity> list = materialMapper.findByPrincipalId(id);
            map.put(id, list);
        }
        return map;
    }

    @Override
    public Map<String, Object> handleMaterialNew(String principalId) {
        List<MaterialEntity> list = materialMapper.findByPrincipalId(principalId);
        Map<String, Object> materialList = Maps.newHashMap();
        Map<String, Object> imageList = getMaterialTypeMap("image");
        Map<String, Object> audioList = getMaterialTypeMap("audio");
        Map<String, Object> videoList = getMaterialTypeMap("video");
        Map<String, Object> fileList = getMaterialTypeMap("file");
        materialList.put("image", imageList);
        materialList.put("audio", audioList);
        materialList.put("video", videoList);
        materialList.put("file", fileList);
        if (CollectionUtils.isNotEmpty(list)) {
            for (MaterialEntity entity : list) {
                if (entity.getFileType().toLowerCase().equals("image")) {
                    getMaterialData((Map<String, Object>) materialList.get("image"), entity);
                }
                if (entity.getFileType().toLowerCase().equals("audio")) {
                    getMaterialData((Map<String, Object>) materialList.get("audio"), entity);
                }
                if (entity.getFileType().toLowerCase().equals("video")) {
                    getMaterialData((Map<String, Object>) materialList.get("video"), entity);
                }
                if (entity.getFileType().toLowerCase().equals("file")) {
                    getMaterialData((Map<String, Object>) materialList.get("file"), entity);
                }
            }
        }
        return materialList;
    }

    private Map<String, Object> getMaterialTypeMap(String name) {
        //获取该文件类型下标识集合
        List<MaterialEntity> materialList = Lists.newArrayList();
        Map<String, Object> codeMap = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "全部");
        map.put("materials", materialList);
        codeMap.put("all", map);
        for (InfoType infoType : (List<InfoType>) getMaterialType().get(name)) {
            materialList = Lists.newArrayList();
            map = new HashMap<>();
            map.put("name", infoType.getName());
            map.put("materials", materialList);
            codeMap.put(infoType.getCode(), map);
        }
        return codeMap;
    }

    private Map<String, Object> getMaterialData(Map<String, Object> typeMap, MaterialEntity entity) {
        //跟据标识分配文件
        Map<String, Object> all = (Map<String, Object>) typeMap.get("all");
        List<MaterialEntity> materialList = (List<MaterialEntity>) all.get("materials");
        materialList.add(entity);
        if (!Strings.isNullOrEmpty(entity.getFileIdentify())) {
            String[] identifyArr = entity.getFileIdentify().split(",");
            for (String str : identifyArr) {
                Map<String, Object> identMap = (Map<String, Object>) typeMap.get(str);
                if (identMap != null && identMap.containsKey("materials")) {
                    materialList = (List<MaterialEntity>) identMap.get("materials");
                    materialList.add(entity);
                }
            }
        }
        return typeMap;
    }

    @Override
    public Map<String, Object> getMaterialType() {
        Map<String, Object> map = new HashMap<>();
        map.put("image", materialModel.getImage().get("info-type"));
        map.put("video", materialModel.getVideo().get("info-type"));
        map.put("audio", materialModel.getAudio().get("info-type"));
        map.put("file", materialModel.getFile().get("info-type"));
        return map;
    }
}
