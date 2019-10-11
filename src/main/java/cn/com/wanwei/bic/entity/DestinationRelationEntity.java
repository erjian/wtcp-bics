/**
 * 该源代码文件 DestinationRelationEntity 是工程“wtcp-bics”的一部分
 * @project wtcp-bics
 * @author zhanglei
 * @date 2019年10月10日09:12:28
 * @description 目的地关联信息实体类
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

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Entity
@Table(name="bic_destination")
@ApiModel(description="目的地关联信息实体类")
@ToString(callSuper = true)
public class DestinationRelationEntity extends BaseEntity{

    @ApiModelProperty(value = "目的地主键")
    private String destinationId;

    @ApiModelProperty(value = "关联信息ID")
    private String principalId;

    @ApiModelProperty(value = "关联类型")
    private Integer type;

    @ApiModelProperty(value = "组织机构编码")
    private String deptCode;

}