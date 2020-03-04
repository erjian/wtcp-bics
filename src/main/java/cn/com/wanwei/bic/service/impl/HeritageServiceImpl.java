package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.HeritageEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.AuditLogMapper;
import cn.com.wanwei.bic.mapper.HeritageMapper;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.bic.service.HeritageService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * wtcp-bics - HeritageServiceImpl 非遗基础信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class HeritageServiceImpl implements HeritageService {

    @Autowired
    private HeritageMapper heritageMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CommonService commonService;

    public static final int LINE_TYPE = 1;//上下线类型

    public static final int AUDIT_TYPE = 0;//审核类型


    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        EscapeCharUtils.escape(filter, "title", "subTitle", "areaName");
        MybatisPageRequest pageRequest = PageUtils.getInstance().setPage(page, size, filter, Sort.Direction.DESC, "created_date", "updated_date");
        Page<HeritageEntity> heritageEntities = heritageMapper.findByPage(filter);
        PageInfo<HeritageEntity> pageInfo = new PageInfo<>(heritageEntities, pageRequest);
        return ResponseMessage.defaultResponse().setData(pageInfo);
    }

    @Override
    public HeritageEntity selectByPrimaryKey(String id) {
        return heritageMapper.selectByPrimaryKey(id);
    }

    @Override
    public ResponseMessage deleteByPrimaryKey(String id) {
         HeritageEntity heritageEntity = heritageMapper.selectByPrimaryKey(id);
         if(null == heritageEntity){
             return ResponseMessage.validFailResponse().setMsg("该数据不存在！");
         }
        heritageMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ResponseMessage insert(EntityTagsModel<HeritageEntity> model, User currentUser, Long ruleId, Integer appCode) {
        HeritageEntity heritageEntity = model.getEntity();
        String id = UUIDUtils.getInstance().getId();
        heritageEntity.setId(UUIDUtils.getInstance().getId());
        ResponseMessage response = coderServiceFeign.buildSerialByCode(ruleId, appCode, model.getType());
        heritageEntity.setCode(response.getData().toString());
        heritageEntity.setFullSpell(PinyinUtils.getPingYin(heritageEntity.getTitle()).toLowerCase());
        heritageEntity.setSimpleSpell(PinyinUtils.converterToFirstSpell(heritageEntity.getTitle()).toLowerCase());
        heritageEntity.setCreatedUser(currentUser.getUsername());
        heritageEntity.setUpdatedUser(currentUser.getUsername());
        heritageEntity.setCreatedDate(new Date());
        heritageEntity.setUpdatedDate(new Date());
        heritageEntity.setDeptCode(currentUser.getOrg().getCode());
        heritageEntity.setStatus(1);
        heritageEntity.setWeight(0);
        heritageMapper.insert(heritageEntity);
        //保存素材
        if (CollectionUtils.isNotEmpty(model.getMaterialList())) {
            materialService.batchInsert(id, model.getMaterialList(), currentUser);
        }
        return ResponseMessage.defaultResponse().setMsg("保存成功").setData(id);
    }

    @Override
    public ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HeritageEntity> heritageModel, User currentUser) {
        HeritageEntity heritageEntity = heritageMapper.selectByPrimaryKey(id);
        if (null == heritageEntity) {
            return ResponseMessage.validFailResponse().setMsg("该非遗信息不存在！");
        }
        HeritageEntity entity = heritageModel.getEntity();
        entity.setId(id);
        entity.setCode(heritageEntity.getCode());
        entity.setCreatedDate(heritageEntity.getCreatedDate());
        entity.setCreatedUser(heritageEntity.getCreatedUser());
        entity.setDeptCode(heritageEntity.getDeptCode());
        entity.setFullSpell(PinyinUtils.getPingYin(entity.getTitle()).toLowerCase());
        entity.setSimpleSpell(PinyinUtils.converterToFirstSpell(entity.getTitle()).toLowerCase());
        entity.setStatus(0);
        entity.setUpdatedDate(new Date());
        entity.setUpdatedUser(currentUser.getName());
        heritageMapper.updateByPrimaryKey(entity);
        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage changeStatus(String id, Integer status, String username) {
        HeritageEntity entity = heritageMapper.selectByPrimaryKey(id);
        if (null == entity) {
            return ResponseMessage.validFailResponse().setMsg("该数据不存在");
        }
        if (status == 9 && entity.getStatus() != 1) {
            return ResponseMessage.validFailResponse().setMsg("该数据未审核通过，不能上线，请先进行审核！");
        }
        entity.setUpdatedUser(username);
        entity.setUpdatedDate(new Date());
        entity.setStatus(status);
        heritageMapper.updateByPrimaryKey(entity);
        String msg = status == 9 ? "上线成功" : "下线成功";
        commonService.saveAuditLog(entity.getStatus(), status, id, username, msg, LINE_TYPE);
        return ResponseMessage.defaultResponse().setMsg(msg);
    }

    @Override
    public ResponseMessage dataBind(String updatedUser, DataBindModel model) {
        heritageMapper.dataBind(updatedUser, new Date(), model.getDeptCode(), model.getIds());
        return ResponseMessage.defaultResponse().setMsg("组织机构切换成功");
    }

    @Override
    public ResponseMessage existsByTitleAndIdNot(String title, String id) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        try {
            List<HeritageEntity> list = heritageMapper.existsByTitleAndIdNot(title, id);
            if (CollectionUtils.isNotEmpty(list)) {
                responseMessage.setStatus(ResponseMessage.FAILED).setMsg("该非遗名称已经存在！");
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            responseMessage.setStatus(ResponseMessage.FAILED).setMsg(e.getMessage());
        }
        return responseMessage;
    }

    @Override
    public ResponseMessage examineHeritage(String id, int auditStatus, String msg, User currentUser) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        HeritageEntity heritageEntity = heritageMapper.selectByPrimaryKey(id);
        if (heritageEntity != null) {
            heritageEntity.setStatus(auditStatus);
            heritageEntity.setUpdatedDate(new Date());
            heritageMapper.updateByPrimaryKey(heritageEntity);
            //添加审核记录
            commonService.saveAuditLog(heritageEntity.getStatus(), auditStatus, id, currentUser.getUsername(), msg, AUDIT_TYPE);
        } else {
            return responseMessage.validFailResponse().setMsg("该非遗信息不存在");
        }
        return responseMessage;
    }


}
