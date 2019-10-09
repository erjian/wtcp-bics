package cn.com.wanwei.bic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "bic_dictionary")
@ApiModel(description = "字典管理")
@ToString(callSuper = true)
public class DictionaryEntity extends BaseEntity{
    @ApiModelProperty(value = "父ID（第一级时为0）")
    private Long parentId;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "权重")
    private Float weight;
    @ApiModelProperty(value = "备注信息")
    private String remark;
}