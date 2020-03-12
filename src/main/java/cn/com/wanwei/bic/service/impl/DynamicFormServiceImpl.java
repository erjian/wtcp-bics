package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.DynamicFormEntity;
import cn.com.wanwei.bic.model.CustomFormModel;
import cn.com.wanwei.bic.service.DynamicFormService;
import cn.com.wanwei.bic.utils.UUIDUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class DynamicFormServiceImpl implements DynamicFormService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int batchInsert(CustomFormModel customFormModel, String username) {
        List<DynamicFormEntity> dynamicFormEntities = Lists.newArrayList();
        String principalId = customFormModel.getPrincipalId();
        Map<String, String> map = customFormModel.getForm();
        Set<String> keys = map.keySet();
        for(String s:keys){
            DynamicFormEntity entity = new DynamicFormEntity();
            entity.setPrincipalId(principalId);
            entity.setExtendField(s);
            entity.setExtendValue(map.get(s));
            dynamicFormEntities.add(entity);
        }
        return this.batchSave(dynamicFormEntities, username);
    }

    @Override
    public int deleteById(String id) {
        StringBuilder sb = new StringBuilder("delete from ").append(getTableName()).append(" where id = ?");
        return jdbcTemplate.update(sb.toString(), id);
    }

    @Override
    public int deleteByPrincipalId(String principalId) {
        StringBuilder sb = new StringBuilder("delete from ").append(getTableName()).append(" where principal_id = ?");
        return jdbcTemplate.update(sb.toString(), principalId);
    }

    @Override
    public String findByPidAndField(String principalId, String field) {
        StringBuilder sb = new StringBuilder("select * from ").append(getTableName()).append(" where principal_id = ? and extend_field = ?");
        return jdbcTemplate.queryForObject(sb.toString(), new String[]{principalId,field}, String.class);
    }

    @Override
    public Map<String, Object> findByPrincipalId(String principalId) {
        StringBuilder sb = new StringBuilder("select * from ").append(getTableName()).append(" where principal_id = ?");
        return jdbcTemplate.queryForMap(sb.toString(), principalId);
    }

    private int batchSave(List<DynamicFormEntity> dynamicFormList, String username) {
        StringBuilder sb = new StringBuilder("insert into ").append(getTableName())
                .append(" (created_user, created_date, updated_user, updated_date, ")
                .append("principal_id, extend_field, extend_value, id values (?,?,?,?,?,?,?,?)");
        Date date = new Date(System.currentTimeMillis());
        int[] updatedCountArray = jdbcTemplate.batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                int index = 0;
                ps.setString(++index, username);
                ps.setDate(++index, date);
                ps.setString(++index, username);
                ps.setDate(++index, date);
                ps.setString(++index, dynamicFormList.get(i).getPrincipalId());
                ps.setString(++index, dynamicFormList.get(i).getExtendField());
                ps.setString(++index, dynamicFormList.get(i).getExtendValue());
                ps.setString(++index, UUIDUtils.getInstance().getId());
            }

            public int getBatchSize() {
                return dynamicFormList.size();
            }
        });
        return updatedCountArray.length;
    }

    private String getTableName() {
        Class<DynamicFormEntity> clazz = DynamicFormEntity.class;
        if (clazz.isAnnotationPresent(Table.class)) {
            return clazz.getAnnotation(Table.class).name();
        }
        return null;
    }

}
