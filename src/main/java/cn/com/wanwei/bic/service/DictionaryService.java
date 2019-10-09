package cn.com.wanwei.bic.service;

import cn.com.wanwei.bic.entity.DictionaryEntity;
import cn.com.wanwei.common.model.ResponseMessage;

/**
 * 字典管理相关接口
 */
public interface DictionaryService {

    /**
     * 保存一条记录
     * @param record
     * @return
     */
    ResponseMessage save(DictionaryEntity record, String userName) throws Exception;

    /**
     * 删除
     * @param id
     * @return
     */
    ResponseMessage deleteByPrimaryKey(Long id)throws Exception;

    /**
     * 编辑
     * @param id 字典ID
     * @param username 用户名
     * @return
     * @throws Exception
     */
    ResponseMessage edit(Long id,DictionaryEntity entity, String username) throws Exception;
}
