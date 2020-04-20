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
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_periphery")
@ApiModel(description="周边信息管理")
@ToString(callSuper = true)
public class PeripheryEntity extends CommonEntity {

    @ApiModelProperty(value = "类别（1：特色小吃，2：餐饮服务，3：小吃街，4：购物场所，5：特产）")
    private String category;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "商圈（万达商圈，兰州中心，...）")
    private String tradingArea;

    @ApiModelProperty(value = "类型（中餐厅，川菜，...）")
    private String type;

    @ApiModelProperty(value = "审核状态（0：待审，1：通过，9：上线）")
    private Integer status;

    @ApiModelProperty(value = "评分")
    private Float score;

    @ApiModelProperty(value = "停车方便评分")
    private Float parkScore;

    @ApiModelProperty(value = "人均消费")
    private Float perConsumption;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    @ApiModelProperty(value = "夏季营业时间")
    private String summerTime;

    @ApiModelProperty(value = "冬季营业时间")
    private String winterTime;

    @ApiModelProperty(value = "交通说明")
    private String trafficNotice;

    /**
     * 冗余字段
     */
    @ApiModelProperty(value = "简拼")
    @Transient
    private String jSpell;

    //冗余字段，关联素材的id
    @Transient
    private String timeId;

    // 关联的美食ID集合
    @Transient
    private List<String> cateIds;

    //冗余字段，标签列表
    private List<BaseTagsEntity> tagList;

    //冗余字段，素材信息
    private Map<String, Object> fileList;
}