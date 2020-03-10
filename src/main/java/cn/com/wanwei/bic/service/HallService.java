package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.HallEntity;
import cn.com.wanwei.bic.model.EntityTagsModel;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import cn.com.wanwei.mybatis.service.BaseService;

public interface HallService extends BaseService<HallEntity, String> {

    ResponseMessage save(EntityTagsModel<HallEntity> hallModel, User user, Long ruleId, Integer appCode);

    ResponseMessage updateByPrimaryKey(String id, EntityTagsModel<HallEntity> hallModel, User user);

    ResponseMessage updateOnlineStatus(String id, Integer status, String username);

    long countByVenueId(String venueId);
}
