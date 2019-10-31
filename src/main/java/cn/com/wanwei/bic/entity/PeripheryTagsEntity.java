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
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_periphery_tags")
@ApiModel(description="周边标签信息")
@ToString(callSuper = true)
public class PeripheryTagsEntity extends BaseEntity{

    @ApiModelProperty(value = "周边ID")
    private String principalId;

    @ApiModelProperty(value = "标签分类")
    private String tagCatagory;

    @ApiModelProperty(value = "标签名称")
    private String tagName;
}