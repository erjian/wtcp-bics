/**
 * 该源代码文件 EnterpriseEntity 是工程“wtcp-bics”的一部分
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日14:05:04
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
@Table(name="bic_enterprise")
@ApiModel(description="企业信息管理")
@ToString(callSuper = true)
public class EnterpriseEntity extends BaseEntity {

    @ApiModelProperty(value = "关联主键ID")
    private Long principalId;

    @ApiModelProperty(value = "企业名称")
    private String name;

    @ApiModelProperty(value = "企业类型（1：事业单位，2：有限责任公司，3：股份有限公司，4：私营企业非法人，5：个体工商户，6：国有企业）")
    private Integer type;

    @ApiModelProperty(value = "法人")
    private String legalPerson;

    @ApiModelProperty(value = "总经理")
    private String generalManager;

    @ApiModelProperty(value = "营业执照注册号")
    private String businessLicense;

    @ApiModelProperty(value = "组织机构代码证")
    private String organizationalCode;

    @ApiModelProperty(value = "信息填报人")
    private String infoFiller;

    @ApiModelProperty(value = "填报人电话")
    private String fillerPhone;
}