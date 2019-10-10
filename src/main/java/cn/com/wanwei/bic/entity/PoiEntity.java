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
@Table(name="bic_poi")
@ApiModel(description="poi管理")
@ToString(callSuper = true)
public class PoiEntity extends BaseEntity {

    @ApiModelProperty(value = "父ID（第一级poi时值为0）" ,required = true)
    private Long parentId;

    @ApiModelProperty(value = "关联信息ID(景区id)" ,required = true)
    private Long principalId;

    @ApiModelProperty(value = "编码" ,required = true)
    private String code;

    @ApiModelProperty(value = "标题" ,required = true)
    private String title;

    @ApiModelProperty(value = "子标题" ,required = true)
    private String subTitle;

    @ApiModelProperty(value = "宣传语（二级标题）" )
    private String slogan;

    @ApiModelProperty(value = "摘要" )
    private String summary;

    @ApiModelProperty(value = "简介" )
    private String description;

    @ApiModelProperty(value = "内容" )
    private String content;

    @ApiModelProperty(value = "权重" )
    private Float weight;

    @ApiModelProperty(value = "经度" ,required = true)
    private Double longitude;

    @ApiModelProperty(value = "纬度" ,required = true)
    private Double latitude;

    @ApiModelProperty(value = "地区名称（格式示例：甘肃省,兰州市,城关区）" ,required = true)
    private String regionFullName;

    @ApiModelProperty(value = "地区编码（格式示例：62,6201,620102）" ,required = true)
    private String regionFullCode;

    @ApiModelProperty(value = "所属区域" ,required = true)
    private String region;

    @ApiModelProperty(value = "详细地址" ,required = true)
    private String address;

    @ApiModelProperty(value = "审核状态（0：待审，1：通过，9：上线）" )
    private Integer status;

    @ApiModelProperty(value = "组织机构编码" ,required = true)
    private String deptCode;

    @ApiModelProperty(value = "类型（1：厕所，2：停车场，3：出入口，4：游客中心，5：景点）" ,required = true)
    private String type;

}