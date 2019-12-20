/**
 * 该源代码文件 ResourceConfigEntity 是工程“wtcp-bics”的一部分
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年12月20日10:59:27
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
@Table(name="t_bic_resource_config")
@ApiModel(description="景区基础信息管理")
@ToString(callSuper = true)
public class ResourceConfigEntity extends BaseEntity {

    @ApiModelProperty(value = "资源编码")
    private String code;

    @ApiModelProperty(value = "资源名称")
    private String name;

    @ApiModelProperty(value = "上级资源编码")
    private String parentCode;

    @ApiModelProperty(value = "查询方式（1 单表查询，0 多表查询）")
    private String queryWay;

    @ApiModelProperty(value = "表名，如 t_bic_scenic")
    private String tableName;

    @ApiModelProperty(value = "类型字段名，如 category")
    private String columnName;
}