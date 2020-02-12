package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.ScenicEntity;
import cn.com.wanwei.bic.entity.ScenicTagsEntity;
import cn.com.wanwei.bic.entity.TrafficAgentEntity;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.DataSyncMapper;
import cn.com.wanwei.bic.model.DataSyncModel;
import cn.com.wanwei.bic.service.DataSyncService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
import com.github.pagehelper.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DataSyncServiceImpl implements DataSyncService {

    @Autowired
    private DataSyncMapper dataSyncMapper;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Override
    public ResponseMessage findTravelByPage(String category, Integer page, Integer size, Map<String, Object> filter) {
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "updated_date");
        Page<DataSyncModel> dataSyncModels = dataSyncMapper.findTravelByPage(filter);
        return addTagsAndFiles(category,dataSyncModels, pageRequest);
    }

    private ResponseMessage addTagsAndFiles(String category, Page<DataSyncModel> dataSyncModels,MybatisPageRequest pageRequest){
        for(DataSyncModel item : dataSyncModels){
            item.setCategory(category);
            item.setTagsList(tagsService.findListByPriId(item.getId(), TrafficAgentEntity.class));
            item.setFileList(materialService.handleMaterialNew(item.getId()));
        }
        PageInfo<DataSyncModel> pageInfo = new PageInfo<>(dataSyncModels, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }


}
