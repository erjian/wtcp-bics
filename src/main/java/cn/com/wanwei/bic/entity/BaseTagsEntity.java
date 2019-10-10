package cn.com.wanwei.bic.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class BaseTagsEntity extends BaseEntity{

    @ApiModelProperty(value = "关联ID")
    private Long principalId;

    @ApiModelProperty(value = "标签分类")
    private String tagCatagory;

    @ApiModelProperty(value = "标签名称")
    private String tagName;
}