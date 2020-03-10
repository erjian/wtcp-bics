package cn.com.wanwei.bic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "t_bic_heritage")
@ApiModel(description = "酒店管理")
@ToString(callSuper = true)
public class HotelEntity extends CommonEntity{

    @ApiModelProperty(value = "名称简拼",required = true)
    private String simpleSpell;

    @ApiModelProperty(value = "名称全拼",required = true)
    private String fullSpell;

    @ApiModelProperty(value = "酒店类别",required = true)
    private String category;

    @ApiModelProperty(value = "酒店级别",required = true)
    private Integer level;

    @ApiModelProperty(value = "一句话特色",required = true)
    private String feature;

    @ApiModelProperty(value = "亮点摘要")
    private String lightspot;

    @ApiModelProperty(value = "房间总数")
    private Integer roomNum;

    @ApiModelProperty(value = "最低房价")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "评分",required = true)
    private Float score;

    @ApiModelProperty(value = "配套设施",required = true)
    private String facility;

    @ApiModelProperty(value = "服务项目",required = true)
    private String services;

    @ApiModelProperty(value = "全景地址")
    private String vrUrl;

    @ApiModelProperty(value = "营业时间",required = true)
    private Date openTime;

    @ApiModelProperty(value = "交通信息",required = true)
    private String trafficNotice;

    @ApiModelProperty(value = "交通信息",required = true)
    private String stayNotice;

    @ApiModelProperty(value = "状态",required = true)
    private Integer status;

}