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
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_catering")
@ApiModel(description="餐饮管理")
@ToString(callSuper = true)
public class CateringEntity extends CommonEntity{

    @ApiModelProperty(value = "名称简拼")
    private String simpleSpell;

    @ApiModelProperty(value = "名称全拼")
    private String fullSpell;

    @ApiModelProperty(value = "餐厅级别 包含：一级店、二级店、三级店、不分级")
    private String level;

    @ApiModelProperty(value = "一句话特色")
    private String feature;

    @ApiModelProperty(value = "亮点摘要")
    private String lightspot;

    @ApiModelProperty(value = "评分")
    private Float score;

    @ApiModelProperty(value = "菜系标签 例如：火锅、地方菜、西餐、小吃、快餐简餐、烧烤、韩国料理、日本料理、面包甜点、酒吧酒馆、海鲜、茶馆、清真、农家菜、创意菜、自助餐、素食、东南亚菜、咖啡店、其他")
    private String cookStyle;

    @ApiModelProperty(value = "特色标签 例如：特色早餐、老字号、网红点、深夜营业、美食街、主题餐厅、百年老店、中华老字号、下午茶、景观餐厅、苍蝇馆子")
    private String featureTags;

    @ApiModelProperty(value = "网评标签 可以设置多个标签，标签之间用半角英文逗号分隔，便于显示时拆分格式化显示。该字段仅为了显示使用; 例如：产品味道不错，环境好，烤羊肉好吃")
    private String webComment;

    @ApiModelProperty(value = "平均消费")
    private Integer avgConsumption;

    @ApiModelProperty(value = "营业时间")
    private Date openTime;

    @ApiModelProperty(value = "交通信息")
    private String trafficNotice;

    @ApiModelProperty(value = "就餐须知")
    private String repastNotice;

    @ApiModelProperty(value = "全景地址")
    private String vrUrl;

    @ApiModelProperty(value = "状态")
    private Integer status;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }
}