package cn.com.wanwei.bic.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="t_bic_poi")
@ApiModel(description="poi管理")
@ToString(callSuper = true)
public class PoiEntity extends CommonEntity {

    @ApiModelProperty(value = "父ID（第一级poi时值为0）" ,required = true)
    private String parentId;

    @ApiModelProperty(value = "关联信息ID(景区id)" ,required = true)
    private String principalId;

    @ApiModelProperty(value = "审核状态（0：待审，1：通过，9：上线）" )
    private Integer status;

    @ApiModelProperty(value = "组织机构编码" ,required = true)
    private String deptCode;

    @ApiModelProperty(value = "类型（1：厕所，2：停车场，3：出入口，4：游客中心，5：景点）" ,required = true)
    private String type;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "景区名称")
    private String scenicName;

}