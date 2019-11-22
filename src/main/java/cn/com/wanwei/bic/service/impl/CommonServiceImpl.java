/**
 * 该源代码文件 CommonServiceImpl.java 是工程“wtcp-bics”的一部分。
 *
 * @project wtcp-bics
 * @author linjw
 * @date 2019年10月14日10:31:49
 */
package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.mapper.ScenicMapper;
import cn.com.wanwei.bic.model.WeightModel;
import cn.com.wanwei.bic.service.CommonService;
import cn.com.wanwei.common.model.ResponseMessage;
import cn.com.wanwei.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Service
public class CommonServiceImpl<T> implements CommonService<T> {

    @Autowired
    private ScenicMapper scenicMapper;

    @Override
    public ResponseMessage changeWeight(WeightModel weightModel, User user, Class<T> clazz) throws Exception {
        //1、根据class获取表名
        String tableName = getTableName(clazz);

        //2、根据表名查询对应的最大权重值
        Integer maxNum = scenicMapper.commonMaxWeight(tableName);
        List<String> ids = weightModel.getIds();
        if (ids != null && !ids.isEmpty()) {
            //判断为重新排序或者最大权重与排序大于999时所有数据权重清0
            if (weightModel.isFlag() || (maxNum + ids.size()) > Integer.MAX_VALUE) {

                //3、根据表名重置权重值
                scenicMapper.commonClearWeight(tableName);
                maxNum = 0;
            }
            for (int i = 0; i < ids.size(); i++) {
                //4、根据表名修改权重值
                scenicMapper.commonUpdateWeight(ids.get(i), maxNum + ids.size() - i, user.getUsername(), new Date(), tableName);
            }
        }
        return ResponseMessage.defaultResponse().setMsg("权重修改成功！");
    }

    private String getTableName(Class<T> clazz) {
        if(clazz.isAnnotationPresent(Table.class)){
            return clazz.getAnnotation(Table.class).name();
        }
        return null;
    }
}
