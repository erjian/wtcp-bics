package cn.com.wanwei.bic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_celebrity_tags")
@ApiModel(description="名人管理标签类")
@ToString(callSuper = true)
public class CelebrityTagsEntity extends BaseEntity{

    @ApiModelProperty(value = "名人ID")
    private String principalId;

    @ApiModelProperty(value = "标签分类")
    private String tagCatagory;

    @ApiModelProperty(value = "标签名称")
    private String tagName;
}