package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.mapper.TagsMapper;
import cn.com.wanwei.bic.service.TagsService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TagsServiceImpl<T> implements TagsService<T> {

    @Autowired
    private TagsMapper tagsMapper;

    @Override
    public BaseTagsEntity selectByPrimaryKey(String id, Class<T> clazz) {
        return tagsMapper.selectByPrimaryKey(id, getTableName(clazz));
    }

    @Override
    public ResponseMessage findByPrincipalId(String principalId, Class<T> clazz) {
        ResponseMessage responseMessage = ResponseMessage.defaultResponse();
        List<BaseTagsEntity> backList = tagsMapper.findByPrincipalId(principalId, getTableName(clazz));
        if (backList.size() > 0) {
            responseMessage.setData(backList);
        }
        return responseMessage;
    }

    @Override
    public int deleteByPrincipalId(String principalId, Class<T> clazz) {
        return tagsMapper.deleteByPrincipalId(principalId, getTableName(clazz));
    }

    @Override
    public int batchInsert(String principalId, List<BaseTagsEntity> entityList, User user, Class<T> clazz) {
        String tableName = getTableName(clazz);
        // 先删除原有的标签信息
        tagsMapper.deleteByPrincipalId(principalId, tableName);

        for (BaseTagsEntity item : entityList) {
            item.setId(UUIDUtils.getInstance().getId());
            item.setPrincipalId(principalId);
            item.setCreatedUser(user.getUsername());
            item.setCreatedDate(new Date());
        }
        Map<String, Object> params = Maps.newHashMap();
        params.put("entityList", entityList);
        params.put("tableName", tableName);
        return tagsMapper.batchInsert(params);
    }

    private String getTableName(Class<T> clazz) {
        if(clazz.isAnnotationPresent(Table.class)){
            return clazz.getAnnotation(Table.class).name();
        }
        return null;
    }
}
