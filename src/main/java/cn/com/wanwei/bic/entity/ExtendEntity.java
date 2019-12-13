/**
 * 该源代码文件 DestinationEntity 是工程“wtcp-bics”的一部分
 * @project wtcp-bics
 * @author zhanglei
 * @date 2019年10月10日09:12:28
 * @description 扩展信息实体类
 */
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

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_extend")
@ApiModel(description="扩展信息管理")
@ToString(callSuper = true)
public class ExtendEntity extends BaseEntity{

    @ApiModelProperty(value = "关联信息ID")
    private String principalId;

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

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "审核状态")
    private Integer status;

    @ApiModelProperty(value = "价格")
    private Float price;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

    @ApiModelProperty(value = "在线状态")
    private Integer onlineStatus;

    public Integer getOnlineStatus() {
        return this.status == 9 ? this.status : 1;
    }

    //冗余字段，关联素材的id
    @Transient
    private String timeId;

}