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
@Table(name="t_bic_entertainment")
@ApiModel(description="休闲娱乐管理")
@ToString(callSuper = true)
public class EntertainmentEntity extends BaseEntity{

    @ApiModelProperty(value = "类型",required = true)
    private Integer type;

    @ApiModelProperty(value = "编码",required = true)
    private String code;

    @ApiModelProperty(value = "标题",required = true)
    private String title;

    @ApiModelProperty(value = "子标题",required = true)
    private String subTitle;

    @ApiModelProperty(value = "宣传语（二级标题）")
    private String slogan;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "权重")
    private Float weight;

    @ApiModelProperty(value = "经度",required = true)
    private Double longitude;

    @ApiModelProperty(value = "维度",required = true)
    private Double latitude;

    @ApiModelProperty(value = "地区名称（格式示例：甘肃省,兰州市,城关区）",required = true)
    private String regionFullName;

    @ApiModelProperty(value = "地区编码（格式示例：62,6201,620102）",required = true)
    private String regionFullCode;

    @ApiModelProperty(value = "所属区域",required = true)
    private String region;

    @ApiModelProperty(value = "详细地址",required = true)
    private String address;

    @ApiModelProperty(value = "审核状态（0：待审，1：通过，2：退回，9：上线）",required = true)
    private Integer status;

    @ApiModelProperty(value = "所属景区")
    private String scenicId;

    @ApiModelProperty(value = "是否在景区之内（1：是，0：否）" ,required = true)
    private Integer withinScenic;

    @ApiModelProperty(value = "是否有停车场（1：是，0：否）" ,required = true)
    private Integer withinPark;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "组织机构编码" ,required = true)
    private String deptCode;

    @ApiModelProperty(value = "内容")
    private String content;

}