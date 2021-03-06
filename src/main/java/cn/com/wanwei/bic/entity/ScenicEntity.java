/**
 * 该源代码文件 ScenicEntity 是工程“wtcp-bics”的一部分
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日10:44:28
 */
package cn.com.wanwei.bic.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_scenic")
@ApiModel(description="景区基础信息管理")
@ToString(callSuper = true)
public class ScenicEntity extends BaseEntity{

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "标题全拼")
    private String titleQp;

    @ApiModelProperty(value = "标题简拼")
    private String titleJp;

    @ApiModelProperty(value = "子标题")
    private String subTitle;

    @ApiModelProperty(value = "宣传语（二级标题）")
    private String slogan;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "地区名称（格式示例：甘肃省,兰州市,城关区）")
    private String regionFullName;

    @ApiModelProperty(value = "地区编码（格式示例：62,6201,620102）")
    private String regionFullCode;

    @ApiModelProperty(value = "所属区域")
    private String region;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "级别（0：非A级，1：1A级，2：2A级，3：3A级，4：4A级，5：5A级）")
    private String level;

    @ApiModelProperty(value = "面积")
    private Double area;

    @ApiModelProperty(value = "类别（1：普通景区，2：乡村游）")
    private String category;

    @ApiModelProperty(value = "全景地址1")
    private String panoramicUrl1;

    @ApiModelProperty(value = "全景地址2")
    private String panoramicUrl2;

    @ApiModelProperty(value = "全景地址3")
    private String panoramicUrl3;

    @ApiModelProperty(value = "全景地址4")
    private String panoramicUrl4;

    @ApiModelProperty(value = "审核状态（0：待审，1：通过，9：上线）")
    private Integer status;

    @ApiModelProperty(value = "游玩时间")
    private Float playtime;

    @ApiModelProperty(value = "评分")
    private Float score;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "开业时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "夏季营业时间")
    private String summerTime;

    @ApiModelProperty(value = "冬季营业时间")
    private String winterTime;

    @ApiModelProperty(value = "亮点标题", notes = "亮点标题")
    private String lightTitle;

    @ApiModelProperty(value = "排名")
    private Integer rank;

    @ApiModelProperty(value = "在线状态")
    private Integer onlineStatus;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }

    /**
     * 用于存储景区-标签
     */
    @Transient
    @ApiModelProperty(value = "标签")
    public List<ScenicTagsEntity> tagsEntities;

    //冗余字段，关联素材的id
    @Transient
    private String timeId;

    private Map<String,Object> fileList;

}