package cn.com.wanwei.bic.entity;

import cn.com.wanwei.mybatis.entity.BaseEntity;
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
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_hall")
@ApiModel(description="场馆厅室表")
@ToString(callSuper = true)
public class HallEntity extends BaseEntity {

    @ApiModelProperty(value = "所属场馆ID")
    private String venueId;

    @ApiModelProperty(value = "所属场馆名称")
    private String venueName;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "简拼")
    private String simpleSpell;

    @ApiModelProperty(value = "全拼")
    private String fullSpell;

    @ApiModelProperty(value = "简介")
    private String summary;

    @ApiModelProperty(value = "摘要")
    private String description;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "厅室类别，如：展厅、阅览室、教室、排练厅、综合厅、会议厅、商务厅、电影厅、服务中心等")
    private String category;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "VR地址")
    private String vrUrl;

    @ApiModelProperty(value = "厅室面积，面积单位：㎡")
    private Integer area;

    @ApiModelProperty(value = "接待上限，接待游客上限")
    private Integer maxReceptionNum;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    /** 以下为冗余字段 **/

    @Transient
    @ApiModelProperty(value = "标签列表")
    private Map<String,Object> fileList;

}