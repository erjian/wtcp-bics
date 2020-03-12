package cn.com.wanwei.bic.entity;

import cn.com.wanwei.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_dynamic_form")
@ApiModel(description="动态表单表")
@ToString(callSuper = true)
public class DynamicFormEntity extends BaseEntity {

    @ApiModelProperty(value = "关联ID")
    private String principalId;

    @ApiModelProperty(value = "扩展字段")
    private String extendField;

    @ApiModelProperty(value = "扩展内容")
    private String extendValue;

}
