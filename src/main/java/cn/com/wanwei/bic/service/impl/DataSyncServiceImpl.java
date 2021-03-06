package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.*;
import cn.com.wanwei.bic.mapper.DataSyncMapper;
import cn.com.wanwei.bic.model.DataSyncModel;
import cn.com.wanwei.bic.model.DataType;
import cn.com.wanwei.bic.service.DataSyncService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataSyncServiceImpl implements DataSyncService {

    @Autowired
    private DataSyncMapper dataSyncMapper;

    @Autowired
    private TagsService tagsService;

    @Autowired
    private MaterialService materialService;

    @Override
    public ResponseMessage findByPage(String category, Integer page, Integer size, Map<String, Object> filter) {
        try {
            MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "updated_date");
            Page<DataSyncModel> dataSyncModels = new Page<>();
            Class clazz = null;
            if (category.equals(DataType.SCENIC_TYPE.getKey()) || category.equals(DataType.TOUR_VILLAGE_TYPE.getKey())) {
                dataSyncModels = dataSyncMapper.findScenicByPage(filter);
                clazz = ScenicTagsEntity.class;
            } else if (category.equals(DataType.TRAVEL_TYPE.getKey())) {
                dataSyncModels = dataSyncMapper.findTravelByPage(filter);
                clazz = TravelAgentTagsEntity.class;
            } else if (category.equals(DataType.PERIPHERY_FOOD_TYPE.getKey())
                    || category.equals(DataType.SHOPPING_TYPE.getKey())
                    || category.equals(DataType.FOOD_STREET.getKey())
                    || category.equals(DataType.SPECIAL_SNACKS.getKey())
                    || category.equals(DataType.SPECIALTY.getKey())) {
                dataSyncModels = dataSyncMapper.findPeripheryByPage(filter);
                clazz = PeripheryTagsEntity.class;
            } else if (category.equals(DataType.AGRITAINMENT_TYPE.getKey())) {
                dataSyncModels = dataSyncMapper.findEntertainmentByPage(filter);
                clazz = EntertainmentTagsEntity.class;
            } else if (category.equals(DataType.RENTAL_CAR_TYPE.getKey())) {
                dataSyncModels = dataSyncMapper.findRentalCarByPage(filter);
                clazz = RentalCarTagsEntity.class;
            } else if (category.equals(DataType.TRAFFIC_AGENT_TYPE.getKey())) {
                dataSyncModels = dataSyncMapper.findTrafficAgentByPage(filter);
                clazz = null;
            } else if (category.equals(DataType.DRIVE_CAMP_TYPE.getKey())) {
                dataSyncModels = dataSyncMapper.findDriveCampByPage(filter);
                clazz = DriveCampTagsEntity.class;
            }
            return addTagsAndFiles(category, dataSyncModels, pageRequest, clazz);
        } catch (Exception e){
            log.info(e.getMessage());
            return ResponseMessage.validFailResponse();
        }
    }

    private ResponseMessage addTagsAndFiles(String category, Page<DataSyncModel> dataSyncModels,
                                            MybatisPageRequest pageRequest, Class clazz) throws Exception {
        for (DataSyncModel item : dataSyncModels) {
            item.setCategory(category);
            item.setTagsList(clazz == null ? Lists.newArrayList() : tagsService.findListByPriId(item.getId(), clazz));
            item.setFileList(materialService.handleMaterialNew(item.getId()));
        }
        PageInfo<DataSyncModel> pageInfo = new PageInfo<>(dataSyncModels, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }


}
