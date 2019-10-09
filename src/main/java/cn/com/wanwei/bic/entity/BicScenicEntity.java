/**
 * 该源代码文件 BicScenicEntity 是工程“wtcp-bics”的一部分
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日10:44:28
 */
package cn.com.wanwei.bic.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="bic_scenic")
@ApiModel(description="景区基础信息管理")
public class BicScenicEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value="主键")
    private Long id;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "子标题")
    private String subTitle;

    @ApiModelProperty(value = "宣传语（二级标题）")
    private String slogan;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "简介")
    private String description;

    @ApiModelProperty(value = "权重")
    private Float weight;

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
    private Integer level;


    private Double area;

    private Integer category;

    private String panoramicUrl;

    private Boolean status;

    private Float playtime;

    private Float score;

    private String deptCode;

    private String createdUser;

    private Date createdDate;

    private String updatedUser;

    private Date updatedDate;

    private String content;
}