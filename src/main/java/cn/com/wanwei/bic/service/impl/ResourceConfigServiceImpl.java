/**
 * 该源代码文件 ResourceConfigServiceImpl.java 是工程“wtcp-bics”的一部分。
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年12月20日11:20:05
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.ResourceConfigEntity;
import cn.com.wanwei.bic.mapper.ResourceConfigMapper;
import cn.com.wanwei.bic.service.ResourceConfigService;
import cn.com.wanwei.bic.service.ScenicService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * wtcp-bics - ResourceConfigServiceImpl 景资源配置信息管理接口实现类
 */
@Service
@Slf4j
@RefreshScope
public class ResourceConfigServiceImpl implements ResourceConfigService {

    @Autowired
    private ResourceConfigMapper resourceConfigMapper;

    @Override
    public ResponseMessage save(ResourceConfigEntity entity, User user) throws Exception {
        String id = UUIDUtils.getInstance().getId();
        entity.setId(id);
        entity.setCreatedUser(user.getUsername());
        entity.setCreatedDate(new Date());
        entity.setUpdatedDate(new Date());
        resourceConfigMapper.insert(entity);
        return ResponseMessage.defaultResponse().setMsg("保存成功");
    }

    @Override
    public ResponseMessage edit(String id, ResourceConfigEntity entity, User user) throws Exception {
        ResourceConfigEntity resourceConfigEntity = resourceConfigMapper.selectByPrimaryKey(id);
        if(null == resourceConfigEntity){
            return ResponseMessage.validFailResponse().setMsg("该配置信息不存在");
        }
        entity.setId(id);
        entity.setCreatedDate(resourceConfigEntity.getCreatedDate());
        entity.setCreatedUser(resourceConfigEntity.getCreatedUser());
        entity.setUpdatedDate(new Date());
        entity.setUpdatedUser(user.getUsername());
        resourceConfigMapper.updateByPrimaryKey(entity);
        return ResponseMessage.defaultResponse().setMsg("更新成功");
    }

    @Override
    public ResponseMessage selectByPrimaryKey(String id) throws Exception {
        ResourceConfigEntity entity = resourceConfigMapper.selectByPrimaryKey(id);
        if(null == entity){
            return ResponseMessage.validFailResponse().setMsg("该配置信息不存在");
        }
        return ResponseMessage.defaultResponse().setData(entity);
    }

    @Override
    public ResponseMessage selectByCode(String code) throws Exception {
        ResourceConfigEntity entity = resourceConfigMapper.selectByCode(code);
        if(null == entity){
            return ResponseMessage.validFailResponse().setMsg("该配置信息不存在");
        }
        return ResponseMessage.defaultResponse().setData(entity);
    }

    @Override
    public ResponseMessage selectTableInfo() throws Exception {
        return ResponseMessage.defaultResponse().setData(resourceConfigMapper.selectTableInfo());
    }

    @Override
    public ResponseMessage selectColumnInfo(String tableName) throws Exception {
        return ResponseMessage.defaultResponse().setData(resourceConfigMapper.selectColumnInfo(tableName));
    }
}
