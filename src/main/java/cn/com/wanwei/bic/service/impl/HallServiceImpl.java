package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.HallEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.HallMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.HallService;
import cn.com.wanwei.bic.service.MaterialService;
import cn.com.wanwei.bic.utils.PageUtils;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.common.utils.ExceptionUtils;
import cn.com.wanwei.common.utils.PinyinUtils;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import cn.com.wanwei.persistence.mybatis.utils.EscapeCharUtils;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class HallServiceImpl implements HallService {

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private CommonService commonService;

    @Autowired
    private MaterialService materialService;

    @Override
    public ResponseMessage deleteByPrimaryKey(String id) {
        hallMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ResponseMessage insert(EntityTagsModel<HallEntity> hallModel, User user, Long ruleId, Integer appCode) {
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
    public HallEntity selectByPrimaryKey(String id) {
        return hallMapper.selectByPrimaryKey(id);
    }

    @Override
    public ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HallEntity> hallModel, User user) {
        HallEntity entity = hallMapper.selectByPrimaryKey(id);
        HallEntity record = hallModel.getEntity();
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("厅室信息不存在");
        }
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
        hallMapper.updateByPrimaryKey(record);
        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        return getPageInfo(page, size, filter, null);
    }

    @Override
    public ResponseMessage findByPageForFeign(Integer page, Integer size, Map<String, Object> filter) {
        return getPageInfo(page, size, filter, "feign");
    }

    private ResponseMessage getPageInfo(Integer page, Integer size, Map<String, Object> filter, String type) {
        EscapeCharUtils.escape(filter, "title");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<HallEntity> hallEntities = null;
        if (StringUtils.isNotEmpty(type) && "feign".equalsIgnoreCase(type)) {
            hallEntities = hallMapper.findByPageForFeign(filter);
            for (HallEntity item : hallEntities) {
                item.setFileList(materialService.handleMaterialNew(item.getId()));
            }
        } else {
            hallEntities = hallMapper.findByPage(filter);
        }
        PageInfo<HallEntity> pageInfo = new PageInfo<>(hallEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public ResponseMessage existsByTitleAndIdNot(String title, String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        try {
            List<HallEntity> hallLists = hallMapper.existsByTitleAndIdNot(title, id);
            if (null != hallLists && CollectionUtils.isNotEmpty(hallLists)) {
                responseMessage.setStatus(ResponseMessage.FAILED).setMsg("该名称已经存在！");
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg(e.getMessage());
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage changeStatus(String id, Integer status, String username) {
        HallEntity entity = hallMapper.selectByPrimaryKey(id);
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("厅室信息不存在");
        }
        entity.setUpdatedUser(username);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        hallMapper.updateByPrimaryKey(entity);
        // 添加上下线操作记录
        String msg = status == 9 ? "上线成功" : "下线成功";
        commonService.saveAuditLog(entity.getStatus(), status, id, username, msg, 1);
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public ResponseMessage findByList(Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title");
        return ResponseMessage.defaultResponse().setData(hallMapper.findByList(filter));
    }

    @Override
    public ResponseMessage dataBind(String updatedUser, DataBindModel model) {
        hallMapper.dataBind(updatedUser, new Date(), model.getDeptCode(), model.getIds());
        return ResponseMessage.defaultResponse().setMsg("组织机构切换成功");
    }

    @Override
    public long countByVenueId(String venueId) {
        return hallMapper.countByVenueId(venueId);
    }
}
