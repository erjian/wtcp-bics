package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.BaseTagsEntity;
import cn.com.wanwei.bic.mapper.TagsMapper;
import cn.com.wanwei.bic.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.util.List;

@Service
public class TagsServiceImpl<T> implements TagsService<T> {

    @Autowired
    private TagsMapper tagsMapper;

    @Override
    public BaseTagsEntity selectByPrimaryKey(String id, Class<T> clazz) {
        return tagsMapper.selectByPrimaryKey(id, getTableName(clazz));
    }

    @Override
    public int deleteByPrincipalId(String principalId, Class<T> clazz) {
        return tagsMapper.deleteByPrincipalId(principalId, getTableName(clazz));
    }

    @Override
    public int batchInsert(List<BaseTagsEntity> entityList) {
        return 0;
    }


    private String getTableName(Class<T> clazz){
        clazz.isAnnotationPresent(Table.class);
        Table table = clazz.getAnnotation(Table.class);
        return table.name();
    }
}
