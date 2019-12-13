/**
 * 该源代码文件 RentalCarEntity 是工程“wtcp-bics”的一部分
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年11月21日14:33:45
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

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_rental_car")
@ApiModel(description="租车基础信息管理")
@ToString(callSuper = true)
public class RentalCarEntity extends BaseEntity {
    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "简称")
    private String subTitle;

    @ApiModelProperty(value = "名称全拼")
    private String titleQp;

    @ApiModelProperty(value = "名称简拼")
    private String titleJp;

    @ApiModelProperty(value = "所在地区编码")
    private String regionFullCode;

    @ApiModelProperty(value = "所在地区名称")
    private String regionFullName;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "所属区域")
    private String region;

    @ApiModelProperty(value = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "成立时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date establishTime;

    @ApiModelProperty(value = "包车类型")
    private String charteredBusType;

    @ApiModelProperty(value = "班车类型")
    private String regularBusType;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "宣传语")
    private String slogan;

    @ApiModelProperty(value = "简介")
    private String summary;

    @ApiModelProperty(value = "权重")
    private Integer weight;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    @ApiModelProperty(value = "内容")
    private String content;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }

    //冗余字段，关联素材的id
    @Transient
    private String timeId;
}