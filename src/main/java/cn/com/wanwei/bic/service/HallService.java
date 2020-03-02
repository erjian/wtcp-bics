package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.HallEntity;
import cn.com.wanwei.bic.model.DataBindModel;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;

import java.util.Map;

public interface HallService {

    ResponseMessage deleteByPrimaryKey(String id);

    ResponseMessage insert(EntityTagsModel<HallEntity> hallModel, User user, Long ruleId, Integer appCode);

    HallEntity selectByPrimaryKey(String id);

    ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HallEntity> hallModel, User user);

    ResponseMessage findByPage(Integer page, Integer size, Map<String, Object> filter);

    ResponseMessage findByPageForFeign(Integer page, Integer size, Map<String, Object> filter);

    ResponseMessage existsByTitleAndIdNot(String title, String id);

    ResponseMessage changeStatus(String id, Integer status, String username);

    ResponseMessage findByList(Map<String, Object> filter);

    ResponseMessage dataBind(String updatedUser, DataBindModel model) throws Exception;

    long countByVenueId(String venueId);
}
