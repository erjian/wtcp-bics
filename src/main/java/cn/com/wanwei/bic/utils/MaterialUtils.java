package cn.com.wanwei.bic.utils;

import cn.com.wanwei.bic.entity.MaterialEntity;
import cn.com.wanwei.bic.mapper.MaterialMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class MaterialUtils {

    private static MaterialUtils instance = new MaterialUtils();

    private MaterialUtils() {
    }

    public static MaterialUtils getInstance() {
        return instance;
    }

    @Autowired
    private MaterialMapper materialMapper;

    public Map<String, Object> handleMaterial(String principalId){
        List<MaterialEntity> list = materialMapper.findByPrincipalId(principalId);
        Map<String, Object> materialList = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(list)){
            List<MaterialEntity> imageList = Lists.newArrayList();
            List<MaterialEntity> audioList = Lists.newArrayList();
            List<MaterialEntity> videoList = Lists.newArrayList();
            List<MaterialEntity> fileList = Lists.newArrayList();
            List<MaterialEntity> titleImageList = Lists.newArrayList();
            List<MaterialEntity> spotImageList = Lists.newArrayList();
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
            materialList.put("image", imageList);
            materialList.put("audio", audioList);
            materialList.put("video", videoList);
            materialList.put("file", fileList);
            materialList.put("titleImage", titleImageList);
            materialList.put("spotImage", spotImageList);
        }
        return materialList;
    }
}
