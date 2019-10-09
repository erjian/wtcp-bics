package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.ScenicSpotEntity;
import cn.com.wanwei.bic.feign.CoderServiceFeign;
import cn.com.wanwei.bic.mapper.ScenicSpotMapper;
import cn.com.wanwei.bic.service.ScenicSpotService;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.persistence.mybatis.MybatisPageRequest;
import cn.com.wanwei.persistence.mybatis.PageInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * wtcp-bics - ScenicSpotServiceImpl 景点管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class ScenicSpotServiceImpl implements ScenicSpotService {

    @Autowired
    private ScenicSpotMapper scenicSpotMapper;

    @Autowired
    private CoderServiceFeign coderServiceFeign;

    @Override
    public ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter) {
        try {
            Sort sort = Sort.by(new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "weight"),new Sort.Order(Sort.Direction.DESC, "createdDate")});
            MybatisPageRequest pageRequest = MybatisPageRequest.of(page, size, sort);
            PageHelper.startPage(pageRequest.getPage(),pageRequest.getSize(),pageRequest.getOrders());
            Page<ScenicSpotEntity> ScenicSpotEntities = scenicSpotMapper.findByPage(filter);
            PageInfo<ScenicSpotEntity> pageInfo = new PageInfo<>(ScenicSpotEntities, pageRequest);
            return ResponseMessage.defaultResponse().setData(pageInfo);
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("获取失败！");
        }
    }

    @Override
    public ResponseMessage find(Long id) {
        try {
            ScenicSpotEntity scenicSpotEntity =scenicSpotMapper.selectByPrimaryKey(id);
            if(scenicSpotEntity==null){
                return ResponseMessage.validFailResponse().setMsg("暂无该景点信息！");
            }
            return ResponseMessage.defaultResponse().setData(scenicSpotEntity);
        } catch (Exception e) {
           log.info(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("获取失败！");
        }
    }

    @Override
    public ResponseMessage create(ScenicSpotEntity scenicSpotEntity, User user) {
        try {
//            coderServiceFeign.buildSerialByCode()/*************待定****************/
//         scenicSpotEntity.setStatus(true);/*************待定****************/
            scenicSpotEntity.setCreatedUser(user.getUsername());
            scenicSpotEntity.setCreatedDate(new Date());
            scenicSpotEntity.setDeptCode(user.getOrg().getCode());
            scenicSpotMapper.insert(scenicSpotEntity);
            return ResponseMessage.defaultResponse().setMsg("保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.validFailResponse().setMsg("获取失败！");
        }
    }

    @Override
    public ResponseMessage update(Long id, ScenicSpotEntity scenicSpotEntity, User user) {
        try {
            ScenicSpotEntity sntity=scenicSpotMapper.selectByPrimaryKey(id);
            if(sntity!=null){
                scenicSpotEntity.setCreatedUser(sntity.getCreatedUser());
                scenicSpotEntity.setCreatedDate(sntity.getCreatedDate());
    //            scenicSpotEntity.setStatus();/***********待定**************/
                scenicSpotEntity.setCode(sntity.getCode());
                scenicSpotEntity.setDeptCode(user.getOrg().getCode());
                scenicSpotEntity.setUpdatedUser(user.getUsername());
                scenicSpotEntity.setUpdatedDate(new Date());
                scenicSpotMapper.updateByPrimaryKey(scenicSpotEntity);
                return ResponseMessage.defaultResponse().setMsg("更新成功！");
            }
            return ResponseMessage.validFailResponse().setMsg("暂无该景点信息！");
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("更新失败！");
        }
    }

    @Override
    public ResponseMessage delete(Long id) {
        try {
            scenicSpotMapper.deleteByPrimaryKey(id);
            return ResponseMessage.defaultResponse().setMsg("删除成功！");
        } catch (Exception e) {
           log.info(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("删除失败！");
        }
    }

    @Override
    public ResponseMessage goWeight(Long id, Float weight, User user) {
        try {
            ScenicSpotEntity sntity=scenicSpotMapper.selectByPrimaryKey(id);
            if(sntity!=null){
                sntity.setWeight(weight);
                sntity.setDeptCode(user.getOrg().getCode());
                sntity.setUpdatedUser(user.getUsername());
                sntity.setUpdatedDate(new Date());
                scenicSpotMapper.updateByPrimaryKey(sntity);
                return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
            }
            return ResponseMessage.validFailResponse().setMsg("暂无该景点信息！");
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseMessage.validFailResponse().setMsg("权重修改失败！");
        }
    }
}
