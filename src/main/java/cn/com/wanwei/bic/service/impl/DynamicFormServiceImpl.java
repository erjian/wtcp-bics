package cn.com.wanwei.bic.service.impl;

import cn.com.wanwei.bic.entity.DynamicFormEntity;
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

@Service
public class DynamicFormServiceImpl implements DynamicFormService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int insert(DynamicFormEntity dynamicFormEntity, String username) {
        List<DynamicFormEntity> dynamicFormList = Lists.newArrayList();
        dynamicFormList.add(dynamicFormEntity);
        return this.batchSave(dynamicFormList, username);
    }

    @Override
    public int batchInsert(List<DynamicFormEntity> dynamicFormList, String username) {
        return this.batchSave(dynamicFormList, username);
    }

    @Override
    public int deleteById(String id) {
        StringBuilder sb = new StringBuilder("delete from ").append(getTableName()).append(" where id = ?");
        return jdbcTemplate.update(sb.toString(), id);
    }

    @Override
    public int deleteByPrincipalId(String principalId) {
        StringBuilder sb = new StringBuilder("delete from ").append(getTableName()).append(" where id = ?");
        return jdbcTemplate.update(sb.toString(), principalId);
    }

    @Override
    public DynamicFormEntity findById(String id) {
        StringBuilder sb = new StringBuilder("select * from ").append(getTableName()).append(" where id = ?");
        return jdbcTemplate.queryForObject(sb.toString(), DynamicFormEntity.class, id);
    }

    @Override
    public List<DynamicFormEntity> findByPrincipalId(String principalId) {
        StringBuilder sb = new StringBuilder("select * from ").append(getTableName()).append(" where principal_id = ?");
        return jdbcTemplate.queryForList(sb.toString(), DynamicFormEntity.class, principalId);
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
