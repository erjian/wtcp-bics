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
@Table(name="bic_material")
@ApiModel(description="素材表")
@ToString(callSuper = true)
public class MaterialEntity extends BaseEntity{

    @ApiModelProperty(value = "关联信息ID")
    private String principalId;

    @ApiModelProperty(value = "素材ID")
    private String materialId;

    @ApiModelProperty(value = "素材类型（image：图片，audio：音频，video：视频， file: 文档）")
    private String fileType;

    @ApiModelProperty(value = "路径(相对路径)")
    private String fileUrl;

    @ApiModelProperty(value = "素材标识（1：标题图片，2：亮点图片，3：标题且亮点图片）")
    private Integer fileIdentify;

}