package cn.com.wanwei.bic.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_traffic_agent")
@ApiModel(description="交通枢纽管理")
@ToString(callSuper = true)
public class TrafficAgentEntity extends BaseEntity{

    @ApiModelProperty(value = "编码" ,required = true)
    private String code;

    @ApiModelProperty(value = "标题" ,required = true)
    private String title;

    @ApiModelProperty(value = "摘要" )
    private String summary;

    @ApiModelProperty(value = "简介" )
    private String description;

    @ApiModelProperty(value = "内容" )
    private String content;

    @ApiModelProperty(value = "权重" )
    private Integer weight;

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

    @ApiModelProperty(value = "交通类型" ,required = true)
    private String type;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    @ApiModelProperty(value = "审核状态（0：待审，1：通过，9：上线）")
    private Integer status;

    @ApiModelProperty(value = "简拼",readOnly = true)
    private String simpleSpell;

    @ApiModelProperty(value = "全拼",readOnly = true)
    private String fullSpell;

    @ApiModelProperty(value = "高德数据id",readOnly = true)
    private String gouldId;

    @ApiModelProperty(value = "联系电话",readOnly = true)
    private String phone;

    //冗余字段获取简拼（用来生成code）
    @Transient
    private String jpin;

    //冗余字段，关联素材的id
    @Transient
    private String timeId;

    //冗余字段，素材数组
    @Transient
    private List<MaterialEntity> materialList;


}