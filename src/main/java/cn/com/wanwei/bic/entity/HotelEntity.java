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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "t_bic_hotel")
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
    private String openTime;

    @ApiModelProperty(value = "交通信息",required = true)
    private String trafficNotice;

    @ApiModelProperty(value = "入职须知",required = true)
    private String stayNotice;

    @ApiModelProperty(value = "状态",required = true)
    private Integer status;

    @ApiModelProperty(value = "在线状态")
    private Integer onlineStatus;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }

    //冗余字段，关联素材的id
    @Transient
    private String timeId;

    /**
     * 用于存储景区-标签
     */
    @Transient
    @ApiModelProperty(value = "标签")
    public List<HotelTagsEntity> tagsEntities;

    private Map<String,Object> fileList;

}