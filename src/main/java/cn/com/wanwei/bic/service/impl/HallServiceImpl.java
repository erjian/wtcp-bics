package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.HallEntity;
import cn.com.wanwei.bic.entity.TravelAgentEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.HallMapper;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.HallService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.PinyinUtils;
import cn.com.wanwei.mybatis.service.impl.BaseServiceImpl;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class HallServiceImpl extends BaseServiceImpl<HallMapper, HallEntity, String> implements HallService {

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private CommonService commonService;

    @Autowired
    private MaterialService materialService;


    @Override
    public ResponseMessage save(EntityTagsModel<HallEntity> hallModel, User user, Long ruleId, Integer appCode) {
        HallEntity hallEntity = hallModel.getEntity();
        String id = UUIDUtils.getInstance().getId();
        hallEntity.setId(id);
        ResponseMessage result = coderServiceFeign.buildSerialByCode(ruleId, appCode, "TS");
        hallEntity.setCode(result.getData().toString());
        hallEntity.setFullSpell(PinyinUtils.getPingYin(hallEntity.getTitle()).toLowerCase());
        hallEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(hallEntity.getTitle()).toLowerCase());
        hallEntity.setCreatedUser(user.getUsername());
        hallEntity.setCreatedDate(new Date());
        hallEntity.setUpdatedDate(new Date());
        hallEntity.setDeptCode(user.getOrg().getCode());
        hallEntity.setStatus(1);
        hallEntity.setWeight(0);
        hallMapper.insert(hallEntity);

        //处理页面新增素材
        if (CollectionUtils.isNotEmpty(hallModel.getMaterialList())) {
            materialService.batchInsert(id, hallModel.getMaterialList(), user);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HallEntity> hallModel, User user) {
        Optional<HallEntity> optional = hallMapper.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.validFailResponse().setMsg("厅室信息不存在");
        }
        HallEntity entity = optional.get();
        HallEntity record = hallModel.getEntity();
        record.setId(id);
        record.setCode(entity.getCode());
        record.setCreatedDate(entity.getCreatedDate());
        record.setCreatedUser(entity.getCreatedUser());
        record.setDeptCode(entity.getDeptCode());
        record.setFullSpell(PinyinUtils.getPingYin(record.getTitle()).toLowerCase());
        record.setSimpleSpell(PinyinUtils.converterToFirstSpell(record.getTitle()).toLowerCase());
        record.setStatus(1);
        record.setUpdatedDate(new Date());
        record.setUpdatedUser(user.getUsername());
        hallMapper.updateById(record);
        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage updateOnlineStatus(String id, Integer status, String username) {
        Optional<HallEntity> optional = hallMapper.findById(id);
        if (!optional.isPresent()) {
            return ResponseMessage.validFailResponse().setMsg("厅室信息不存在");
        }
        HallEntity entity = optional.get();
        entity.setUpdatedUser(username);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        hallMapper.updateById(entity);
        // 添加上下线操作记录
        String msg = status == 9 ? "上线成功" : "下线成功";
        commonService.saveAuditLog(entity.getStatus(), status, id, username, msg, 1);
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public long countByVenueId(String venueId) {
        return hallMapper.countByVenueId(venueId);
    }

    @Override
    public ResponseMessage findBySearchValue(String name, String ids) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<Map<String, Object>> data = new ArrayList<>();
        List<String> idList = null;
        if (StringUtil.isNotEmpty(ids)) {
            idList = Arrays.asList(ids.split(","));
        }
        List<HallEntity> list = hallMapper.findBySearchValue(name, idList);
        if (!list.isEmpty()) {
            for (HallEntity entity : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", entity.getId());
                map.put("unicode", entity.getCode());
                map.put("name", entity.getTitle());
                map.put("level", null);
                map.put("longitude", entity.getLongitude());
                map.put("latitude", entity.getLatitude());
                map.put("areaCode", null);
                map.put("areaName", null);
                map.put("address", null);
                map.put("pinyin", entity.getSimpleSpell());
                map.put("pinyinqp", entity.getFullSpell());
                data.add(map);
            }
            responseMessage.setData(data);
        } else {
            responseMessage.setData("暂无数据");
        }
        return responseMessage;
    }
}
