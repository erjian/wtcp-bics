/**
 * 该源代码文件 BusinessEntity 是工程“wtcp-bics”的一部分
 *
 * @project wtcp-bics
 * @author 蔺健武
 * @date 2019年10月9日11:29:16
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
@Table(name="t_bic_business")
@ApiModel(description="营业信息管理")
@ToString(callSuper = true)
public class BusinessEntity extends BaseEntity {

    @ApiModelProperty(value = "关联主键ID")
    private String principalId;

    @ApiModelProperty(value = "门票")
    private String ticket;

    @ApiModelProperty(value = "优惠政策")
    private String coupon;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "预定页面")
    private String reserve;

    @ApiModelProperty(value = "门票说明")
    private String ticketNotice;

    @ApiModelProperty(value = "营业说明")
    private String businessNotice;

    @ApiModelProperty(value = "交通说明")
    private String trafficNotice;

    @ApiModelProperty(value = "提示说明")
    private String hintNotice;

    @ApiModelProperty(value = "扩展说明")
    private String extendNotice;

    @ApiModelProperty(value = "服务及设施")
    private String serviceFacility;
}