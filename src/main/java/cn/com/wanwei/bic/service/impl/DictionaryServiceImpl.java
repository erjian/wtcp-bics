package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.DictionaryEntity;
import cn.com.wanwei.bic.mapper.DictionaryMapper;
import cn.com.wanwei.bic.service.DictionaryService;
import cn.com.wanwei.common.model.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class DictionaryServiceImpl implements DictionaryService {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public ResponseMessage save(DictionaryEntity record, String userName) {
        record.setCreatedUser(userName);
        record.setCreatedDate(new Date());
        dictionaryMapper.insert(record);
        return ResponseMessage.defaultResponse().setMsg("保存成功");
    }

    @Override
    public ResponseMessage deleteByPrimaryKey(Long id){
        dictionaryMapper.deleteByPrimaryKey(id);
        return ResponseMessage.defaultResponse().setMsg("删除成功");
    }

    @Override
    public ResponseMessage edit(Long id,DictionaryEntity entity,String userName){
        DictionaryEntity dictionaryEntity = dictionaryMapper.selectByPrimaryKey(id);
        if (dictionaryEntity == null){
            return ResponseMessage.validFailResponse().setMsg("该字典信息不存在！！！");
        }
        entity.setUpdatedUser(userName);
        entity.setUpdatedDate(new Date());
        dictionaryMapper.updateByPrimaryKey(entity);
        return ResponseMessage.defaultResponse().setMsg("编辑成功");
    }

}
